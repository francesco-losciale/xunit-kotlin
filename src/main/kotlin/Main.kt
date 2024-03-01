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
        } catch (e: Exception) {
            result.testError()
            return
        }
        try {
            invokeTestMethod()
            log += "$testMethodName "
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

class TestResult(private var runCount: Int = 0, private var failureCount: Int = 0, private var errorCount: Int = 0) {
    fun summary() = "$runCount run, $failureCount failed" + if (errorCount > 0) {
        ", $errorCount errors"
    } else {
        ""
    }

    fun testStarted() = runCount++
    fun testFailed() = failureCount++
    fun testError() = errorCount++
}

class WasRun(testMethodName: String) : TestCase(testMethodName) {
    fun testMethod() {
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

    fun testErrorResultFormatting() {
        val testResult = TestResult()
        testResult.testStarted()
        testResult.testError()
        assert(testResult.summary() == "1 run, 0 failed, 1 errors")
    }

    fun testFailedResult() {
        val test = WasRun("testBrokenMethod")
        val result = TestResult()
        test.run(result)
        assert(result.summary() == "1 run, 1 failed")
    }

    fun testErrorResult() {
        val test = BrokenSetUpTestCase()
        try {
            test.setUp()
        } catch (_: Exception) {
        }
        val result = TestResult()
        test.run(result)
        assert(result.summary() == "1 run, 0 failed, 1 errors")
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
    suite.add(TestCaseTest("testErrorResultFormatting"))
    suite.add(TestCaseTest("testFailedResult"))
    suite.add(TestCaseTest("testErrorResult"))
    suite.add(TestCaseTest("testSuite"))
    suite.run(result)
    println(result.summary())
}
