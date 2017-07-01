package service.impl

import model.transaction.Transaction
import model.transaction.TransactionShare
import model.transaction.TransactionStatus
import service.TransactionOperations
import service.error.OperationsErrorCode
import service.error.OperationsException
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
            throw OperationsException(OperationsErrorCode.TRANSACTION_DESCRIPTION_TOO_LONG, parameters = description.length.toString())
        }

        val fund = fundRepository.get(fundId) ?: throw OperationsException(OperationsErrorCode.INCORRECT_FUND_ID, parameters = fundId.toString())
        val users = userRepository.get(userAmountPairs.map { it.first })

        if (users.size != userAmountPairs.map { it.first }.toSet().size) {
            val searchUserIds = userAmountPairs.map { it.first }.toSet()
            val foundUserIds = users.map { it.id }.toSet()
            throw OperationsException(OperationsErrorCode.INCORRECT_USER_ID, parameters = (searchUserIds - foundUserIds).first().toString())
        }

        val shares = userAmountPairs.map { (userId, amount) ->
            val user = users.find { it.id == userId }!!
            TransactionShare(user, amount)
        }

        val balanceSum = userAmountPairs.map { it.second }.reduce { acc, i -> acc + i }
        if (balanceSum != 0L) {
            throw OperationsException(OperationsErrorCode.TRANSACTION_BALANCES_DELTAS_SUM_IS_NOT_ZERO)
        }

        return transactionRepository.createTransaction(fund, amount, description, shares, status, Instant.now())
    }

    override fun getUserTransactionsCount(fundId: Long, userId: Long): Int {
        val fund = fundRepository.get(fundId) ?: throw OperationsException(OperationsErrorCode.INCORRECT_FUND_ID, parameters = fundId.toString())
        val user = userRepository.get(userId) ?: throw OperationsException(OperationsErrorCode.INCORRECT_USER_ID, parameters = userId.toString())

        return transactionRepository.getUserTransactionsCount(fund, user)
    }

    override fun getUserTransactions(fundId: Long, userId: Long, fromTransactionId: Long, limit: Int): List<Transaction> {
        val fund = fundRepository.get(fundId) ?: throw OperationsException(OperationsErrorCode.INCORRECT_FUND_ID, parameters = fundId.toString())
        val user = userRepository.get(userId) ?: throw OperationsException(OperationsErrorCode.INCORRECT_USER_ID, parameters = userId.toString())

        return transactionRepository.getUserTransactions(fund, user, fromTransactionId, limit)
    }
}