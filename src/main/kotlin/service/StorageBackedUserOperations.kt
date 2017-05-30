package service

import domain.User
import storage.IdEntityHolder
import storage.UserStorage
import java.time.LocalDate

class StorageBackedUserOperations(val userStorage: UserStorage) : UserOperations {

    override fun registerUser(login: String, name: String, birthday: LocalDate): IdEntityHolder<User> {
        val user = User(login, name, birthday)
        val id = userStorage.store(user)
        return IdEntityHolder(id, user)
    }

    override fun getUser(id: Long): IdEntityHolder<User> {
        return IdEntityHolder(id, userStorage.get(id))
    }
}


