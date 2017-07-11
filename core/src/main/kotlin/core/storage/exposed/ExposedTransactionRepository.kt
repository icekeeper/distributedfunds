package core.storage.exposed

import core.model.Balance
import core.model.Fund
import core.model.User
import core.model.transaction.Transaction
import core.model.transaction.TransactionShare
import core.model.transaction.TransactionStatus
import core.storage.TransactionFilter
import core.storage.TransactionRepository
import core.storage.dao.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.neq
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
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

    override fun getTransactionsCount(filter: TransactionFilter): Int = transaction {
        Transactions.innerJoin(TransactionShares.source)
                .select(makeOp(filter))
                .count()
    }

    override fun getTransactions(filter: TransactionFilter, limit: Int, offset: Int): List<Transaction> = transaction {
        val transactionEntityIds = Transactions
                .innerJoin(TransactionShares.source)
                .slice(Transactions.id)
                .select(makeOp(filter))
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
                    filter.fund,
                    transactionEntity.amount,
                    shares,
                    transactionEntity.description,
                    Instant.ofEpochMilli(transactionEntity.timestamp.millis),
                    transactionEntity.status)
        }.sortedByDescending { it.id }
    }

    private fun makeOp(filter: TransactionFilter): Op<Boolean> {
        var op: Op<Boolean> = Transactions.fund eq filter.fund.id
        op = filter.user?.let { op and (TransactionShares.user eq it.id) } ?: op
        op = filter.amountEq?.let { op and (Transactions.amount eq it) } ?: op
        op = filter.amountNeq?.let { op and (Transactions.amount neq it) } ?: op
        return op
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

    override fun setTransactionStatus(transaction: Transaction, status: TransactionStatus): Transaction = transaction {
        val transactionEntity = TransactionEntity[transaction.id]
        transactionEntity.status = status
        Transaction(
                transactionEntity.id.value,
                transaction.fund,
                transactionEntity.amount,
                transaction.shares,
                transactionEntity.description,
                Instant.ofEpochMilli(transactionEntity.timestamp.millis),
                transactionEntity.status
        )
    }
}