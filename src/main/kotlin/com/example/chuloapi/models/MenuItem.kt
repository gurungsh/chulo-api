package com.example.chuloapi.models

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
data class MenuItem(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    var name: String,
    val price: Double,
    val isAvailable: Boolean,
    val tags: List<String> // e.g., ["vegetarian", "gluten free"]
)