package ru.tech.kastybiy.data.remote.dto

import ru.tech.kastybiy.domain.model.Product

data class ProductDto(
    val id: Int,
    val name: String
)

fun ProductDto.toProduct() = Product(id, name)