package io.specmatic.inventory

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledIf
import org.springframework.boot.test.context.SpringBootTest
import org.testcontainers.containers.BindMode
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@EnabledIf(value = "isNonCIOrLinux", disabledReason = "Run only on Linux in CI; all platforms allowed locally")
class ContractTestsUsingTestContainer {
    companion object {
        @JvmStatic
        fun isNonCIOrLinux(): Boolean =
            System.getenv("CI") != "true" || System.getProperty("os.name").lowercase().contains("linux")

        private val testContainer: GenericContainer<*> =
            GenericContainer("specmatic/specmatic:latest")
                .withCommand(
                    "test"
                )
                .withFileSystemBind(
                    "./specmatic.yaml",
                    "/usr/src/app/specmatic.yaml",
                    BindMode.READ_ONLY,
                ).withFileSystemBind(
                    "./build/reports/specmatic",
                    "/usr/src/app/build/reports/specmatic",
                    BindMode.READ_WRITE,
                )
                .waitingFor(Wait.forLogMessage(".*Tests run:.*", 1))
                .withNetworkMode("host")
                .withLogConsumer { print(it.utf8String) }
    }

    @Test
    fun specmaticContractTest() {
        testContainer.start()
        val hasSucceeded = testContainer.logs.contains("Failures: 0")
        Assertions.assertThat(hasSucceeded).isTrue()
    }
}
