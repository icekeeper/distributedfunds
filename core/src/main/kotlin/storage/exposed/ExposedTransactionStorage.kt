package storage.exposed

import domain.transaction.GiftTransaction
import domain.transaction.RedistributionTransaction
import domain.transaction.Transaction
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import storage.TransactionStorage
import storage.dao.*


class ExposedTransactionStorage : TransactionStorage {

    override fun store(transaction: GiftTransaction) {
        transaction {
            val transactionEntity = createTransactionEntity(transaction, TransactionType.GIFT)

            val partakers = UserEntity.find { Users.login inList transaction.partakers.map { it.login } }
            partakers.forEach { userEntity ->
                TransactionPartakers.insert {
                    it[TransactionPartakers.transaction] = transactionEntity.id
                    it[TransactionPartakers.user] = userEntity.id
                }
            }
        }
    }

    override fun store(transaction: RedistributionTransaction) {
        transaction {
            createTransactionEntity(transaction, TransactionType.REDISTRIBUTION)
        }
    }

    private fun createTransactionEntity(transaction: Transaction, type: TransactionType): TransactionEntity {
        val fund = FundEntity.find { Funds.name eq transaction.fund.name }.first()
        val sourceUser = UserEntity.find { Users.login eq transaction.source.login }.first()
        val destinationUser = UserEntity.find { Users.login eq transaction.destination.login }.first()

        val transactionEntity = TransactionEntity.new {
            this.fund = fund
            this.sourceUser = sourceUser
            this.destinationUser = destinationUser
            this.amount = transaction.amount
            this.description = transaction.description
            this.timestamp = DateTime(transaction.timestamp.toEpochMilli())
            this.status = transaction.status
            this.type = type
        }
        return transactionEntity
    }

}