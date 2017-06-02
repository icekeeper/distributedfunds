package storage


data class IdEntityHolder<out T>(val id: Long, val entity: T)