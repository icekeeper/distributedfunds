package storage.exposed

import model.Balance
import model.Fund
import model.User
import model.transaction.Transaction
import model.transaction.TransactionShare
import model.transaction.TransactionStatus
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.sum
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import storage.TransactionRepository
import storage.dao.*
import java.time.Instant


class ExposedTransactionRepository : TransactionRepository {


    override fun get(id: Long): Transaction? = transaction {
        val transactionEntity = TransactionEntity.findById(id)

        if (transactionEntity != null) {
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
        } else {
            null
        }
    }

    override fun createTransaction(fund: Fund,
                                   amount: Long,
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

    override fun getUserTransactions(fund: Fund, user: User, fromTransactionId: Long, limit: Int): List<Transaction> = transaction {
        val transactionEntityIds = Transactions
                .innerJoin(TransactionShares.source)
                .slice(Transactions.id)
                .select { (TransactionShares.user eq user.id) and (Transactions.fund eq fund.id) and (Transactions.id lessEq fromTransactionId) }
                .orderBy(Transactions.id, isAsc = false)
                .limit(limit)
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
        }.sortedByDescending { it.id }
    }

    override fun getUserBalance(fund: Fund, user: User): Balance = transaction {
        val value = Transactions.innerJoin(TransactionShares.source)
                .slice(TransactionShares.user, TransactionShares.amount.sum())
                .select { (TransactionShares.user eq user.id) and (Transactions.fund eq fund.id) }
                .groupBy(TransactionShares.user)
                .firstOrNull()
                ?.let { it[TransactionShares.amount.sum()] } ?: 0

        Balance(user, fund, value)
    }

    override fun getUserBalances(funds: List<Fund>, user: User): List<Balance> = transaction {
        val fundToBalance: MutableMap<Long, Long> = mutableMapOf()

        Transactions.innerJoin(TransactionShares.source)
                .slice(TransactionShares.user, Transactions.fund, TransactionShares.amount.sum())
                .select { (TransactionShares.user eq user.id) and (Transactions.fund inList funds.map { it.id }) }
                .groupBy(TransactionShares.user, Transactions.fund)
                .forEach { fundToBalance.put(it[Transactions.fund].value, it[TransactionShares.amount.sum()] ?: 0) }

        funds.map { Balance(user, it, fundToBalance.getOrDefault(it.id, 0)) }
    }
}