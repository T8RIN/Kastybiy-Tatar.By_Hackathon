package ru.tech.kastybiy.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.tech.kastybiy.data.local.dao.FavRecipeDao
import ru.tech.kastybiy.data.local.dao.FridgeDao
import ru.tech.kastybiy.data.local.entity.FavRecipeEntity
import ru.tech.kastybiy.data.local.entity.ProductEntity

@Database(
    entities = [FavRecipeEntity::class, ProductEntity::class],
    exportSchema = false,
    version = 1
)
abstract class KastybiyDatabase : RoomDatabase() {
    abstract val favRecipeDao: FavRecipeDao
    abstract val fridgeDao: FridgeDao
}