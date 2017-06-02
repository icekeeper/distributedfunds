package domain.transaction

import domain.Fund
import domain.User
import java.time.Instant


class GiftTransaction(fund: Fund,
                      source: User,
                      destination: User,
                      amount: Int,
                      description: String,
                      timestamp: Instant,
                      val partakers: List<User>) : Transaction(fund, source, destination, amount, description, timestamp, TransactionStatus.CONFIRMED)