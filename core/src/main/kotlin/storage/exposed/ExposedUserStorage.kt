package storage.exposed

import domain.User
import org.jetbrains.exposed.sql.transactions.transaction
import storage.UserStorage
import storage.dao.UserEntity

class ExposedUserStorage : UserStorage {

    companion object {
        fun userFromEntity(entity: UserEntity): User {
            return User(entity.login, entity.name)
        }
    }

    override fun store(user: User): Long {
        return transaction {
            UserEntity.new {
                this.login = user.login
                this.name = user.name
            }.id.value
        }
    }

    override fun get(id: Long): User {
        return transaction {
            val entity = UserEntity[id]
            userFromEntity(entity)
        }
    }

    override fun get(ids: List<Long>): List<User> {
        return transaction {
            UserEntity.forIds(ids).map { userFromEntity(it) }
        }
    }
}

