package service

import domain.Fund
import domain.User
import storage.IdEntityHolder
import storage.model.FundDescription


interface FundOperations {

    fun getDescriptions(): List<IdEntityHolder<FundDescription>>

    fun getFund(fundId: Long): IdEntityHolder<Fund>

    fun createFund(name: String, description: String, creatorUserId: Long): IdEntityHolder<Fund>

    fun renameFund(fundId: Long, name: String): IdEntityHolder<Fund>

    fun changeDescription(fundId: Long, description: String): IdEntityHolder<Fund>

    fun getSupervisor(fundId: Long): IdEntityHolder<User>

    fun addUsers(fundId: Long, userIds: List<Long>)

    fun removeUser(fundId: Long, userIds: List<Long>)

    fun getFundUsersWithIds(fundId: Long): List<IdEntityHolder<User>>
}

