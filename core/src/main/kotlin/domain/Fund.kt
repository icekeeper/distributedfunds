package domain

import domain.transaction.GiftTransaction
import domain.transaction.RedistributionTransaction
import domain.transaction.TransactionStatus
import java.time.Instant


class Fund(val name: String, val description: String, val supervisor: User, val users: List<User>) {

    constructor(name: String, description: String, supervisor: User) : this(name, description, supervisor, emptyList())


    fun createGiftTransaction(buyer: User,
                              receiver: User,
                              price: Int,
                              description: String): GiftTransaction {

        val partakers = users.filter { it != buyer && it != receiver }
        return GiftTransaction(this, buyer, receiver, price, description, Instant.now(), partakers)
    }

    fun createRedistributionTransaction(from: User,
                                        to: User,
                                        amount: Int,
                                        description: String): RedistributionTransaction {

        return RedistributionTransaction(this, from, to, amount, description, Instant.now(), TransactionStatus.PENDING)
    }

}