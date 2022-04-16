package ru.tech.kastybiy.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.tech.kastybiy.core.Action
import ru.tech.kastybiy.domain.model.Cuisine
import ru.tech.kastybiy.domain.model.Product

interface KastybiyRepository {

    fun getCuisineList(): Flow<Action<List<Cuisine>>>

    fun getDishById(id: Int): Flow<Action<Cuisine>>

    suspend fun updateFav(id: Int, fav: Boolean)

    fun getFavouriteRecipes(): Flow<Action<List<Cuisine>>>

    suspend fun checkFavoriteId(id: Int): Boolean

    fun getFridgeList(): Flow<Action<List<Product>>>

    suspend fun updateProduct(id: Int, inFridge: Boolean)

    fun getAllProducts(): Flow<Action<List<Product>>>

}