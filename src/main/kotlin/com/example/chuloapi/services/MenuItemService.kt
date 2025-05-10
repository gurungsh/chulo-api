package com.example.chuloapi.services

import com.example.chuloapi.models.MenuItem
import com.example.chuloapi.repositories.MenuItemRepository
import org.springframework.stereotype.Service

@Service
class MenuItemService(private val menuItemRepository: MenuItemRepository) {
    fun getAllMenuItems(): List<MenuItem> = menuItemRepository.findAll()

    fun getMenuItemById(id: Long): MenuItem? = menuItemRepository.findById(id).orElse(null)

    fun createMenuItem(menuItem: MenuItem): MenuItem = menuItemRepository.save(menuItem)

    fun updateMenuItem(id: Long, menuItem: MenuItem): MenuItem? {
        return if (menuItemRepository.existsById(id)) {
            menuItemRepository.save(menuItem.copy(id = id))
        } else {
            null
        }
    }

    fun deleteMenuItem(id: Long): Boolean {
        return if (menuItemRepository.existsById(id)) {
            menuItemRepository.deleteById(id)
            true
        } else {
            false
        }
    }
}