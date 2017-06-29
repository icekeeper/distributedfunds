package service

import model.transaction.Transaction
import model.transaction.TransactionStatus

interface TransactionOperations {


    fun createTransaction(fundId: Long,
                          amount: Long,
                          description: String,
                          userAmountPairs: List<Pair<Long, Long>>,
                          status: TransactionStatus = TransactionStatus.PENDING): Transaction

    fun getUserTransactionsCount(fundId: Long,
                                 userId: Long): Int

    fun getUserTransactions(fundId: Long,
                            userId: Long,
                            fromTransactionId: Long,
                            limit: Int): List<Transaction>


}


