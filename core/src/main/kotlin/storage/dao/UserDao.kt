package storage.dao

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.LongIdTable

object Users : LongIdTable() {
    val login = varchar("login", 50).uniqueIndex()
    val name = varchar("name", 100)
}

class UserEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<UserEntity>(Users)

    var login by Users.login
    var name by Users.name

}

