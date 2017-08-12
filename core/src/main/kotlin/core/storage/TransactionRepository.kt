package core.storage

import core.model.Balance
import core.model.Fund
import core.model.User
import core.model.transaction.Transaction
import core.model.transaction.TransactionShare
import core.model.transaction.TransactionStatus
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

    fun getUsersBalances(fund: Fund): List<Balance>
}