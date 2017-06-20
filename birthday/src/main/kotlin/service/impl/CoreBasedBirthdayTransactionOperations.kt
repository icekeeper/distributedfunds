package service.impl

import model.transaction.Transaction
import model.transaction.TransactionStatus
import service.BirthdayTransactionOperations
import service.FundOperations
import service.TransactionOperations
import java.util.*


class CoreBasedBirthdayTransactionOperations(val transactionOperations: TransactionOperations,
                                             val fundOperations: FundOperations) : BirthdayTransactionOperations {


    override fun createRedistributionTransaction(fundId: Long,
                                                 fromUserId: Long,
                                                 toUserId: Long,
                                                 description: String,
                                                 amount: Int): Transaction {

        return transactionOperations.createTransaction(fundId, 0, description, listOf(Pair(fromUserId, amount), Pair(toUserId, -amount)))
    }

    override fun createGiftTransaction(fundId: Long,
                                       buyerId: Long,
                                       receiverId: Long,
                                       description: String,
                                       price: Int): Transaction {

        val fundUsers = fundOperations.getFundUsers(fundId)
        val partakerIds = fundUsers.map { it.id }.filter { it != buyerId && it != receiverId }

        val share = price / (partakerIds.size + 1)
        val reminder = price % (partakerIds.size + 1)

        val userAmountPairs = listOf(Pair(buyerId, price - share)) +
                if (reminder == 0) {
                    partakerIds.map { Pair(it, -share) }
                } else {
                    val initial = partakerIds.map { Pair(it, -share) }.toMutableList()
                    Collections.shuffle(initial)
                    val luckyWinners = initial.subList(0, reminder).map { Pair(it.first, it.second - 1) }
                    val loosers = initial.subList(reminder, initial.size)

                    luckyWinners + loosers
                }

        return transactionOperations.createTransaction(fundId, price, description, userAmountPairs, TransactionStatus.CONFIRMED)
    }

    override fun getUserTransactionsCount(fundId: Long, userId: Long): Int {
        return transactionOperations.getUserTransactionsCount(fundId, userId)
    }

    override fun getUserTransactions(fundId: Long, userId: Long, fromTransactionId: Long, limit: Int): List<Transaction> {
        return transactionOperations.getUserTransactions(fundId, userId, fromTransactionId, limit)
    }
}