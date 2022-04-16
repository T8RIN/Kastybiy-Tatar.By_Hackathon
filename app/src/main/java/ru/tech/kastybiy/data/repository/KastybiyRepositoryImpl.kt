package ru.tech.kastybiy.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.tech.kastybiy.core.Action
import ru.tech.kastybiy.data.local.dao.FavRecipeDao
import ru.tech.kastybiy.data.local.dao.FridgeDao
import ru.tech.kastybiy.data.local.entity.FavRecipeEntity
import ru.tech.kastybiy.data.local.entity.ProductEntity
import ru.tech.kastybiy.data.remote.api.KastybiyApi
import ru.tech.kastybiy.data.remote.dto.toCuisine
import ru.tech.kastybiy.data.remote.dto.toProduct
import ru.tech.kastybiy.domain.model.Cuisine
import ru.tech.kastybiy.domain.model.Product
import ru.tech.kastybiy.domain.repository.KastybiyRepository
import javax.inject.Inject

class KastybiyRepositoryImpl @Inject constructor(
    private val api: KastybiyApi,
    private val favRecipeDao: FavRecipeDao,
    private val fridgeDao: FridgeDao
) : KastybiyRepository {

    override fun getCuisineList(): Flow<Action<List<Cuisine>>> = flow {
        emit(Action.Loading())
        try {
            val remoteCoins = api.getCuisine()
            emit(Action.Success(remoteCoins.map { it.toCuisine() }))
        } catch (e: Exception) {
            emit(Action.Empty(e.localizedMessage))
        }
    }

    override fun getDishById(id: Int): Flow<Action<Cuisine>> = flow {
        emit(Action.Loading())
        try {
            val remoteData = api.getDishById(id)
            emit(Action.Success(remoteData.toCuisine()))
        } catch (e: Exception) {
            emit(Action.Empty(e.localizedMessage))
        }
    }

    override suspend fun updateFav(id: Int, fav: Boolean) {
        if (!fav) {
            favRecipeDao.insertRecipe(FavRecipeEntity(id))
        } else {
            favRecipeDao.deleteRecipe(id)
        }
    }

    override fun getFavouriteRecipes(): Flow<Action<List<Cuisine>>> = flow {

        favRecipeDao.getFavRecipes().collect { list ->
            emit(Action.Loading())

            val ids = list.map { it.id }
            val data: List<Cuisine> = ids.mapNotNull { id ->
                try {
                    api.getDishById(id).toCuisine()
                } catch (e: Exception) {
                    null
                }
            }
            if (data.isNotEmpty()) emit(Action.Success(data))
            else emit(Action.Empty("Сезнең сайланма рецептлар буш"))
        }

    }

    override suspend fun checkFavoriteId(id: Int): Boolean {
        return favRecipeDao.getById(id)?.let { true } ?: false
    }

    override fun getFridgeList(): Flow<Action<List<Product>>> = flow {
        fridgeDao.getFridgeList().collect { list ->
            emit(Action.Loading())

            val ids = list.map { it.id }
            val data: List<Product> = ids.mapNotNull { id ->
                try {
                    api.getProductById(id).toProduct()
                } catch (e: Exception) {
                    null
                }
            }
            if (data.isNotEmpty()) emit(Action.Success(data))
            else emit(Action.Empty("Сезнең суыткыч буш, башланырга өчен ашамлыкларны кушыгыз"))
        }
    }

    override suspend fun updateProduct(id: Int, inFridge: Boolean) {
        if (!inFridge) {
            fridgeDao.insertProduct(ProductEntity(id))
        } else {
            fridgeDao.removeProduct(id)
        }
    }

    override fun getAllProducts(): Flow<Action<List<Product>>> = flow {
        emit(Action.Loading())
        try {
            val remoteCoins = api.getProducts()
            emit(Action.Success(remoteCoins.map { it.toProduct() }))
        } catch (e: Exception) {
            emit(Action.Empty(e.localizedMessage))
        }
    }


}