import java.lang.reflect.Method

open class TestCase(private val testMethodName: String) {
    var log: String = ""
    open fun setUp() {
        log += "setUp "
    }
    open fun tearDown() {
        log += "tearDown "
    }
    fun run(result: TestResult) {
        result.testStarted()
        try {
            setUp()
            invokeTestMethod()
        } catch (e: Exception) {
            result.testFailed()
        }
        tearDown()
    }

    private fun invokeTestMethod() {
        val method: Method = this::class.java.getDeclaredMethod(this.testMethodName)
        method.invoke(this)
    }
}

class BrokenSetUpTestCase : TestCase("") {
    override fun setUp() {
        throw Exception()
    }
}

class TestResult(private var runCount: Int = 0, private var failureCount: Int = 0) {
    fun summary() = "$runCount run, $failureCount failed"
    fun testStarted() = runCount++
    fun testFailed() = failureCount++
}

class WasRun(testMethodName: String): TestCase(testMethodName) {
    fun testMethod() {
        log += "testMethod "
    }
    fun testBrokenMethod() {
        throw Exception()
    }
}

class TestSuite {
    private var tests: List<TestCase> = listOf()
    fun add(test: TestCase) {
        tests = tests + test
    }
    fun run(result: TestResult) {
        for (test in tests) {
            test.run(result)
        }
    }
}

class TestCaseTest(testMethodName: String) : TestCase(testMethodName) {
    fun testTemplateMethod() {
        val test = WasRun("testMethod")
        val result = TestResult()
        test.run(result)
        assert(test.log == "setUp testMethod tearDown ")
    }
    fun testResult() {
        val test = WasRun("testMethod")
        val result = TestResult()
        test.run(result)
        assert(result.summary() == "1 run, 0 failed")
    }
    fun testFailedResultFormatting() {
        val testResult = TestResult()
        testResult.testStarted()
        testResult.testFailed()
        assert(testResult.summary() == "1 run, 1 failed")
    }
    fun testFailedResult() {
        val test = WasRun("testBrokenMethod")
        val result = TestResult()
        test.run(result)
        assert(result.summary() == "1 run, 1 failed")
    }
    fun testSetUpFailedResult() {
        val test = BrokenSetUpTestCase()
        try {
            test.setUp()
        } catch (_: Exception) {}
        val result = TestResult()
        test.run(result)
        assert(result.summary() == "1 run, 1 failed")
    }
    fun testSuite() {
        val suite = TestSuite()
        suite.add(WasRun("testMethod"))
        suite.add(WasRun("testMethod"))
        val result = TestResult()
        suite.run(result)
        assert(result.summary() == "2 run, 0 failed")
    }
}

fun main() {
    val result = TestResult()
    val suite = TestSuite()
    suite.add(TestCaseTest("testTemplateMethod"))
    suite.add(TestCaseTest("testResult"))
    suite.add(TestCaseTest("testFailedResultFormatting"))
    suite.add(TestCaseTest("testFailedResult"))
    suite.add(TestCaseTest("testSetUpFailedResult"))
    suite.add(TestCaseTest("testSuite"))
    suite.run(result)
    println(result.summary())
}
