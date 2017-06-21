package ws.routing

import org.jetbrains.ktor.application.call
import org.jetbrains.ktor.routing.Route
import org.jetbrains.ktor.routing.get
import org.jetbrains.ktor.routing.optionalParam
import org.jetbrains.ktor.routing.route
import org.jetbrains.ktor.sessions.sessionOrNull
import service.BirthdayTransactionOperations
import ws.CreateGiftTransactionRequest
import ws.CreateRedistributionTransactionRequest
import ws.TransactionsPageDto
import ws.util.post

fun Route.birthdayTransaction(transactionOperations: BirthdayTransactionOperations) {

    route("fund/{fundId}/transaction") {
        authorized {
            post<CreateRedistributionTransactionRequest>("redistribution") { (fundId, toUserId, description, amount) ->
                val session = call.sessionOrNull<Session>()!!
                val userId = session.userId

                val transaction = transactionOperations.createRedistributionTransaction(fundId, userId, toUserId, description, amount)

                call.respond(transactionToDto(transaction))
            }

            post<CreateGiftTransactionRequest>("gift") { (fundId, receiverId, description, price) ->
                val session = call.sessionOrNull<Session>()!!
                val userId = session.userId

                val transaction = transactionOperations.createGiftTransaction(fundId, userId, receiverId, description, price)

                call.respond(transactionToDto(transaction))
            }


            optionalParam("limit") {
                optionalParam("fromTransactionId") {
                    get {
                        val session = call.sessionOrNull<Session>()!!
                        val userId = session.userId

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


