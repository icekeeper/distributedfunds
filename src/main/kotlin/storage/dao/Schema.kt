package storage.dao

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.transactions.transaction

fun initDao() {
    Database.connect("jdbc:h2:mem:dev;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")
    transaction {
        create(Users, Funds, FundsUsers)
    }
}


