package service

import domain.User
import storage.IdEntityHolder
import storage.UserStorage

class StorageBackedUserOperations(val userStorage: UserStorage) : UserOperations {

    override fun registerUser(login: String, name: String): IdEntityHolder<User> {
        val user = User(login, name)
        val id = userStorage.store(user)
        return IdEntityHolder(id, user)
    }

    override fun getUser(id: Long): IdEntityHolder<User> {
        return IdEntityHolder(id, userStorage.get(id))
    }
}


