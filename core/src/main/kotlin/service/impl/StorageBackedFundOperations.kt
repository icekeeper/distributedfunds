package service.impl

import storage.FundRepository
import storage.TransactionRepository
import storage.UserRepository


class StorageBackedFundOperations(val fundRepository: FundRepository,
                                  val userRepository: UserRepository,
                                  val transactionRepository: TransactionRepository) : service.FundOperations {

    override fun getFund(fundId: Long): model.Fund {
        return fundRepository.get(fundId)
    }

    override fun createFund(name: String, description: String, creatorUserId: Long): model.Fund {
        val supervisor = userRepository.get(creatorUserId)
        return fundRepository.create(name, description, supervisor)
    }

    override fun renameFund(fundId: Long, name: String): model.Fund {
        val fund = fundRepository.get(fundId)
        return fundRepository.updateName(fund, name)
    }

    override fun changeDescription(fundId: Long, description: String): model.Fund {
        val fund = fundRepository.get(fundId)
        return fundRepository.updateDescription(fund, description)
    }

    override fun addUsers(fundId: Long, userIds: List<Long>) {
        val fund = fundRepository.get(fundId)
        val users = userRepository.get(userIds)
        fundRepository.linkUsers(fund, users)
    }

    override fun removeUser(fundId: Long, userIds: List<Long>) {
        val fund = fundRepository.get(fundId)
        val users = userRepository.get(userIds)
        fundRepository.unlinkUsers(fund, users)
    }

    override fun getFundUsers(fundId: Long): List<model.User> {
        val fund = fundRepository.get(fundId)
        return fundRepository.getLinkedUsers(fund)
    }

    override fun getFunds(): List<model.Fund> {
        return fundRepository.getAll()
    }

    override fun getFundUserBalance(fundId: Long, userId: Long): Int {
        val fund = fundRepository.get(fundId)
        val user = userRepository.get(userId)
        return transactionRepository.getUserBalance(fund, user)
    }
}