package domain.transaction

import domain.User


class TransactionShare(val user: User,
                       val amount: Int)