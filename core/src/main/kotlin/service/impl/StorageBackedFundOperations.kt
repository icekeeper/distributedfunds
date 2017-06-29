package service.impl

import model.Balance
import model.Fund
import model.User
import service.error.EntityNotFoundException
import service.error.InvalidArgumentException
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
        validateName(name)
        validateDescription(description)

        val supervisor = userRepository.get(creatorUserId) ?: throw EntityNotFoundException("Not found user with id = $creatorUserId")
        return fundRepository.create(name, description, supervisor)
    }

    override fun renameFund(fundId: Long, name: String): Fund {
        validateName(name)
        val fund = fundRepository.get(fundId) ?: throw EntityNotFoundException("Not found fund with id = $fundId")
        return fundRepository.updateName(fund, name)
    }

    override fun changeDescription(fundId: Long, description: String): Fund {
        validateDescription(description)
        val fund = fundRepository.get(fundId) ?: throw EntityNotFoundException("Not found fund with id = $fundId")
        return fundRepository.updateDescription(fund, description)
    }

    override fun addUsers(fundId: Long, userIds: List<Long>) {
        val fund = fundRepository.get(fundId) ?: throw EntityNotFoundException("Not found fund with id = $fundId")
        val users = userRepository.get(userIds)

        validateUsers(users, userIds)

        fundRepository.linkUsers(fund, users)
    }

    override fun removeUser(fundId: Long, userIds: List<Long>) {
        val fund = fundRepository.get(fundId) ?: throw EntityNotFoundException("Not found fund with id = $fundId")
        val users = userRepository.get(userIds)

        validateUsers(users, userIds)

        fundRepository.unlinkUsers(fund, users)
    }

    override fun getFundUsers(fundId: Long): List<model.User> {
        val fund = fundRepository.get(fundId) ?: throw EntityNotFoundException("Not found fund with id = $fundId")
        return fundRepository.getLinkedUsers(fund)
    }

    override fun getFunds(): List<Fund> {
        return fundRepository.getAll()
    }

    override fun getFundUserBalance(fundId: Long, userId: Long): Balance {
        val fund = fundRepository.get(fundId) ?: throw EntityNotFoundException("Not found fund with id = $fundId")
        val user = userRepository.get(userId) ?: throw EntityNotFoundException("Not found user with id = $userId")
        return transactionRepository.getUserBalance(fund, user)
    }

    override fun getFundUserBalances(userId: Long): List<Balance> {
        val user = userRepository.get(userId) ?: throw EntityNotFoundException("Not found user with id = $userId")
        val funds = fundRepository.getUserFunds(user)
        return transactionRepository.getUserBalances(funds, user)
    }

    private fun validateDescription(description: String) {
        if (description.length > 1000) {
            throw InvalidArgumentException("Description must be no longer than 1000 characters ")
        }
    }

    private fun validateName(name: String) {
        if (name.length > 100) {
            throw InvalidArgumentException("Name must be no longer than 100 characters")
        }
    }

    private fun validateUsers(users: List<User>, userIds: List<Long>) {
        if (users.size != userIds.toSet().size) {
            val foundUserIds = users.map { it.id }.toSet()
            throw EntityNotFoundException("Not found users with ids = ${userIds.toSet() - foundUserIds}")
        }
    }
}