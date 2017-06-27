package service.impl

import model.User
import service.error.EntityNotFoundException

class StorageBackedUserOperations(val userRepository: storage.UserRepository) : service.UserOperations {

    override fun registerUser(login: String, name: String): User {
        return userRepository.create(login, name)
    }

    override fun getUser(userId: Long): User {
        val user = userRepository.get(userId) ?: throw EntityNotFoundException("Not found user with userId = $userId")
        return user
    }
}


