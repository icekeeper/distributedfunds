package storage.dao

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.LongIdTable
import org.jetbrains.exposed.sql.Table

object Funds : LongIdTable() {
    val name = varchar("name", 100).uniqueIndex()
    val description = varchar("description", 1000)
    val supervisor = reference("supervisor", Users)
}

object FundsUsers : Table() {
    val user = reference("user", Users)
    val fund = reference("fund", Funds)
}

class FundEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<FundEntity>(Funds)

    var name by Funds.name
    var description by Funds.description
    var supervisor by Funds.supervisor

}


