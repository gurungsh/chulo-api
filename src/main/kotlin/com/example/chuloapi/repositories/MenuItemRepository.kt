package com.example.chuloapi.repositories

import com.example.chuloapi.models.MenuItem
import org.springframework.data.jpa.repository.JpaRepository

interface MenuItemRepository: JpaRepository<MenuItem, Long>