import org.jetbrains.ktor.application.ApplicationCallPipeline
import org.jetbrains.ktor.application.install
import org.jetbrains.ktor.application.log
import org.jetbrains.ktor.host.embeddedServer
import org.jetbrains.ktor.http.HttpStatusCode
import org.jetbrains.ktor.jetty.Jetty
import org.jetbrains.ktor.logging.CallLogging
import org.jetbrains.ktor.routing.route
import org.jetbrains.ktor.routing.routing
import org.jetbrains.ktor.sessions.SessionCookieTransformerMessageAuthentication
import org.jetbrains.ktor.sessions.SessionCookiesSettings
import org.jetbrains.ktor.sessions.withCookieByValue
import org.jetbrains.ktor.sessions.withSessions
import org.jetbrains.ktor.util.hex
import service.error.BirthdayException
import service.error.OperationsException
import service.impl.CoreBasedBirthdayTransactionOperations
import service.impl.StorageBackedFundOperations
import service.impl.StorageBackedUserOperations
import storage.dao.initDao
import storage.exposed.ExposedFundRepository
import storage.exposed.ExposedTransactionRepository
import storage.exposed.ExposedUserRepository
import ws.ErrorResponse
import ws.routing.*
import ws.util.GsonDtoProcessor

fun main(args: Array<String>) {
    initDao()

    val userRepository = ExposedUserRepository()
    val fundRepository = ExposedFundRepository()
    val transactionRepository = ExposedTransactionRepository()

    val userOperations = StorageBackedUserOperations(userRepository)
    val fundOperations = StorageBackedFundOperations(fundRepository, userRepository, transactionRepository)

    val birthdayTransactionOperations = CoreBasedBirthdayTransactionOperations(userRepository, fundRepository, transactionRepository)

    createTestUsers(userOperations)
    createTestFunds(fundOperations)
    createTestGiftTransactions(birthdayTransactionOperations)
    createTestRedistributionTransactions(birthdayTransactionOperations)

    val server = embeddedServer(Jetty, 8080) {
        routing {
            intercept(ApplicationCallPipeline.ApplicationPhase.Infrastructure) { call ->
                try {
                    proceed()
                } catch (exception: OperationsException) {
                    call.respond(ErrorResponse(exception.errorCode.toString(), exception.httpStatusCode, exception.parameters.toList()))
                    finish()
                } catch (exception: BirthdayException) {
                    call.respond(ErrorResponse(exception.errorCode.toString(), exception.httpStatusCode, exception.parameters.toList()))
                    finish()
                } catch (exception: Exception) {
                    log.error(exception)
                    call.response.status(HttpStatusCode.InternalServerError)
                    finish()
                }
            }

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
                birthdayFunds(fundOperations)
                birthdayTransaction(birthdayTransactionOperations)
            }
        }


    }
    server.start(wait = true)
}

//Temporary db filling code for development

private fun createTestRedistributionTransactions(birthdayTransactionOperations: CoreBasedBirthdayTransactionOperations) {
    birthdayTransactionOperations.createRedistributionTransaction(1, 1, 2, "from icekeeper to terry", 100)
    birthdayTransactionOperations.createRedistributionTransaction(1, 2, 1, "from terry to icekeeper", 200)
    birthdayTransactionOperations.createRedistributionTransaction(1, 3, 4, "from ortemij to 40in", 300)
    birthdayTransactionOperations.createRedistributionTransaction(1, 4, 2, "from 40in to ortemij", 400)
}

private fun createTestGiftTransactions(birthdayTransactionOperations: CoreBasedBirthdayTransactionOperations) {
    birthdayTransactionOperations.createGiftTransaction(1, 1, 2, "Gift to terry from icekeeper #1", 100)
    birthdayTransactionOperations.createGiftTransaction(1, 3, 2, "Gift to terry from ortemij #2", 143)
    birthdayTransactionOperations.createGiftTransaction(1, 2, 3, "Gift to ortemij from terry #1", 2372)
    birthdayTransactionOperations.createGiftTransaction(1, 5, 3, "Gift to ortemij from hexode #2", 2312)
    birthdayTransactionOperations.createGiftTransaction(1, 1, 4, "Gift to 40in from icekeeper #1", 2555)
    birthdayTransactionOperations.createGiftTransaction(1, 5, 1, "Gift to icekeeper from hexode #1", 3112)
    birthdayTransactionOperations.createGiftTransaction(1, 2, 1, "Gift to icekeeper from terry #2", 5324)
}

private fun createTestFunds(fundOperations: StorageBackedFundOperations) {
    fundOperations.createFund("The Great Test Fund 1", "Fund #1 for testing", 1)
    fundOperations.addUsers(1, listOf(1, 2, 3, 4, 5))
    fundOperations.createFund("The Even Greater Test Fund 2", "Fund #2 for testing", 2)
    fundOperations.addUsers(2, listOf(1, 2, 3, 4, 5))
}

private fun createTestUsers(userOperations: StorageBackedUserOperations) {
    userOperations.registerUser("icekeeper", "Alexey")
    userOperations.registerUser("terry", "Danila")
    userOperations.registerUser("ortemij", "Artem")
    userOperations.registerUser("40in", "Eugine")
    userOperations.registerUser("hexode", "Alexander")
}

