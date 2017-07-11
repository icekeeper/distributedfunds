package core.storage

import core.model.Fund
import core.model.User

data class TransactionFilter(val fund: Fund,
                             val user: User? = null,
                             val amountEq: Long? = null,
                             val amountNeq: Long? = null)