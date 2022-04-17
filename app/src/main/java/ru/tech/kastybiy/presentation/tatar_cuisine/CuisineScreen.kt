package ru.tech.kastybiy.presentation.tatar_cuisine

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Error
import androidx.compose.material.icons.twotone.FindInPage
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.tech.kastybiy.domain.model.Cuisine
import ru.tech.kastybiy.presentation.app.components.Placeholder
import ru.tech.kastybiy.presentation.tatar_cuisine.components.CuisineItem
import ru.tech.kastybiy.presentation.tatar_cuisine.viewModel.CuisineViewModel
import ru.tech.kastybiy.presentation.ui.utils.showSnackbar

@Composable
fun CuisineScreen(
    snackState: SnackbarHostState,
    idState: MutableState<Int>,
    searchString: MutableState<String>,
    viewModel: CuisineViewModel = hiltViewModel()
) {

    val state = viewModel.cuisineState.value

    Box(modifier = Modifier.fillMaxSize()) {
        if (state.cuisineList != null) {

            var data: List<Cuisine> = state.cuisineList

            if (searchString.value.isNotEmpty()) {
                data = state.cuisineList.filter {
                    it.products.joinToString().lowercase().contains(searchString.value)
                        .or(it.title.lowercase().contains(searchString.value))
                }
                if (data.isEmpty()) {
                    Placeholder(
                        icon = Icons.TwoTone.FindInPage,
                        text = "Бу сузләр өчен бернәрсәдә табылмаган"
                    )
                }
            }
            LazyColumn {
                items(data.size) { index ->
                    CuisineItem(data[index]) {
                        idState.value = it
                    }
                    Spacer(Modifier.height(10.dp))
                }
            }
        } else if (!state.isLoading) {
            Placeholder(icon = Icons.TwoTone.Error, text = "Нәрсәдер начар булып чыккан")
        }

        if (state.error.isNotBlank()) {
            showSnackbar(
                rememberCoroutineScope(),
                snackState,
                state.error,
                "Тагын"
            ) {
                if (it == SnackbarResult.ActionPerformed) {
                    viewModel.reload()
                }
            }
        }

        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }

}
