package service

import domain.User
import storage.IdEntityHolder
import java.time.LocalDate

interface UserOperations {

    fun registerUser(login: String, name: String, birthday: LocalDate): IdEntityHolder<User>

    fun getUser(id: Long): IdEntityHolder<User>
}