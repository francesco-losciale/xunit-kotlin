import java.lang.reflect.Method

open class TestCase(private val testMethodName: String) {
    var log: String = ""
    open fun setUp() {
        log += "setUp "
    }
    open fun tearDown() {
        log += "tearDown "
    }
    fun run(): TestResult {
        val testResult = TestResult()
        testResult.testStarted()
        setUp()
        val method: Method = this::class.java.getDeclaredMethod(this.testMethodName)
        method.invoke(this)
        tearDown()
        return testResult
    }
}

class TestResult(private var runCount: Int = 0) {
    fun summary() = "$runCount run, 0 failed"
    fun testStarted() = runCount++
}

class WasRun(testMethodName: String): TestCase(testMethodName) {
    fun testMethod() {
        log += "testMethod "
    }
}

class TestCaseTest(testMethodName: String) : TestCase(testMethodName) {
    fun testTemplateMethod() {
        val test = WasRun("testMethod")
        test.run()
        assert(test.log == "setUp testMethod tearDown ")
    }
    fun testResult() {
        val test = WasRun("testMethod")
        val result = test.run()
        assert(result.summary() == "1 run, 0 failed")
    }
}

fun main() {
    TestCaseTest("testTemplateMethod").run()
    TestCaseTest("testResult").run()
}
