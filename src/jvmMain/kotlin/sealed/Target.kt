package sealed

sealed class Target() {
    object WALL : Target()
    object APPLE : Target()
    object AIR : Target()
    object BODY : Target()
}
