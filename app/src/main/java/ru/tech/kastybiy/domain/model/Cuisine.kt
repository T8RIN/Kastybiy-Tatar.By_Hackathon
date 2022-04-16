package ru.tech.kastybiy.domain.model

data class Cuisine(
    val id: Int,
    val products: List<String>,
    val productIds: List<Int>,
    val calories: Double,
    val carboh: Double,
    val category: String,
    val cookSteps: List<String>,
    val cookTime: Int,
    val fats: Double,
    val proteins: Double,
    val source: String,
    val title: String,
    val iconUrl: String
)