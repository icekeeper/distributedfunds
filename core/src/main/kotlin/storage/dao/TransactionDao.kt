package storage.dao

import model.transaction.TransactionStatus
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.LongIdTable
import org.jetbrains.exposed.sql.Table

object Transactions : LongIdTable() {
    val fund = reference("fund", Funds)
    val sourceUser = reference("source", Users)
    val destinationUser = reference("destination", Users)
    val amount = integer("amount")
    val description = varchar("description", 1000)
    val timestamp = datetime("timestamp")
    val status = enumeration("status", TransactionStatus::class.java)
    val type = enumeration("type", TransactionType::class.java)
}

object TransactionPartakers : Table() {
    val transaction = reference("transaction", Transactions)
    val user = reference("user", Users)
}

class TransactionEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<TransactionEntity>(Transactions)

    var fund by FundEntity referencedOn Transactions.fund
    var sourceUser by UserEntity referencedOn Transactions.sourceUser
    var destinationUser by UserEntity referencedOn Transactions.destinationUser
    var amount by Transactions.amount
    var description by Transactions.description
    var timestamp by Transactions.timestamp
    var status by Transactions.status
    var type by Transactions.type

    val partakers by UserEntity referrersOn TransactionPartakers.user
}


enum class TransactionType {
    GIFT, REDISTRIBUTION
}


