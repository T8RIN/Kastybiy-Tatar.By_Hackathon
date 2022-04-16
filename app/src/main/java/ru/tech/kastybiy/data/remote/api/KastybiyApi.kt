package ru.tech.kastybiy.data.remote.api

import retrofit2.http.GET
import retrofit2.http.Path
import ru.tech.kastybiy.data.remote.dto.CuisineDto
import ru.tech.kastybiy.data.remote.dto.ProductDto

interface KastybiyApi {

    @GET("/api/get/cuisine")
    suspend fun getCuisine(): List<CuisineDto>

    @GET("/api/get/cuisine/{id}")
    suspend fun getDishById(@Path("id") id: Int): CuisineDto

    @GET("/api/get/products")
    suspend fun getProducts(): List<ProductDto>

    @GET("/api/get/products/{id}")
    suspend fun getProductById(@Path("id") id: Int): ProductDto

}