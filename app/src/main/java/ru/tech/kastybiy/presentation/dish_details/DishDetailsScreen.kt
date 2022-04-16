package ru.tech.kastybiy.presentation.dish_details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BrokenImage
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.twotone.Error
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.google.accompanist.flowlayout.FlowRow
import ru.tech.kastybiy.presentation.app.components.Placeholder
import ru.tech.kastybiy.presentation.dish_details.components.InfoItem
import ru.tech.kastybiy.presentation.dish_details.viewModel.DishDetailsViewModel

@ExperimentalMaterial3Api
@Composable
fun DishDetailsScreen(
    id: Int,
    goBack: () -> Unit,
    viewModel: DishDetailsViewModel = hiltViewModel()
) {

    LaunchedEffect(Unit) {
        viewModel.load(id)
    }

    val state = viewModel.dishState.value

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            topBar = {
                val backgroundColors = TopAppBarDefaults.smallTopAppBarColors()
                val backgroundColor = backgroundColors.containerColor(
                    scrollFraction = viewModel.scrollBehavior.scrollFraction
                ).value
                val foregroundColors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent
                )

                Surface(color = backgroundColor) {
                    LargeTopAppBar(
                        modifier = Modifier.statusBarsPadding(),
                        title = { Text(state.dish?.title ?: "") },
                        navigationIcon = {
                            IconButton(onClick = { goBack() }) {
                                Icon(Icons.Rounded.ArrowBack, null)
                            }
                        },
                        scrollBehavior = viewModel.scrollBehavior,
                        colors = foregroundColors
                    )
                }
            },
            modifier = Modifier.nestedScroll(viewModel.scrollBehavior.nestedScrollConnection)
        ) {

            Box(modifier = Modifier.fillMaxSize()) {
                if (state.dish != null) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            top = WindowInsets.statusBars.asPaddingValues()
                                .calculateTopPadding(),
                            bottom = WindowInsets.navigationBars.asPaddingValues()
                                .calculateBottomPadding() + 100.dp
                        )
                    ) {
                        item {
                            Column(modifier = Modifier.padding(horizontal = 10.dp)) {
                                SubcomposeAsyncImage(
                                    modifier = Modifier
                                        .height(200.dp)
                                        .fillMaxWidth()
                                        .padding(horizontal = 10.dp)
                                        .clip(RoundedCornerShape(24.dp)),
                                    contentScale = ContentScale.Crop,
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(state.dish.iconUrl)
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

                                Spacer(modifier = Modifier.height(15.dp))
                                Text(
                                    text = "Ашамлык турында",
                                    color = MaterialTheme.colorScheme.onBackground,
                                    style = MaterialTheme.typography.headlineSmall
                                )
                                Spacer(modifier = Modifier.height(30.dp))

                                FlowRow(
                                    mainAxisSpacing = 10.dp,
                                    crossAxisSpacing = 10.dp,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 15.dp)
                                ) {
                                    InfoItem(state.dish.category)
                                    InfoItem("Әзерләргә ${state.dish.cookTime} мин")
                                    InfoItem("${state.dish.proteins}г/100г Б")
                                    InfoItem("${state.dish.fats}г/100г Ж")
                                    InfoItem("${state.dish.carboh}г/100г У")
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    state.dish.products.joinToString(),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 15.dp)
                                )
                                Spacer(modifier = Modifier.height(30.dp))
                                Text(
                                    text = "Әзерләү ысулы",
                                    color = MaterialTheme.colorScheme.onBackground,
                                    style = MaterialTheme.typography.headlineSmall
                                )
                                Spacer(modifier = Modifier.height(30.dp))
                            }
                        }
                        items(state.dish.cookSteps.size) {
                            Text(
                                state.dish.cookSteps[it],
                                modifier = Modifier.padding(horizontal = 15.dp)
                            )
                            Spacer(Modifier.size(10.dp))
                        }
                    }
                    FloatingActionButton(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .navigationBarsPadding()
                            .padding(end = 16.dp, bottom = 16.dp),
                        onClick = { viewModel.processFavorites(id) }
                    ) {
                        Icon(
                            if (viewModel.isFavorite.value) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                            null
                        )
                    }
                } else if (!state.isLoading && state.error.isBlank()) {
                    Placeholder(
                        icon = Icons.TwoTone.Error,
                        text = "Нәрсәдер начар булып чыккан"
                    )
                }

                if (state.error.isNotBlank()) {
                    Placeholder(icon = Icons.TwoTone.Error, text = state.error)
                }

                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }

}