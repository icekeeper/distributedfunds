package ws.routing

import model.User
import org.jetbrains.ktor.application.call
import org.jetbrains.ktor.routing.Route
import org.jetbrains.ktor.routing.get
import org.jetbrains.ktor.routing.route
import org.jetbrains.ktor.sessions.sessionOrNull
import service.UserOperations
import ws.CreateUserRequestDto
import ws.DtoCollection
import ws.UserDto
import ws.util.post

fun Route.user(userOperations: UserOperations) {

    route("user") {
        post<CreateUserRequestDto> { (login, name) ->
            val user = userOperations.registerUser(login, name)
            call.respond(userToDto(user))
        }

        authorized {
            get {
                val session = call.sessionOrNull<Session>()!!
                val user = userOperations.getUser(session.userId)
                call.respond(userToDto(user))
            }
        }

        get("{id}") {
            val id = call.parameters["id"]?.toLong()
            val user = userOperations.getUser(id!!)
            call.respond(userToDto(user))
        }

        get("all") {
            val users = userOperations.getAllUsers()
            call.respond(DtoCollection(users.map { userToDto(it) }))
        }

    }


}

fun userToDto(user: User): UserDto {
    return UserDto(user.id, user.login, user.name)
}


