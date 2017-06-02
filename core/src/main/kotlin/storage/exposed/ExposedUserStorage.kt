package storage.exposed

import domain.User
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import storage.UserStorage
import storage.dao.UserEntity
import java.time.LocalDate

class ExposedUserStorage : UserStorage {

    companion object {
        fun userFromEntity(entity: UserEntity): User {
            val birthday = entity.birthday
            val birthdayLocalDate = LocalDate.of(birthday.year, birthday.monthOfYear, birthday.dayOfMonth)
            return User(entity.login, entity.name, birthdayLocalDate)
        }
    }

    override fun store(user: User): Long {
        return transaction {
            UserEntity.new {
                this.login = user.login
                this.name = user.name
                val userBirthday = user.birthday
                this.birthday = DateTime(userBirthday.year, userBirthday.monthValue, userBirthday.dayOfMonth, 0, 0)
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

