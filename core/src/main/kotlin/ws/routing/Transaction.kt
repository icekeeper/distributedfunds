package ws.routing

import model.transaction.Transaction
import org.jetbrains.ktor.application.call
import org.jetbrains.ktor.routing.Route
import org.jetbrains.ktor.routing.get
import org.jetbrains.ktor.routing.optionalParam
import org.jetbrains.ktor.routing.route
import service.TransactionOperations
import ws.CreateTransactionRequestDto
import ws.TransactionDto
import ws.TransactionShareDto
import ws.TransactionsPageDto
import ws.util.post

fun Route.transaction(transactionOperations: TransactionOperations) {

    route("fund/{fundId}/transaction") {
        post<CreateTransactionRequestDto> { (fundId, amount, description, shares) ->
            val transaction = transactionOperations.createTransaction(
                    fundId,
                    amount,
                    description,
                    shares.map { Pair(it.userId, it.amount) })

            call.respond(transactionToDto(transaction))
        }

        optionalParam("limit") {
            optionalParam("offset") {
                get {
                    val fundId = call.parameters["fundId"]?.toLong()!!

                    val limit = call.parameters["limit"]?.toInt() ?: 10
                    val offset = call.parameters["offset"]?.toInt() ?: 0

                    val userTransactionsCount = transactionOperations.getTransactionsCount(fundId)
                    val userTransactions = transactionOperations.getTransactions(fundId, limit, offset)

                    val userTransactionDtos = userTransactions.map { transactionToDto(it) }

                    val page = TransactionsPageDto(userTransactionDtos, limit, offset, userTransactionsCount)

                    call.respond(page)
                }
            }


        }

    }


}


fun transactionToDto(transaction: Transaction): TransactionDto {
    val shares = transaction.shares.map {
        TransactionShareDto(userToDto(it.user), it.amount)
    }

    return TransactionDto(transaction.id,
            transaction.fund.id,
            transaction.amount,
            transaction.description,
            transaction.timestamp.toEpochMilli(),
            transaction.status,
            shares)
}

fun moneyToString(amount: Long, addPlus: Boolean = false): String {
    val sign = if (amount < 0) "-" else if (addPlus) "+" else ""
    val decimal = Math.abs(amount) / 100
    val fraction = Math.abs(amount) % 100
    return "%s%d.%02d".format(sign, decimal, fraction)
}
