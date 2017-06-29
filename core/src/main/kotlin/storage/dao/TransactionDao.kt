package storage.dao

import model.transaction.TransactionStatus
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.LongIdTable
import org.jetbrains.exposed.sql.Table

object Transactions : LongIdTable() {
    val fund = reference("fund", Funds).index()
    val amount = long("amount")
    val description = varchar("description", 1000)
    val timestamp = datetime("timestamp")
    val status = enumeration("status", TransactionStatus::class.java)
}

object TransactionShares : Table() {
    val transaction = reference("transaction", Transactions).index()
    val user = reference("user", Users).index()
    val amount = long("amount")

    init {
        this.index(true, transaction, user)
    }
}

class TransactionEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<TransactionEntity>(Transactions)

    var fund by FundEntity referencedOn Transactions.fund
    var amount by Transactions.amount
    var description by Transactions.description
    var timestamp by Transactions.timestamp
    var status by Transactions.status
}
