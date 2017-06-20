package service

import model.transaction.Transaction


interface BirthdayTransactionOperations {

    fun createRedistributionTransaction(fundId: Long,
                                        fromUserId: Long,
                                        toUserId: Long,
                                        description: String,
                                        amount: Int): Transaction

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
                              price: Int): Transaction


    fun getUserTransactionsCount(fundId: Long,
                                 userId: Long): Int

    fun getUserTransactions(fundId: Long,
                            userId: Long,
                            fromTransactionId: Long,
                            limit: Int): List<Transaction>

}