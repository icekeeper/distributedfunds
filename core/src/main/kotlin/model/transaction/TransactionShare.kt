package model.transaction

import model.User


class TransactionShare(val user: User,
                       val amount: Long)