package ws.routing

import model.transaction.Transaction
import org.jetbrains.ktor.application.call
import org.jetbrains.ktor.routing.*
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

        param("userId") {
            optionalParam("limit") {
                optionalParam("fromTransactionId") {
                    get {
                        val userId = call.parameters["userId"]?.toLong()!!
                        val fundId = call.parameters["fundId"]?.toLong()!!

                        val limit = call.parameters["limit"]?.toInt() ?: 10
                        val fromTransactionId = call.parameters["fromTransactionId"]?.toLong() ?: Long.MAX_VALUE

                        val userTransactionsCount = transactionOperations.getUserTransactionsCount(fundId, userId)
                        val userTransactions = transactionOperations.getUserTransactions(fundId, userId, fromTransactionId, limit)

                        val userTransactionDtos = userTransactions.map { transactionToDto(it) }

                        val page = TransactionsPageDto(userTransactionDtos, fromTransactionId, limit, userTransactionsCount)

                        call.respond(page)
                    }
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
