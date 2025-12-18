package io.specmatic.inventory

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HealthEndpointTest(
    @Autowired private val restTemplate: TestRestTemplate,
) {
    @Test
    fun `GET - health returns UP`() {
        val response = restTemplate.getForEntity("/health", Map::class.java)
        val body = response.body

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.headers.contentType?.includes(MediaType.APPLICATION_JSON)).isTrue()
        assertThat(body).isNotNull
        assertThat(body!!["status"]).isEqualTo("UP")
    }
}
