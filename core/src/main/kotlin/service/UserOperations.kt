package service

import model.User

interface UserOperations {

    fun registerUser(login: String, name: String): User

    fun getUser(id: Long): User
}