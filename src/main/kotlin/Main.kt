
class WasRun(name: String) {
    var wasRun: Boolean = false
    fun run() {
        testMethod()
    }
    private fun testMethod() {
        wasRun = true
    }
}

fun main() {
    val test = WasRun("testMethod")
    println(test.wasRun)
    test.run()
    println(test.wasRun)
}
