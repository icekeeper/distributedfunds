package service

import model.transaction.Transaction

interface TransactionOperations {


    fun createTransaction(fundId: Long,
                          amount: Int,
                          description: String,
                          userAmountPairs: List<Pair<Long, Int>>): Transaction

    fun getUserTransactionsCount(fundId: Long,
                                 userId: Long): Int

    fun getUserTransactions(fundId: Long,
                            userId: Long,
                            fromTransactionId: Long,
                            limit: Int): List<Transaction>


}


