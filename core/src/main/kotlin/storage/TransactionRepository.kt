package storage

import model.Fund
import model.User
import model.transaction.Transaction
import model.transaction.TransactionShare
import model.transaction.TransactionStatus
import java.time.Instant


interface TransactionRepository {

    fun get(id: Long): Transaction

    fun createTransaction(fund: Fund,
                          amount: Int,
                          description: String,
                          shares: List<TransactionShare>,
                          status: TransactionStatus,
                          timestamp: Instant): Transaction

    fun getUserTransactionsCount(fund: Fund, user: User): Int

    fun getUserTransactions(fund: Fund, user: User, fromTransactionId: Long, limit: Int): List<Transaction>

    fun getUserBalance(fund: Fund, user: User): Int
}