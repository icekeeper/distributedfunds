package domain


class Fund(val name: String, val description: String, val supervisor: User, val users: List<User>) {

    constructor(name: String, description: String, supervisor: User) : this(name, description, supervisor, emptyList())

}