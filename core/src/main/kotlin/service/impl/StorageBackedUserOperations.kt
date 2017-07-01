package service.impl

import model.User
import service.UserOperations
import service.error.EntityNotFoundException
import service.error.InvalidArgumentException

class StorageBackedUserOperations(val userRepository: storage.UserRepository) : UserOperations {

    override fun registerUser(login: String, name: String): User {
        if (login.isBlank() || login.length > 50) {
            throw InvalidArgumentException("User login must contain at least 1 non space character, but be no longer than 50 characters")
        }
        if (name.isBlank() || name.length > 100) {
            throw InvalidArgumentException("User name must contain at least 1 non space character, but be no longer thant 100 characters")
        }

        return userRepository.create(login, name)
    }

    override fun getUser(userId: Long): User {
        val user = userRepository.get(userId) ?: throw EntityNotFoundException("Not found user with userId = $userId")
        return user
    }

    override fun getAllUsers(): List<User> {
        return userRepository.getAll().sortedBy { it.login }
    }
}


