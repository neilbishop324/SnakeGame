package sealed

sealed class Dest() {
    object W: Dest()
    object A: Dest()
    object S: Dest()
    object D: Dest()
}