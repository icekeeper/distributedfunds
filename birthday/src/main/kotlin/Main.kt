package ws

import org.jetbrains.ktor.application.install
import org.jetbrains.ktor.host.embeddedServer
import org.jetbrains.ktor.jetty.Jetty
import org.jetbrains.ktor.logging.CallLogging
import org.jetbrains.ktor.routing.route
import org.jetbrains.ktor.routing.routing
import org.jetbrains.ktor.sessions.SessionCookieTransformerMessageAuthentication
import org.jetbrains.ktor.sessions.SessionCookiesSettings
import org.jetbrains.ktor.sessions.withCookieByValue
import org.jetbrains.ktor.sessions.withSessions
import org.jetbrains.ktor.util.hex
import service.impl.CoreBasedBirthdayTransactionOperations
import service.impl.StorageBackedFundOperations
import service.impl.StorageBackedTransactionOperations
import service.impl.StorageBackedUserOperations
import storage.dao.initDao
import storage.exposed.ExposedFundRepository
import storage.exposed.ExposedTransactionRepository
import storage.exposed.ExposedUserRepository
import ws.util.GsonDtoProcessor

fun main(args: Array<String>) {
    initDao()

    val userRepository = ExposedUserRepository()
    val fundRepository = ExposedFundRepository()
    val transactionRepository = ExposedTransactionRepository()

    val userOperations = StorageBackedUserOperations(userRepository)
    val fundOperations = StorageBackedFundOperations(fundRepository, userRepository, transactionRepository)
    val transactionOperations = StorageBackedTransactionOperations(userRepository, fundRepository, transactionRepository)

    val birthdayTransactionOperations = CoreBasedBirthdayTransactionOperations(transactionOperations, fundOperations)

    val server = embeddedServer(Jetty, 8080) {
        routing {
            install(CallLogging)
            install(GsonDtoProcessor)

            val hashKey = hex("4819b57c323945c12a85452f6239")

            withSessions<Session> {
                withCookieByValue {
                    settings = SessionCookiesSettings(transformers = listOf(SessionCookieTransformerMessageAuthentication(hashKey)))
                }
            }

            route("/api/front") {
                login(userOperations)
                user(userOperations)
                fund(fundOperations)
                birthdayTransaction(birthdayTransactionOperations)
            }
        }


    }
    server.start(wait = true)
}

