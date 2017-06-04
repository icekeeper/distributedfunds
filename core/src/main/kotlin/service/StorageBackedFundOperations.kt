package service

import domain.Fund
import domain.User
import storage.FundStorage
import storage.IdEntityHolder
import storage.UserStorage
import storage.model.FundDescription


class StorageBackedFundOperations(val fundStorage: FundStorage, val userStorage: UserStorage) : FundOperations {

    override fun getFund(fundId: Long): IdEntityHolder<Fund> {
        val fund = fundStorage.get(fundId)
        return IdEntityHolder(fundId, fund)
    }

    override fun createFund(name: String, description: String, creatorUserId: Long): IdEntityHolder<Fund> {
        val supervisor = userStorage.get(creatorUserId)
        val fund = Fund(name, description, supervisor)
        val id = fundStorage.store(fund)
        return IdEntityHolder(id, fund)
    }

    override fun renameFund(fundId: Long, name: String): IdEntityHolder<Fund> {
        val fund = fundStorage.updateName(fundId, name)
        return IdEntityHolder(fundId, fund)
    }

    override fun changeDescription(fundId: Long, description: String): IdEntityHolder<Fund> {
        val fund = fundStorage.updateDescription(fundId, description)
        return IdEntityHolder(fundId, fund)
    }

    override fun getSupervisor(fundId: Long): IdEntityHolder<User> {
        val supervisorId = fundStorage.getSupervisorId(fundId)
        val user = userStorage.get(supervisorId)
        return IdEntityHolder(supervisorId, user)
    }

    override fun addUsers(fundId: Long, userIds: List<Long>) {
        fundStorage.linkUsers(fundId, userIds)
    }

    override fun removeUser(fundId: Long, userIds: List<Long>) {
        fundStorage.unlinkUsers(fundId, userIds)
    }

    override fun getFundUsersWithIds(fundId: Long): List<IdEntityHolder<User>> {
        val userIds = fundStorage.getLinkedUserIds(fundId)
        val users = userStorage.get(userIds)
        return userIds.zip(users).map { IdEntityHolder(it.first, it.second) }
    }

    override fun getDescriptions(): List<IdEntityHolder<FundDescription>> {
        return fundStorage.getFundDescriptions().sortedBy { it.id }
    }
}