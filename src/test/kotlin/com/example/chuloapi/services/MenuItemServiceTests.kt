package com.example.chuloapi.services

import com.example.chuloapi.models.MenuItem
import com.example.chuloapi.repositories.MenuItemRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.*

@ExtendWith(MockitoExtension::class)
class MenuItemServiceTests {
    @Mock
    private lateinit var menuItemRepository: MenuItemRepository

    @InjectMocks
    private lateinit var menuItemService: MenuItemService

    private lateinit var testMenuItem: MenuItem

    @BeforeEach
    fun setUp() {
        testMenuItem = MenuItem(
            id = 1L,
            name = "Masu MenuItem",
            price = 10.0,
            isAvailable = true,
            tags = listOf("Gluten Free")
        )
    }

    @Test
    fun `getAllMenuItems should return all menu items`() {
        // Given
        val menuItems = listOf(testMenuItem, testMenuItem.copy(id = 2L))
        whenever(menuItemRepository.findAll()).thenReturn(menuItems)

        // When
        val result = menuItemService.getAllMenuItems()

        // Then
        assertEquals(2, result.size)
        verify(menuItemRepository, times(1)).findAll()
    }

    @Test
    fun `getMenuItemById should return menu item when exists`() {
        // Given
        whenever(menuItemRepository.findById(1L)).thenReturn(Optional.of(testMenuItem))

        // When
        val result = menuItemService.getMenuItemById(1L)

        // Then
        assertNotNull(result)
        assertEquals("Masu MenuItem", result?.name)
        verify(menuItemRepository, times(1)).findById(1L)
    }

    @Test
    fun `getMenuItemById should return null when menu item does not exist`() {
        // Given
        whenever(menuItemRepository.findById(10L)).thenReturn(Optional.empty())

        // When
        val result = menuItemService.getMenuItemById(10L)

        // Then
        assertNull(result)
        verify(menuItemRepository, times(1)).findById(10L)
    }

    @Test
    fun `createMenuItem should save and return menu item`() {
        // Given
        val newMenuItem = testMenuItem.copy(id = null)
        whenever(menuItemRepository.save(any<MenuItem>())).thenReturn(testMenuItem)

        // When
        val result = menuItemService.createMenuItem(newMenuItem)

        // Then
        assertNotNull(result.id)
        assertEquals("Masu MenuItem", result.name)

        val menuItemCaptor = argumentCaptor<MenuItem>()
        verify(menuItemRepository).save(menuItemCaptor.capture())
        assertEquals(null, menuItemCaptor.firstValue.id)
    }

    @Test
    fun `updateMenuItem should update existing menu item`() {
        // Given
        val updatedMenuItem = testMenuItem.copy(name = "Updated MenuItem")
        whenever(menuItemRepository.existsById(1L)).thenReturn(true)
        whenever(menuItemRepository.save(any<MenuItem>())).thenReturn(updatedMenuItem)

        // When
        val result = menuItemService.updateMenuItem(1L, updatedMenuItem)

        // Then
        assertNotNull(result)
        assertEquals("Updated MenuItem", result?.name)
        verify(menuItemRepository).existsById(1L)
        verify(menuItemRepository).save(updatedMenuItem.copy(id = 1L))
    }

    @Test
    fun `updateMenuItem should return null for non-existing menu item`() {
        // Given
        whenever(menuItemRepository.existsById(99L)).thenReturn(false)

        // When
        val result = menuItemService.updateMenuItem(99L, testMenuItem)

        // Then
        assertNull(result)
        verify(menuItemRepository).existsById(99L)
        verify(menuItemRepository, never()).save(any())
    }

    @Test
    fun `deleteMenuItem should return true when menu item exists`() {
        // Given
        whenever(menuItemRepository.existsById(1L)).thenReturn(true)

        // When
        val result = menuItemService.deleteMenuItem(1L)

        // Then
        assertTrue(result)
        verify(menuItemRepository).existsById(1L)
        verify(menuItemRepository).deleteById(1L)
    }

    @Test
    fun `deleteMenuItem should return false when menu item does not exist`() {
        // Given
        whenever(menuItemRepository.existsById(99L)).thenReturn(false)

        // When
        val result = menuItemService.deleteMenuItem(99L)

        // Then
        assertFalse(result)
        verify(menuItemRepository).existsById(99L)
        verify(menuItemRepository, never()).deleteById(any())
    }
}