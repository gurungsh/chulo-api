package com.example.chuloapi.controllers

import com.example.chuloapi.models.MenuItem
import com.example.chuloapi.services.MenuItemService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.Instant

@RestController
@RequestMapping("/api/menu-items")
class MenuItemController(private val menuItemService: MenuItemService) {

    @GetMapping
    fun getAllMenuItems(): ResponseEntity<List<MenuItem>> =
        ResponseEntity.ok(menuItemService.getAllMenuItems())

    @GetMapping("/{id}")
    fun getMenuItemById(@PathVariable id: Long): ResponseEntity<MenuItem> =
        menuItemService.getMenuItemById(id)?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()

    @PostMapping
    fun createMenuItem(@RequestBody menuItem: MenuItem): ResponseEntity<MenuItem> =
        ResponseEntity.status(HttpStatus.CREATED).body(menuItemService.createMenuItem(menuItem))

    @PutMapping("/{id}")
    fun updateMenuItem(@PathVariable id: Long, @RequestBody menuItem: MenuItem): ResponseEntity<MenuItem> =
        menuItemService.updateMenuItem(id, menuItem)?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()

    @DeleteMapping("/{id}")
    fun deleteMenuItem(@PathVariable id: Long): ResponseEntity<Void> =
        if (menuItemService.deleteMenuItem(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }

    @GetMapping("/ping")
    fun ping(): Map<String, String> {
        return mapOf(
            "status" to "OK",
            "timestamp" to Instant.now().toString()
        )
    }
}