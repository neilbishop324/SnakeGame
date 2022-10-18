class SystemProp {
    val os: String = System.getProperty("os.name")
    fun cls() {
        if (os.contains("Windows"))
            ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor()
        else
            Runtime.getRuntime().exec("clear")
    }
}