package service

import model.transaction.Transaction
import model.transaction.TransactionStatus

interface TransactionOperations {


    fun createTransaction(fundId: Long,
                          amount: Long,
                          description: String,
                          userAmountPairs: List<Pair<Long, Long>>,
                          status: TransactionStatus = TransactionStatus.PENDING): Transaction

    fun getTransactionsCount(fundId: Long): Int

    fun getTransactions(fundId: Long, limit: Int, offset: Int): List<Transaction>


}


