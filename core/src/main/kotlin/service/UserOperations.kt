package service

import domain.User
import storage.IdEntityHolder

interface UserOperations {

    fun registerUser(login: String, name: String): IdEntityHolder<User>

    fun getUser(id: Long): IdEntityHolder<User>
}