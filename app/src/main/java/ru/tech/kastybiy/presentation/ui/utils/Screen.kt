package ru.tech.kastybiy.presentation.ui.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DinnerDining
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.DinnerDining
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.PhoneAndroid
import androidx.compose.material.icons.rounded.PhoneAndroid
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val title: String = "",
    val shortTitle: String = "",
    val baseIcon: ImageVector = Icons.Outlined.PhoneAndroid,
    val selectedIcon: ImageVector = Icons.Rounded.PhoneAndroid
) {
    object Cuisine : Screen(
        route = "cuisine",
        title = "Халык ашлары",
        shortTitle = "Ашлары",
        baseIcon = Icons.Outlined.DinnerDining,
        selectedIcon = Icons.Filled.DinnerDining
    )

    object Favourites : Screen(
        route = "favourites",
        title = "Яраткан ашлар",
        shortTitle = "Яраткан",
        baseIcon = Icons.Outlined.FavoriteBorder,
        selectedIcon = Icons.Filled.Favorite
    )

    object Fridge : Screen(
        route = "fridge",
        title = "Суыткыч",
        shortTitle = "Суыткыч"
    )
}
