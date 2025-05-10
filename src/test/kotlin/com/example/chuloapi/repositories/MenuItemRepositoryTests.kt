package com.example.chuloapi.repositories

import com.example.chuloapi.models.MenuItem
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.ActiveProfiles
import kotlin.test.Test

@DataJpaTest
@ActiveProfiles("test")
class MenuItemRepositoryTests {
    @Autowired
    private lateinit var entityManager: TestEntityManager

    @Autowired
    private lateinit var menuItemRepository: MenuItemRepository

    @Test
    fun `findById should return menu item when exists`() {
        // Given
        val savedMenuItem = entityManager.persistFlushFind(
            MenuItem(
                name = "Test Achar",
                price = 10.0,
                isAvailable = true,
                tags = listOf("Gluten Free")
            )
        )

        // When
        val foundMenuItem = menuItemRepository.findById(savedMenuItem.id!!)

        // Then
        assertTrue(foundMenuItem.isPresent)
        assertEquals("Test Achar", foundMenuItem.get().name)
    }

    @Test
    fun `save should persist menu item`() {
        // Given
        val newMenuItem = MenuItem(name = "New Menu Item", price = 4.5, isAvailable = true, tags = listOf())

        // When
        val savedMenuItem = menuItemRepository.save(newMenuItem)

        // Then
        assertNotNull(savedMenuItem.id)
        val foundMenuItem = entityManager.find(MenuItem::class.java, savedMenuItem.id)
        assertEquals("New Menu Item", foundMenuItem.name)
    }

    @Test
    fun `delete should remove menu item`() {
        // Given
        val savedMenuItem = entityManager.persistFlushFind(
            MenuItem(name = "New Menu Item", price = 4.5, isAvailable = true, tags = listOf())
        )

        // When
        menuItemRepository.deleteById(savedMenuItem.id!!)

        // Then
        assertNull(entityManager.find(MenuItem::class.java, savedMenuItem.id))
    }
}