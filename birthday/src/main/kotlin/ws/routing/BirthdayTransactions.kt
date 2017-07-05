package ws.routing

import model.transaction.Transaction
import org.jetbrains.ktor.application.call
import org.jetbrains.ktor.http.HttpStatusCode
import org.jetbrains.ktor.routing.Route
import org.jetbrains.ktor.routing.get
import org.jetbrains.ktor.routing.optionalParam
import org.jetbrains.ktor.routing.post
import org.jetbrains.ktor.routing.route
import org.jetbrains.ktor.sessions.sessionOrNull
import service.BirthdayTransactionOperations
import ws.*
import ws.util.post

fun Route.birthdayTransaction(transactionOperations: BirthdayTransactionOperations) {

    route("user/fund/{fundId}/transaction") {
        authorized {
            post("{transactionId}/confirm") {
                val session = call.sessionOrNull<Session>()!!
                val userId = session.userId
                val transactionId = call.parameters["transactionId"]?.toLong()!!

                transactionOperations.confirmTransaction(transactionId, userId)
                call.response.status(HttpStatusCode.OK)
            }

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

            optionalParam("pageSize") {
                optionalParam("pageNumber") {

                    get("redistribution") {
                        val session = call.sessionOrNull<Session>()!!
                        val userId = session.userId

                        val fundId = call.parameters["fundId"]?.toLong()!!

                        val pageSize = call.parameters["pageSize"]?.toInt() ?: 10
                        val pageNumber = call.parameters["pageNumber"]?.toInt() ?: 1

                        val count = transactionOperations.getUserRedistributionTransactionsCount(fundId, userId)
                        val totalPages = if (count % pageSize == 0) {
                            count / pageSize
                        } else {
                            (count / pageSize) + 1
                        }

                        val transactions = transactionOperations.getUserRedistributionsTransactions(fundId, userId, pageSize, (pageSize * (pageNumber - 1)))
                        call.respond(Page(transactions.map { redistributionTransactionToDto(it, userId) }, pageNumber, pageSize, totalPages))
                    }

                    get("gift") {
                        val session = call.sessionOrNull<Session>()!!
                        val userId = session.userId

                        val fundId = call.parameters["fundId"]?.toLong()!!

                        val pageSize = call.parameters["pageSize"]?.toInt() ?: 10
                        val pageNumber = call.parameters["pageNumber"]?.toInt() ?: 1

                        val count = transactionOperations.getUserRedistributionTransactionsCount(fundId, userId)
                        val totalPages = if (count % pageSize == 0) {
                            count / pageSize
                        } else {
                            (count / pageSize) + 1
                        }

                        val transactions = transactionOperations.getUserGiftTransactions(fundId, userId, pageSize, (pageSize * (pageNumber - 1)))
                        call.respond(Page(transactions.map { giftTransactionToDto(it, userId) }, pageNumber, pageSize, totalPages))
                    }
                }
            }

        }

    }
}


fun redistributionTransactionToDto(transaction: Transaction, userId: Long): RedistributionTransactionDto {
    val otherUserShare = transaction.shares.first { it.user.id != userId }
    val direction = if (otherUserShare.amount > 0) {
        RedistributionTransactionDirection.INCOME
    } else {
        RedistributionTransactionDirection.OUTCOME
    }

    return RedistributionTransactionDto(
            transaction.id,
            moneyToString(Math.abs(otherUserShare.amount)),
            transaction.description,
            transaction.timestamp.toEpochMilli(),
            transaction.status,
            userToDto(otherUserShare.user),
            direction
    )
}

fun giftTransactionToDto(transaction: Transaction, userId: Long): GiftTransactionDto {
    val userShare = transaction.shares.first { it.user.id == userId }
    val buyerShare = transaction.shares.first { it.amount > 0 }

    return GiftTransactionDto(
            transaction.id,
            moneyToString(transaction.amount),
            moneyToString(userShare.amount, addPlus = true),
            transaction.description,
            transaction.timestamp.toEpochMilli(),
            userToDto(buyerShare.user),
            transaction.shares.map { userToDto(it.user) }
    )
}


