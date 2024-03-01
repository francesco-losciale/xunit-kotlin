import java.lang.reflect.Method

open class TestCase(private val testMethodName: String) {
    var log: String = ""
    open fun setUp() {
        log += "setUp "
    }
    fun run() {
        setUp()
        val method: Method = this::class.java.getDeclaredMethod(this.testMethodName)
        method.invoke(this)
    }
}

class WasRun(testMethodName: String): TestCase(testMethodName) {
    fun testMethod() {
        log += "testMethod "
    }
}

class TestCaseTest(testMethodName: String) : TestCase(testMethodName) {
    lateinit var test: WasRun
    override fun setUp() {
        test = WasRun("testMethod")
    }
    fun testRunning() {
        test.run()
        assert(test.log == "setUp testMethod ")
    }
    fun testSetUp() {
        test.run()
        assert(test.log == "setUp testMethod ")
    }
}

fun main() {
    TestCaseTest("testRunning").run()
    TestCaseTest("testSetUp").run()
}
