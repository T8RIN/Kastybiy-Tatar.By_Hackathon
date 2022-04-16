package ru.tech.kastybiy.domain.model

data class Product(
    val id: Int,
    val name: String,
    val inFridge: Boolean = false
)
