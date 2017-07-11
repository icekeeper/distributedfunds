package core.service.impl

import core.model.User
import core.service.UserOperations
import core.service.error.OperationsErrorCode
import core.service.error.OperationsException

class StorageBackedUserOperations(val userRepository: core.storage.UserRepository) : UserOperations {

    override fun registerUser(login: String, name: String): User {
        if (login.isBlank() || login.length > 50) {
            throw OperationsException(OperationsErrorCode.INVALID_LOGIN, parameters = login)
        }
        if (name.isBlank() || name.length > 100) {
            throw OperationsException(OperationsErrorCode.INVALID_USER_NAME, parameters = name)
        }

        if (userRepository.get(login) != null) {
            throw OperationsException(OperationsErrorCode.DUPLICATE_LOGIN, parameters = login)
        }

        return userRepository.create(login, name)
    }

    override fun getUser(userId: Long): User {
        val user = userRepository.get(userId) ?: throw OperationsException(OperationsErrorCode.INCORRECT_USER_ID, parameters = userId.toString())
        return user
    }

    override fun getAllUsers(): List<User> {
        return userRepository.getAll().sortedBy { it.login }
    }
}


