package core.storage

import core.model.Fund
import core.model.User


interface FundRepository {

    fun create(name: String, description: String, supervisor: User): Fund

    fun get(id: Long): Fund?

    fun getAll(): List<Fund>

    fun updateName(fund: Fund, name: String): Fund

    fun updateDescription(fund: Fund, description: String): Fund

    fun linkUsers(fund: Fund, users: List<User>)

    fun unlinkUsers(fund: Fund, users: List<User>)

    fun getLinkedUsers(fund: Fund): List<User>

    fun getUserFunds(user: User): List<Fund>

}