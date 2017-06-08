package service

import model.User
import storage.UserRepository

class StorageBackedUserOperations(val userRepository: UserRepository) : UserOperations {

    override fun registerUser(login: String, name: String): User {
        return userRepository.create(login, name)
    }

    override fun getUser(id: Long): User {
        return userRepository.get(id)
    }
}


