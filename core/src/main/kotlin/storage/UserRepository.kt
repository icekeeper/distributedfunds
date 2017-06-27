package storage

import model.User


interface UserRepository {

    fun create(login: String, name: String): User

    fun get(id: Long): User?

    fun get(ids: List<Long>): List<User>
}