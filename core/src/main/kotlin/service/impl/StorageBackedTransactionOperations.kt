package service.impl

import model.transaction.Transaction
import model.transaction.TransactionShare
import model.transaction.TransactionStatus
import service.TransactionOperations
import storage.FundRepository
import storage.TransactionRepository
import storage.UserRepository
import java.time.Instant


class StorageBackedTransactionOperations(val userRepository: UserRepository,
                                         val fundRepository: FundRepository,
                                         val transactionRepository: TransactionRepository) : TransactionOperations {

    override fun createTransaction(fundId: Long,
                                   amount: Int,
                                   description: String,
                                   userAmountPairs: List<Pair<Long, Int>>,
                                   status: TransactionStatus): Transaction {
        val fund = fundRepository.get(fundId)
        val users = userRepository.get(userAmountPairs.map { it.first })
        val shares = userAmountPairs.map { (userId, amount) ->
            val user = users.find { it.id == userId }!!
            TransactionShare(user, amount)
        }

        return transactionRepository.createTransaction(fund, amount, description, shares, status, Instant.now())
    }

    override fun getUserTransactionsCount(fundId: Long, userId: Long): Int {
        val fund = fundRepository.get(fundId)
        val user = userRepository.get(userId)

        return transactionRepository.getUserTransactionsCount(fund, user)
    }

    override fun getUserTransactions(fundId: Long, userId: Long, fromTransactionId: Long, limit: Int): List<Transaction> {
        val fund = fundRepository.get(fundId)
        val user = userRepository.get(userId)

        return transactionRepository.getUserTransactions(fund, user, fromTransactionId, limit)
    }
}