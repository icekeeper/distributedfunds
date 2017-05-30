package storage

import domain.transaction.GiftTransaction
import domain.transaction.RedistributionTransaction


interface TransactionStorage {

    fun store(transaction: GiftTransaction)

    fun store(transaction: RedistributionTransaction)

}