package core.model.transaction

import core.model.Fund
import java.time.Instant


class Transaction(val id: Long,
                  val fund: Fund,
                  val amount: Long,
                  val shares: Collection<TransactionShare>,
                  var description: String,
                  val timestamp: Instant,
                  val status: TransactionStatus)