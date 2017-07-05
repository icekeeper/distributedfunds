package service.impl

import model.transaction.Transaction
import model.transaction.TransactionStatus
import org.jetbrains.ktor.http.HttpStatusCode
import service.BirthdayTransactionOperations
import service.error.BirthdayErrorCode
import service.error.BirthdayException
import service.error.OperationsErrorCode
import service.error.OperationsException
import storage.FundRepository
import storage.TransactionFilter
import storage.TransactionRepository
import storage.UserRepository
import java.util.*


class CoreBasedBirthdayTransactionOperations(userRepository: UserRepository,
                                             fundRepository: FundRepository,
                                             transactionRepository: TransactionRepository) :
        StorageBackedTransactionOperations(userRepository, fundRepository, transactionRepository), BirthdayTransactionOperations {


    override fun createRedistributionTransaction(fundId: Long,
                                                 fromUserId: Long,
                                                 toUserId: Long,
                                                 description: String,
                                                 amount: Long): Transaction {

        return createTransaction(fundId, 0, description, listOf(Pair(fromUserId, amount), Pair(toUserId, -amount)))
    }

    override fun createGiftTransaction(fundId: Long,
                                       buyerId: Long,
                                       receiverId: Long,
                                       description: String,
                                       price: Long): Transaction {
        val fund = fundRepository.get(fundId) ?: throw OperationsException(OperationsErrorCode.INCORRECT_FUND_ID, parameters = fundId.toString())

        val fundUsers = fundRepository.getLinkedUsers(fund)
        val partakerIds = fundUsers.map { it.id }.filter { it != buyerId && it != receiverId }

        val share = price / (partakerIds.size + 1)
        val reminder = price % (partakerIds.size + 1)

        val userAmountPairs = listOf(Pair(buyerId, price - share)) +
                if (reminder == 0L) {
                    partakerIds.map { Pair(it, -share) }
                } else {
                    val initial = partakerIds.map { Pair(it, -share) }.toMutableList()
                    Collections.shuffle(initial)
                    val luckyWinners = initial.subList(0, reminder.toInt()).map { Pair(it.first, it.second - 1) }
                    val loosers = initial.subList(reminder.toInt(), initial.size)

                    luckyWinners + loosers
                }

        return createTransaction(fundId, price, description, userAmountPairs, TransactionStatus.CONFIRMED)
    }

    override fun getUserRedistributionTransactionsCount(fundId: Long, userId: Long): Int {
        val fund = fundRepository.get(fundId) ?: throw OperationsException(OperationsErrorCode.INCORRECT_FUND_ID, parameters = fundId.toString())
        val user = userRepository.get(userId) ?: throw OperationsException(OperationsErrorCode.INCORRECT_USER_ID, parameters = userId.toString())

        return transactionRepository.getTransactionsCount(TransactionFilter(fund, user, amountEq = 0))
    }

    override fun getUserRedistributionsTransactions(fundId: Long, userId: Long, limit: Int, offset: Int): List<Transaction> {
        val fund = fundRepository.get(fundId) ?: throw OperationsException(OperationsErrorCode.INCORRECT_FUND_ID, parameters = fundId.toString())
        val user = userRepository.get(userId) ?: throw OperationsException(OperationsErrorCode.INCORRECT_USER_ID, parameters = userId.toString())

        return transactionRepository.getTransactions(TransactionFilter(fund, user, amountEq = 0), limit, offset)
    }

    override fun getUserGiftTransactionsCount(fundId: Long, userId: Long): Int {
        val fund = fundRepository.get(fundId) ?: throw OperationsException(OperationsErrorCode.INCORRECT_FUND_ID, parameters = fundId.toString())
        val user = userRepository.get(userId) ?: throw OperationsException(OperationsErrorCode.INCORRECT_USER_ID, parameters = userId.toString())

        return transactionRepository.getTransactionsCount(TransactionFilter(fund, user, amountNeq = 0))
    }

    override fun getUserGiftTransactions(fundId: Long, userId: Long, limit: Int, offset: Int): List<Transaction> {
        val fund = fundRepository.get(fundId) ?: throw OperationsException(OperationsErrorCode.INCORRECT_FUND_ID, parameters = fundId.toString())
        val user = userRepository.get(userId) ?: throw OperationsException(OperationsErrorCode.INCORRECT_USER_ID, parameters = userId.toString())

        return transactionRepository.getTransactions(TransactionFilter(fund, user, amountNeq = 0), limit, offset)
    }

    override fun confirmTransaction(transactionId: Long, userId: Long) {
        val user = userRepository.get(userId) ?: throw OperationsException(OperationsErrorCode.INCORRECT_USER_ID, parameters = userId.toString())
        val transaction = transactionRepository.get(transactionId) ?: throw OperationsException(OperationsErrorCode.INCORRECT_TRANSACTION_ID, parameters = transactionId.toString())

        if (transaction.amount != 0L) {
            throw BirthdayException(BirthdayErrorCode.ACTION_IS_UNAVAILABLE_FOR_TRANSACTION)
        }

        val userShare = transaction.shares.firstOrNull { it.user == user }
        if (userShare == null || userShare.amount > 0) {
            throw BirthdayException(BirthdayErrorCode.FORBIDDEN, HttpStatusCode.Forbidden)
        }

        transactionRepository.setTransactionStatus(transaction, TransactionStatus.CONFIRMED)
    }
}