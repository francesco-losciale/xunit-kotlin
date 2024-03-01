import java.lang.reflect.Method

open class TestCase(private val testMethodName: String) {
    fun run() {
        val method: Method = this::class.java.getDeclaredMethod(this.testMethodName)
        method.invoke(this)
    }
}

class WasRun(testMethodName: String): TestCase(testMethodName) {
    var wasRun: Boolean = false

    fun testMethod() {
        wasRun = true
    }
}

class TestCaseTest(testMethodName: String) : TestCase(testMethodName) {
    fun testRunning() {
        val test = WasRun("testMethod")
        assert(!test.wasRun)
        test.run()
        assert(test.wasRun)
    }
}

fun main() {
    TestCaseTest("testRunning").run()
}
