package service.impl

import model.Fund
import model.User
import service.error.EntityNotFoundException
import storage.FundRepository
import storage.TransactionRepository
import storage.UserRepository


class StorageBackedFundOperations(val fundRepository: FundRepository,
                                  val userRepository: UserRepository,
                                  val transactionRepository: TransactionRepository) : service.FundOperations {

    override fun getFund(fundId: Long): Fund {
        val fund = fundRepository.get(fundId) ?: throw EntityNotFoundException("Not found fund with id = $fundId")
        return fund
    }

    override fun createFund(name: String, description: String, creatorUserId: Long): Fund {
        val supervisor = userRepository.get(creatorUserId) ?: throw EntityNotFoundException("Not found user with id = $creatorUserId")
        return fundRepository.create(name, description, supervisor)
    }

    override fun renameFund(fundId: Long, name: String): Fund {
        val fund = fundRepository.get(fundId) ?: throw EntityNotFoundException("Not found fund with id = $fundId")
        return fundRepository.updateName(fund, name)
    }

    override fun changeDescription(fundId: Long, description: String): Fund {
        val fund = fundRepository.get(fundId) ?: throw EntityNotFoundException("Not found fund with id = $fundId")
        return fundRepository.updateDescription(fund, description)
    }

    override fun addUsers(fundId: Long, userIds: List<Long>) {
        val fund = fundRepository.get(fundId) ?: throw EntityNotFoundException("Not found fund with id = $fundId")
        val users = userRepository.get(userIds)

        checkUsers(users, userIds)

        fundRepository.linkUsers(fund, users)
    }

    override fun removeUser(fundId: Long, userIds: List<Long>) {
        val fund = fundRepository.get(fundId) ?: throw EntityNotFoundException("Not found fund with id = $fundId")
        val users = userRepository.get(userIds)

        checkUsers(users, userIds)

        fundRepository.unlinkUsers(fund, users)
    }

    private fun checkUsers(users: List<User>, userIds: List<Long>) {
        if (users.size != userIds.toSet().size) {
            val foundUserIds = users.map { it.id }.toSet()
            throw EntityNotFoundException("Not found users with ids = ${userIds.toSet() - foundUserIds}")
        }
    }

    override fun getFundUsers(fundId: Long): List<model.User> {
        val fund = fundRepository.get(fundId) ?: throw EntityNotFoundException("Not found fund with id = $fundId")
        return fundRepository.getLinkedUsers(fund)
    }

    override fun getFunds(): List<Fund> {
        return fundRepository.getAll()
    }

    override fun getFundUserBalance(fundId: Long, userId: Long): Int {
        val fund = fundRepository.get(fundId) ?: throw EntityNotFoundException("Not found fund with id = $fundId")
        val user = userRepository.get(userId) ?: throw EntityNotFoundException("Not found user with id = $userId")
        return transactionRepository.getUserBalance(fund, user)
    }
}