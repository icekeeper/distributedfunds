package storage

import domain.User


interface UserStorage {

    fun store(user: User): Long

    fun get(id: Long): User

    fun get(ids: List<Long>): List<User>
}