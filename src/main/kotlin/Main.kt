import java.lang.reflect.Method

class WasRun(private val testMethodName: String) {
    var wasRun: Boolean = false
    fun run() {
        val method: Method = WasRun::class.java.getDeclaredMethod(testMethodName)
        method.invoke(this)
    }
    fun testMethod() {
        wasRun = true
    }
}

fun main() {
    val test = WasRun("testMethod")
    println(test.wasRun)
    test.run()
    println(test.wasRun)
}
