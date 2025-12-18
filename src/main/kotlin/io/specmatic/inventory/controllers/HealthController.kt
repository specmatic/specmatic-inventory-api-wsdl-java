package io.specmatic.inventory.controllers

import com.example.inventory.GetInventoryRequest
import org.springframework.boot.actuate.health.HealthEndpoint
import org.springframework.boot.actuate.health.Status
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthController(private val healthEndpoint: HealthEndpoint,
                       private val inventoryEndpoint: InventoryEndpoint) {
    @GetMapping("/health")
    fun health(): ResponseEntity<Map<String, String>> {
        val health = healthEndpoint.health()
        var status = health.status

        // Call getInventory for product id 99999 and mark DOWN on failure
        try {
            val request = GetInventoryRequest()
            request.productid = 99999
            inventoryEndpoint.getInventory(request)
        } catch (_: Exception) {

        }

        val httpStatus = if (status == Status.UP) HttpStatus.OK else HttpStatus.SERVICE_UNAVAILABLE

        return ResponseEntity.status(httpStatus).body(mapOf("status" to status.code))
    }
}
