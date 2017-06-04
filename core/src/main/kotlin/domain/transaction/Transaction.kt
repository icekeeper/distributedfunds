package domain.transaction

import domain.Fund
import java.time.Instant


class Transaction(val fund: Fund,
                  val amount: Int,
                  val shares: List<TransactionShare>,
                  var description: String,
                  val timestamp: Instant,
                  val status: TransactionStatus)