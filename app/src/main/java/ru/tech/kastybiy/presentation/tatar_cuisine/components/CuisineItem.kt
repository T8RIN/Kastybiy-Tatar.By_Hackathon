package ru.tech.kastybiy.presentation.tatar_cuisine.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AvTimer
import androidx.compose.material.icons.outlined.BrokenImage
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import ru.tech.kastybiy.domain.model.Cuisine

@Composable
fun CuisineItem(cuisine: Cuisine, onClick: (id: Int) -> Unit) {
    Column(
        Modifier
            .clip(RoundedCornerShape(6.dp))
            .clickable {
                onClick(cuisine.id)
            }) {
        Row(Modifier.padding(10.dp)) {
            SubcomposeAsyncImage(
                modifier = Modifier
                    .height(100.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .weight(1f),
                contentScale = ContentScale.Crop,
                model = ImageRequest.Builder(LocalContext.current)
                    .data(cuisine.iconUrl)
                    .crossfade(true)
                    .build(),
                loading = {
                    Box(Modifier.fillMaxSize()) {
                        CircularProgressIndicator(Modifier.align(Alignment.Center))
                    }
                },
                error = {
                    Icon(
                        modifier = Modifier.fillMaxSize(),
                        imageVector = Icons.Outlined.BrokenImage,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                },
                contentDescription = null
            )
            Spacer(Modifier.size(20.dp))
            Column(
                Modifier
                    .weight(1f)
                    .height(100.dp)
            ) {
                Spacer(Modifier.size(10.dp))
                Text(cuisine.title, style = MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.weight(1f))
                Text(
                    cuisine.products.joinToString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline,
                    maxLines = 4
                )
                Spacer(Modifier.size(10.dp))
            }
            Spacer(Modifier.size(20.dp))
            Column(
                Modifier.height(100.dp)
            ) {
                Spacer(Modifier.size(10.dp))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Outlined.AvTimer, null)
                    Spacer(Modifier.size(15.dp))
                    Text("${cuisine.cookTime} мин", textAlign = TextAlign.Center)
                }
                Spacer(Modifier.size(5.dp))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Outlined.LocalFireDepartment,
                        null,
                        tint = MaterialTheme.colorScheme.outline
                    )
                    Spacer(Modifier.size(15.dp))
                    Text(
                        "Б ${cuisine.proteins}\nЖ ${cuisine.fats}\nУ ${cuisine.carboh}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
                Spacer(Modifier.size(10.dp))
            }
        }
        Spacer(Modifier.size(10.dp))
        Divider(Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.surfaceVariant)
    }
}