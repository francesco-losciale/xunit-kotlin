import java.lang.reflect.Method

open class TestCase(private val testMethodName: String) {
    var log: String = ""
    open fun setUp() {
        log += "setUp "
    }
    open fun tearDown() {
        log += "tearDown "
    }
    fun run() {
        setUp()
        val method: Method = this::class.java.getDeclaredMethod(this.testMethodName)
        method.invoke(this)
        tearDown()
    }
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
}

fun main() {
    TestCaseTest("testTemplateMethod").run()
}
