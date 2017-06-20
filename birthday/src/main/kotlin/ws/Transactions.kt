package ws

import com.google.gson.Gson
import org.jetbrains.ktor.application.call
import org.jetbrains.ktor.application.receive
import org.jetbrains.ktor.routing.*
import org.jetbrains.ktor.sessions.sessionOrNull
import service.BirthdayTransactionOperations

fun Route.birthdayTransaction(gson: Gson,
                              transactionOperations: BirthdayTransactionOperations) {

    route("fund/{fundId}/transaction") {
        authorized {

            post("redistribution") {
                val session = call.sessionOrNull<Session>()!!
                val userId = session.userId

                val body = call.request.receive<String>()
                val createTransactionRequest = gson.fromJson(body, CreateRedistributionTransactionRequest::class.java)

                val transaction = transactionOperations.createRedistributionTransaction(
                        createTransactionRequest.fundId,
                        userId,
                        createTransactionRequest.toUserId,
                        createTransactionRequest.description,
                        createTransactionRequest.amount
                )

                call.respond(transactionToDto(transaction))
            }

            post("gift") {
                val session = call.sessionOrNull<Session>()!!
                val userId = session.userId

                val body = call.request.receive<String>()
                val createTransactionRequest = gson.fromJson(body, CreateGiftTransactionRequest::class.java)

                val transaction = transactionOperations.createGiftTransaction(
                        createTransactionRequest.fundId,
                        userId,
                        createTransactionRequest.receiverId,
                        createTransactionRequest.description,
                        createTransactionRequest.price
                )

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


