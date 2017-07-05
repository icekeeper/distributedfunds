package storage

import model.Balance
import model.Fund
import model.User
import model.transaction.Transaction
import model.transaction.TransactionShare
import model.transaction.TransactionStatus
import java.time.Instant


interface TransactionRepository {

    fun get(id: Long): Transaction?

    fun createTransaction(fund: Fund,
                          amount: Long,
                          description: String,
                          shares: List<TransactionShare>,
                          status: TransactionStatus,
                          timestamp: Instant): Transaction

    fun getTransactionsCount(filter: TransactionFilter): Int

    fun getTransactions(filter: TransactionFilter, limit: Int, offset: Int): List<Transaction>

    fun setTransactionStatus(transaction: Transaction, status: TransactionStatus): Transaction

    fun getUserBalance(fund: Fund, user: User): Balance

    fun getUserBalances(funds: List<Fund>, user: User): List<Balance>
}

data class TransactionFilter(val fund: Fund,
                             val user: User? = null,
                             val amountEq: Long? = null,
                             val amountNeq: Long? = null)