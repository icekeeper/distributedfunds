package service.impl

import model.transaction.Transaction
import model.transaction.TransactionShare
import model.transaction.TransactionStatus
import service.TransactionOperations
import service.error.OperationsErrorCode
import service.error.OperationsException
import storage.FundRepository
import storage.TransactionFilter
import storage.TransactionRepository
import storage.UserRepository
import java.time.Instant


open class StorageBackedTransactionOperations(val userRepository: UserRepository,
                                              val fundRepository: FundRepository,
                                              val transactionRepository: TransactionRepository) : TransactionOperations {

    override fun createTransaction(fundId: Long,
                                   amount: Long,
                                   description: String,
                                   userAmountPairs: List<Pair<Long, Long>>,
                                   status: TransactionStatus): Transaction {
        if (description.length > 100) {
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

    override fun getTransactionsCount(fundId: Long): Int {
        val fund = fundRepository.get(fundId) ?: throw OperationsException(OperationsErrorCode.INCORRECT_FUND_ID, parameters = fundId.toString())
        return transactionRepository.getTransactionsCount(TransactionFilter(fund))
    }

    override fun getTransactions(fundId: Long, limit: Int, offset: Int): List<Transaction> {
        val fund = fundRepository.get(fundId) ?: throw OperationsException(OperationsErrorCode.INCORRECT_FUND_ID, parameters = fundId.toString())
        return transactionRepository.getTransactions(TransactionFilter(fund), limit, offset)
    }
}