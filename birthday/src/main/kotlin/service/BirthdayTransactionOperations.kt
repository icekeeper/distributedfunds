package service

import model.transaction.Transaction


interface BirthdayTransactionOperations {

    fun createRedistributionTransaction(fundId: Long,
                                        fromUserId: Long,
                                        toUserId: Long,
                                        description: String,
                                        amount: Long): Transaction

    /* Common scenario for gift transaction:
     * one person buys a gift while all other members of a fund
     * except for receiver of a present share expenses.
     *
     * So in terms of domain model, person who buys a present
     * participate in transaction and increase his/her balance in a fund for (gift price – common share).
     * Other partakers decrease their balances for the common share size.
     *
     * If gift price is not divides evenly between partakers, then reminder is split between some randomly chosen partakers.
     */
    fun createGiftTransaction(fundId: Long,
                              buyerId: Long,
                              receiverId: Long,
                              description: String,
                              price: Long): Transaction


    fun getUserRedistributionTransactionsCount(fundId: Long,
                                               userId: Long): Int

    fun getUserRedistributionsTransactions(fundId: Long,
                                           userId: Long,
                                           limit: Int,
                                           offset: Int): List<Transaction>

    fun getUserGiftTransactionsCount(fundId: Long,
                                     userId: Long): Int

    fun getUserGiftTransactions(fundId: Long,
                                userId: Long,
                                limit: Int,
                                offset: Int): List<Transaction>

    fun confirmTransaction(transactionId: Long, userId: Long)

}