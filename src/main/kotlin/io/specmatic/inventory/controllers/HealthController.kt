package io.specmatic.inventory.controllers

import org.springframework.boot.actuate.health.HealthEndpoint
import org.springframework.boot.actuate.health.Status
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthController(private val healthEndpoint: HealthEndpoint) {
    @GetMapping("/health")
    fun health(): ResponseEntity<Map<String, String>> {
        val health = healthEndpoint.health()
        val status = health.status
        val httpStatus = if (status == Status.UP) HttpStatus.OK else HttpStatus.SERVICE_UNAVAILABLE

        return ResponseEntity.status(httpStatus).body(mapOf("status" to status.code))
    }
}
