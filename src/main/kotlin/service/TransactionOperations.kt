package service

import domain.transaction.GiftTransaction
import domain.transaction.RedistributionTransaction


interface TransactionOperations {

    fun registerPresent(
            fundId: Long,
            buyerId: Long,
            receiverId: Long,
            price: Int,
            description: String
    ): GiftTransaction

    fun registerRedistribution(
            fundId: Long,
            sourceUserId: Long,
            destinationUserId: Long,
            amount: Int,
            description: String
    ): RedistributionTransaction

    fun confirmRedistribution(transactionId: Long): RedistributionTransaction

    fun cancelRedistribution(transactionId: Long): RedistributionTransaction
}