package ru.tech.kastybiy.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavRecipeEntity(
    @PrimaryKey val id: Int
)
