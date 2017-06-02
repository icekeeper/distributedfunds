package domain.transaction

import domain.Fund
import domain.User
import java.time.Instant


abstract class Transaction(val fund: Fund,
                           val source: User,
                           val destination: User,
                           val amount: Int,
                           var description: String,
                           val timestamp: Instant,
                           val status: TransactionStatus)