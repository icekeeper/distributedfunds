package service.impl

class StorageBackedUserOperations(val userRepository: storage.UserRepository) : service.UserOperations {

    override fun registerUser(login: String, name: String): model.User {
        return userRepository.create(login, name)
    }

    override fun getUser(id: Long): model.User {
        return userRepository.get(id)
    }
}


