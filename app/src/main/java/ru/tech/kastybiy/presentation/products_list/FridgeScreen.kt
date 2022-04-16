package ru.tech.kastybiy.presentation.products_list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.twotone.CloudOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.tech.kastybiy.presentation.app.components.Placeholder
import ru.tech.kastybiy.presentation.products_list.viewModel.FridgeViewModel

@Composable
fun FridgeScreen(viewModel: FridgeViewModel = hiltViewModel()) {

    val state = viewModel.listState.value

    Box(modifier = Modifier.fillMaxSize()) {

        if (state.products != null) {
            LazyColumn(contentPadding = PaddingValues(bottom = 200.dp, top = 10.dp)) {
                items(state.products.size) { index ->
                    Row(
                        Modifier.padding(start = 10.dp, end = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            state.products[index].name.replaceFirstChar { it.uppercase() },
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(Modifier.weight(1f))
                        IconButton(onClick = { viewModel.remove(state.products[index].id) }) {
                            Icon(Icons.Outlined.DeleteOutline, null)
                        }
                    }
                    Spacer(Modifier.height(10.dp))
                }
            }
        } else if (state.error.isNotBlank()) {
            Placeholder(icon = Icons.TwoTone.CloudOff, text = state.error)
        }

        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }

}