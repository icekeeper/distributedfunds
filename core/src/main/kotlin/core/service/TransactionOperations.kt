package core.service

import core.model.transaction.Transaction
import core.model.transaction.TransactionStatus

interface TransactionOperations {


    fun createTransaction(fundId: Long,
                          amount: Long,
                          description: String,
                          userAmountPairs: List<Pair<Long, Long>>,
                          status: TransactionStatus = TransactionStatus.PENDING): Transaction

    fun getTransactionsCount(fundId: Long): Int

    fun getTransactions(fundId: Long, limit: Int, offset: Int): List<Transaction>


}


