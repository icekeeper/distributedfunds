package storage.exposed

import model.Fund
import model.User
import model.transaction.Transaction
import model.transaction.TransactionShare
import model.transaction.TransactionStatus
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import storage.TransactionRepository
import storage.dao.*
import java.time.Instant


class ExposedTransactionRepository : TransactionRepository {


    override fun get(id: Long): Transaction = transaction {
        val transactionEntity = TransactionEntity[id]
        val fundEntity = transactionEntity.fund

        val sharesData = TransactionShares.select { TransactionShares.transaction eq transactionEntity.id }.map {
            val user = it[TransactionShares.user].value
            val amount = it[TransactionShares.amount]
            Pair(user, amount)
        }

        val userEntities = UserEntity.forIds(sharesData.map { it.first })

        val shares = sharesData.map { (userId, amount) ->
            val userEntity = userEntities.find { it.id.value == userId }!!
            val user = ExposedUserRepository.userFromEntity(userEntity)
            TransactionShare(user, amount)
        }


        Transaction(
                transactionEntity.id.value,
                ExposedFundRepository.fundFromEntity(fundEntity),
                transactionEntity.amount,
                shares,
                transactionEntity.description,
                Instant.ofEpochMilli(transactionEntity.timestamp.millis),
                transactionEntity.status
        )
    }

    override fun createTransaction(fund: Fund,
                                   amount: Int,
                                   description: String,
                                   shares: List<TransactionShare>,
                                   status: TransactionStatus,
                                   timestamp: Instant): Transaction = transaction {

        val fundEntity = FundEntity[fund.id]

        val transactionEntity = TransactionEntity.new {
            this.fund = fundEntity
            this.amount = amount
            this.description = description
            this.status = status
            this.timestamp = DateTime(timestamp.toEpochMilli())
        }

        shares.forEach { share ->
            TransactionShares.insert {
                it[TransactionShares.transaction] = transactionEntity.id
                it[TransactionShares.user] = UserEntity[share.user.id].id
                it[TransactionShares.amount] = share.amount
            }
        }

        Transaction(transactionEntity.id.value, fund, amount, shares, description, timestamp, status)
    }

    override fun getUserTransactionsCount(fund: Fund, user: User): Int = transaction {
        Transactions.innerJoin(TransactionShares.source)
                .select { (TransactionShares.user eq user.id) and (Transactions.fund eq fund.id) }
                .count()
    }

    override fun getUserTransactions(fund: Fund, user: User, limit: Int, offset: Int): List<Transaction> = transaction {
        val transactionEntityIds = Transactions
                .innerJoin(TransactionShares.source)
                .select { (TransactionShares.user eq user.id) and (Transactions.fund eq fund.id) }
                .orderBy(Transactions.id)
                .limit(limit, offset = offset)
                .map { it[Transactions.id] }


        val transactionsSharesData = TransactionShares
                .select { TransactionShares.transaction inList transactionEntityIds }
                .map { Triple(it[TransactionShares.transaction], it[TransactionShares.user], it[TransactionShares.amount]) }

        val transactionEntities = TransactionEntity.forEntityIds(transactionEntityIds)

        val users = UserEntity.forEntityIds(transactionsSharesData.map { it.second })
                .map { ExposedUserRepository.userFromEntity(it) }

        transactionEntities.map { transactionEntity ->
            val shares = transactionsSharesData
                    .filter { it.first.value == transactionEntity.id.value }
                    .map { (_, userId, amount) ->
                        val shareUser = users.find { it.id == userId.value }!!
                        TransactionShare(shareUser, amount)
                    }

            Transaction(transactionEntity.id.value,
                    fund,
                    transactionEntity.amount,
                    shares,
                    transactionEntity.description,
                    Instant.ofEpochMilli(transactionEntity.timestamp.millis),
                    transactionEntity.status)
        }


    }
}