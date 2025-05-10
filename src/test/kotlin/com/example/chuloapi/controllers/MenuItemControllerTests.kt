package com.example.chuloapi.controllers

import com.example.chuloapi.models.MenuItem
import com.example.chuloapi.services.MenuItemService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(MenuItemController::class)
class MenuItemControllerTests {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockitoBean
    private lateinit var menuItemService: MenuItemService

    private val testMenuItem = MenuItem(
        id = 1L,
        name = "Masu Achar",
        price = 10.0,
        isAvailable = true,
        tags = listOf("Gluten Free")
    )

    @Test
    fun `getAllMenuItems should return all menu items`() {
        // Given
        val menuItems = listOf(testMenuItem, testMenuItem.copy(id = 2L))
        given(menuItemService.getAllMenuItems()).willReturn(menuItems)

        // When/Then
        mockMvc.perform(get("/api/menu-items"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.size()").value(2))
            .andExpect(jsonPath("$[0].name").value("Masu Achar"))
    }

    @Test
    fun `getMenuItemById should return menu item when exists`() {
        // Given
        given(menuItemService.getMenuItemById(1L)).willReturn(testMenuItem)

        // When/Then
        mockMvc.perform(get("/api/menu-items/1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("Masu Achar"))
    }

    @Test
    fun `getMenuItemById should return 404 when menu item does not exist`() {
        // Given
        given(menuItemService.getMenuItemById(99L)).willReturn(null)

        // When/Then
        mockMvc.perform(get("/api/menu-items/99"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `createMenuItem should return created menu item`() {
        // Given
        val newMenuItem = testMenuItem.copy(id = null)
        given(menuItemService.createMenuItem(any<MenuItem>())).willReturn(testMenuItem)

        // When/Then
        mockMvc.perform(
            post("/api/menu-items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    objectMapper.writeValueAsString(newMenuItem)
                )
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").value(1L))
    }

    @Test
    fun `updateMenuItem should return updated menu item`() {
        // Given
        val updatedMenuItem = testMenuItem.copy(name = "Updated Name")
        given(menuItemService.updateMenuItem(1L, updatedMenuItem)).willReturn(updatedMenuItem)

        // When/Then
        mockMvc.perform(
            put("/api/menu-items/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedMenuItem))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("Updated Name"))
    }

    @Test
    fun `updateMenuItem should return 404 when menu item does not exists`() {
        // Given
        given(menuItemService.updateMenuItem(eq(99L), any())).willReturn(null)

        // When/Then
        mockMvc.perform(
            put("/api/menuItems/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testMenuItem))
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun `deleteMenuItem should return 204 when menu item exists`() {
        // Given
        given(menuItemService.deleteMenuItem(1L)).willReturn(true)

        // When/Then
        mockMvc.perform(delete("/api/menu-items/1"))
            .andExpect(status().isNoContent)
    }

    @Test
    fun `deleteMenuItem should return 404 when menu item not exists`() {
        // Given
        given(menuItemService.deleteMenuItem(99L)).willReturn(false)

        // When/Then
        mockMvc.perform(delete("/api/menu-items/99"))
            .andExpect(status().isNotFound)
    }
}