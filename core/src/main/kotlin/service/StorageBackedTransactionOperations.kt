package service

import domain.transaction.GiftTransaction
import domain.transaction.RedistributionTransaction
import storage.FundStorage
import storage.TransactionStorage
import storage.UserStorage


class StorageBackedTransactionOperations(val transactionStorage: TransactionStorage,
                                         val fundStorage: FundStorage,
                                         val userStorage: UserStorage) : TransactionOperations {

    override fun registerPresent(fundId: Long,
                                 buyerId: Long,
                                 receiverId: Long,
                                 price: Int,
                                 description: String): GiftTransaction {

        val fund = fundStorage.get(fundId)
        val buyer = userStorage.get(buyerId)
        val receiver = userStorage.get(receiverId)

        val transaction = fund.createGiftTransaction(buyer, receiver, price, description)
        transactionStorage.store(transaction)

        return transaction
    }

    override fun registerRedistribution(fundId: Long,
                                        sourceUserId: Long,
                                        destinationUserId: Long,
                                        amount: Int,
                                        description: String): RedistributionTransaction {
        val fund = fundStorage.get(fundId)
        val source = userStorage.get(sourceUserId)
        val destination = userStorage.get(destinationUserId)

        val transaction = fund.createRedistributionTransaction(source, destination, amount, description)
        transactionStorage.store(transaction)

        return transaction
    }

    override fun confirmRedistribution(transactionId: Long): RedistributionTransaction {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun cancelRedistribution(transactionId: Long): RedistributionTransaction {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}