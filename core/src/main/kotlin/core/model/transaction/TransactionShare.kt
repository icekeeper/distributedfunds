package core.model.transaction

import core.model.User


class TransactionShare(val user: User,
                       val amount: Long)