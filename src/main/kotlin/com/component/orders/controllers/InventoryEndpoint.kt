package com.component.orders.controllers

import com.example.bff.AddInventoryRequest
import com.example.bff.AddInventoryResponse
import com.example.bff.GetInventoryRequest
import com.example.bff.GetInventoryResponse
import com.example.bff.ReduceInventoryRequest
import com.example.bff.ReduceInventoryResponse
import org.springframework.ws.server.endpoint.annotation.Endpoint
import org.springframework.ws.server.endpoint.annotation.PayloadRoot
import org.springframework.ws.server.endpoint.annotation.RequestPayload
import org.springframework.ws.server.endpoint.annotation.ResponsePayload
import java.util.concurrent.ConcurrentHashMap

@Endpoint
class InventoryEndpoint {

    companion object {
        const val NAMESPACE_URI: String = "http://www.example.com/bff"
    }

    private val inventoryStore: ConcurrentHashMap<Int, Int> = ConcurrentHashMap()

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "AddInventoryRequest")
    @ResponsePayload
    fun addInventory(@RequestPayload request: AddInventoryRequest): AddInventoryResponse {
        inventoryStore.merge(request.productid, request.inventory) { existing, incoming ->
            existing + incoming
        }

        return AddInventoryResponse().apply {
            message = "success"
        }
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetInventoryRequest")
    @ResponsePayload
    fun getInventory(@RequestPayload request: GetInventoryRequest): GetInventoryResponse {
        val currentInventory = inventoryStore[request.productid] ?: 0

        return GetInventoryResponse().apply {
            productid = request.productid
            inventory = currentInventory
        }
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "ReduceInventoryRequest")
    @ResponsePayload
    fun reduceInventory(@RequestPayload request: ReduceInventoryRequest): ReduceInventoryResponse {
        val requestedReduction = request.inventory
        val productId = request.productid

        var reductionSucceeded = false
        val availableInventory = inventoryStore.compute(productId) { _, existingInventory ->
            val currentInventory = existingInventory ?: 0
            if (currentInventory >= requestedReduction) {
                reductionSucceeded = true
                currentInventory - requestedReduction
            } else {
                currentInventory
            }
        } ?: 0

        val message = if (reductionSucceeded) {
            "success"
        } else {
            "Tried to reduce by $requestedReduction but available inventory was $availableInventory"
        }

        return ReduceInventoryResponse().apply {
            this.message = message
        }
    }
}
