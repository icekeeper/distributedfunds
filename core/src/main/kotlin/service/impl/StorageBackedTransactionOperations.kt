package service.impl

import model.transaction.Transaction
import model.transaction.TransactionShare
import model.transaction.TransactionStatus
import service.TransactionOperations
import service.error.EntityNotFoundException
import service.error.InvalidArgumentException
import storage.FundRepository
import storage.TransactionRepository
import storage.UserRepository
import java.time.Instant


class StorageBackedTransactionOperations(val userRepository: UserRepository,
                                         val fundRepository: FundRepository,
                                         val transactionRepository: TransactionRepository) : TransactionOperations {

    override fun createTransaction(fundId: Long,
                                   amount: Long,
                                   description: String,
                                   userAmountPairs: List<Pair<Long, Long>>,
                                   status: TransactionStatus): Transaction {
        if (description.length > 1000) {
            throw InvalidArgumentException("Description must be no longer than 1000 characters")
        }

        val fund = fundRepository.get(fundId) ?: throw EntityNotFoundException("Not found fund with id = $fundId")
        val users = userRepository.get(userAmountPairs.map { it.first })

        if (users.size != userAmountPairs.map { it.first }.toSet().size) {
            val searchUserIds = userAmountPairs.map { it.first }.toSet()
            val foundUserIds = users.map { it.id }.toSet()
            throw EntityNotFoundException("Not found users with ids = ${searchUserIds - foundUserIds}")
        }

        val shares = userAmountPairs.map { (userId, amount) ->
            val user = users.find { it.id == userId }!!
            TransactionShare(user, amount)
        }

        val balanceSum = userAmountPairs.map { it.second }.reduce { acc, i -> acc + i }
        if (balanceSum != 0L) {
            throw InvalidArgumentException("Sum of all balance adjustments must be 0")
        }

        return transactionRepository.createTransaction(fund, amount, description, shares, status, Instant.now())
    }

    override fun getUserTransactionsCount(fundId: Long, userId: Long): Int {
        val fund = fundRepository.get(fundId) ?: throw EntityNotFoundException("Not found fund with id = $fundId")
        val user = userRepository.get(userId) ?: throw EntityNotFoundException("Not found user with id = $userId")

        return transactionRepository.getUserTransactionsCount(fund, user)
    }

    override fun getUserTransactions(fundId: Long, userId: Long, fromTransactionId: Long, limit: Int): List<Transaction> {
        val fund = fundRepository.get(fundId) ?: throw EntityNotFoundException("Not found fund with id = $fundId")
        val user = userRepository.get(userId) ?: throw EntityNotFoundException("Not found user with id = $userId")

        return transactionRepository.getUserTransactions(fund, user, fromTransactionId, limit)
    }
}