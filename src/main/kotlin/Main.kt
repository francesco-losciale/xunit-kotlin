import java.lang.reflect.Method

open class TestCase(private val testMethodName: String) {
    var wasSetUp: Boolean = false
    var wasRun: Boolean = false
    open fun setUp() {
        wasSetUp = true
        wasRun = false
    }
    fun run() {
        setUp()
        val method: Method = this::class.java.getDeclaredMethod(this.testMethodName)
        method.invoke(this)
    }
}

class WasRun(testMethodName: String): TestCase(testMethodName) {
    fun testMethod() {
        wasRun = true
    }
}

class TestCaseTest(testMethodName: String) : TestCase(testMethodName) {
    lateinit var test: WasRun
    override fun setUp() {
        test = WasRun("testMethod")
    }
    fun testRunning() {
        test.run()
        assert(test.wasRun)
    }
    fun testSetUp() {
        test.run()
        assert(test.wasSetUp)
    }
}

fun main() {
    TestCaseTest("testRunning").run()
    TestCaseTest("testSetUp").run()
}
