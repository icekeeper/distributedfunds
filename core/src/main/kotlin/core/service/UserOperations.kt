package core.service

import core.model.User

interface UserOperations {

    fun registerUser(login: String, name: String): User

    fun getUser(userId: Long): User

    fun getAllUsers(): List<User>
}