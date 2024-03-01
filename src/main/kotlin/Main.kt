import java.lang.reflect.Method

open class TestCase(private val testMethodName: String) {
    fun run() {
        val method: Method = WasRun::class.java.getDeclaredMethod(testMethodName)
        method.invoke(this)
    }
}

class WasRun(testMethodName: String): TestCase(testMethodName) {
    var wasRun: Boolean = false

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
