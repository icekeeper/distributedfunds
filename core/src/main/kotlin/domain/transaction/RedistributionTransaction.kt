package domain.transaction

import domain.Fund
import domain.User
import java.time.Instant


class RedistributionTransaction(fund: Fund,
                                source: User,
                                destination: User,
                                amount: Int,
                                description: String,
                                timestamp: Instant,
                                status: TransactionStatus) : Transaction(fund, source, destination, amount, description, timestamp, status)

