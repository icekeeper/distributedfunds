package storage.exposed

import model.User
import org.jetbrains.exposed.sql.transactions.transaction
import storage.UserRepository
import storage.dao.UserEntity

class ExposedUserRepository : UserRepository {

    companion object {
        fun userFromEntity(entity: UserEntity): User {
            return User(entity.id.value, entity.login, entity.name)
        }
    }

    override fun create(login: String, name: String): User {
        return transaction {
            val entity = UserEntity.new {
                this.login = login
                this.name = name
            }
            userFromEntity(entity)
        }
    }

    override fun get(id: Long): User? {
        return transaction {
            UserEntity.findById(id)?.let { userFromEntity(it) }
        }
    }

    override fun get(ids: List<Long>): List<User> {
        return transaction {
            UserEntity.forIds(ids).map { userFromEntity(it) }
        }
    }

    override fun getAll(): List<User> {
        return transaction {
            UserEntity.all().map { userFromEntity(it) }
        }
    }
}

