package ws

import com.google.gson.Gson
import model.User
import org.jetbrains.ktor.application.call
import org.jetbrains.ktor.application.receive
import org.jetbrains.ktor.routing.Route
import org.jetbrains.ktor.routing.get
import org.jetbrains.ktor.routing.post
import org.jetbrains.ktor.routing.route
import service.UserOperations

fun Route.user(gson: Gson, userOperations: UserOperations) {

    route("user") {
        post {
            val body = call.request.receive<String>()
            val createUserRequest = gson.fromJson(body, CreateUserRequestDto::class.java)

            val user = userOperations.registerUser(createUserRequest.login, createUserRequest.name)

            call.respond(userToDto(user))
        }

        get("{id}") {
            val id = call.parameters["id"]?.toLong()
            val user = userOperations.getUser(id!!)
            call.respond(userToDto(user))
        }

    }


}

fun userToDto(user: User): UserDto {
    return UserDto(user.id, user.login, user.name)
}


