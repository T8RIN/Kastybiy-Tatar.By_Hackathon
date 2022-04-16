package ru.tech.kastybiy.presentation.app.components

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FindReplace
import androidx.compose.material.icons.outlined.PhoneAndroid
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.tech.kastybiy.R
import ru.tech.kastybiy.presentation.app.viewModel.MainViewModel
import ru.tech.kastybiy.presentation.dish_details.DishDetailsScreen
import ru.tech.kastybiy.presentation.dishes_based_on_fridge.OnFridgeBasedDishes
import ru.tech.kastybiy.presentation.favourite_dishes.FavouriteListScreen
import ru.tech.kastybiy.presentation.products_list.FridgeScreen
import ru.tech.kastybiy.presentation.tatar_cuisine.CuisineScreen
import ru.tech.kastybiy.presentation.ui.theme.KastybiyTheme
import ru.tech.kastybiy.presentation.ui.utils.Screen
import ru.tech.kastybiy.presentation.ui.utils.showSnackbar

@ExperimentalAnimationApi
@ExperimentalMaterial3Api
@Composable
fun KastybiyApp(viewModel: MainViewModel = viewModel()) {
    KastybiyTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val snackbarHostState = remember { SnackbarHostState() }
            val navController = rememberNavController()
            val scope = rememberCoroutineScope()

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
                        CenterAlignedTopAppBar(
                            modifier = Modifier.statusBarsPadding(),
                            title = { Text(viewModel.title) },
                            scrollBehavior = viewModel.scrollBehavior,
                            colors = foregroundColors
                        )
                    }
                },
                bottomBar = {
                    Surface(
                        color = TopAppBarDefaults.smallTopAppBarColors().containerColor(100f).value
                    ) {
                        NavigationBar(modifier = Modifier.navigationBarsPadding()) {
                            val items = listOf(
                                Screen.Cuisine,
                                Screen.Fridge,
                                Screen.Favourites
                            )

                            items.forEachIndexed { index, screen ->

                                viewModel.selectedItem =
                                    items.indexOfFirst { it.route == navController.currentDestination?.route }

                                NavigationBarItem(
                                    icon = {
                                        if (screen.baseIcon != Icons.Outlined.PhoneAndroid) {
                                            Icon(
                                                if (viewModel.selectedItem == index) screen.selectedIcon else screen.baseIcon,
                                                null
                                            )
                                        } else {
                                            Icon(
                                                if (viewModel.selectedItem == index) painterResource(
                                                    R.drawable.fridge
                                                ) else painterResource(R.drawable.fridge_outline),
                                                null
                                            )
                                        }
                                    },
                                    alwaysShowLabel = false,
                                    label = { Text(screen.shortTitle) },
                                    selected = viewModel.selectedItem == index,
                                    onClick = {
                                        if (viewModel.selectedItem != index) {
                                            viewModel.title = screen.title
                                            viewModel.selectedItem = index
                                            navController.navigate(screen.route) {
                                                navController.popBackStack()
                                                launchSingleTop = true
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }
                },
                floatingActionButton = {
                    AnimatedVisibility(
                        visible = viewModel.selectedItem == 1,
                        enter = fadeIn() + scaleIn(),
                        exit = fadeOut() + scaleOut()
                    ) {
                        Column(horizontalAlignment = Alignment.End) {
                            SmallFloatingActionButton(
                                onClick = {
                                    if (viewModel.productsList.value.error.isNotBlank()) {
                                        showSnackbar(
                                            scope,
                                            snackbarHostState,
                                            viewModel.productsList.value.error,
                                            "Яхшы"
                                        ) {
                                            if (it == SnackbarResult.ActionPerformed) viewModel.reload()
                                        }
                                    } else if (viewModel.productsList.value.list?.isNotEmpty() == true) {
                                        viewModel.showProductsDialog = true
                                    } else {
                                        showSnackbar(
                                            scope,
                                            snackbarHostState,
                                            "Суыткыч тулысынча тулы",
                                            ""
                                        ) {}
                                    }
                                },
                                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                            ) {
                                Icon(Icons.Rounded.Add, null)
                            }
                            Spacer(Modifier.size(8.dp))
                            ExtendedFloatingActionButton(
                                onClick = {
                                    if (viewModel.productsList.value.list != null) viewModel.openSheet = true
                                },
                                text = {
                                    Text("Сайларга")
                                },
                                icon = {
                                    Icon(Icons.Outlined.FindReplace, null)
                                })
                        }
                    }
                },
                snackbarHost = { SnackbarHost(snackbarHostState) },
                modifier = Modifier.nestedScroll(viewModel.scrollBehavior.nestedScrollConnection)
            ) { pv ->
                NavHost(
                    navController = navController,
                    startDestination = Screen.Cuisine.route,
                    Modifier.padding(pv)
                ) {
                    composable(Screen.Cuisine.route) {
                        CuisineScreen(snackbarHostState, viewModel.id)
                    }
                    composable(Screen.Fridge.route) {
                        FridgeScreen()
                    }
                    composable(Screen.Favourites.route) {
                        FavouriteListScreen(snackbarHostState, viewModel.id)
                    }
                }
            }

            AnimatedVisibility(
                visible = viewModel.openSheet,
                enter = fadeIn() + slideInHorizontally(),
                exit = fadeOut() + slideOutHorizontally()
            ) {
                BackHandler {
                    viewModel.openSheet = false
                }
                OnFridgeBasedDishes(viewModel.id, goBack = { viewModel.openSheet = false })
            }

            AnimatedVisibility(
                visible = viewModel.id.value != -1,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                BackHandler {
                    viewModel.id.value = -1
                }
                DishDetailsScreen(viewModel.id.value, goBack = { viewModel.id.value = -1 })
            }

            if (viewModel.showProductsDialog) {

                val maxHeight = LocalConfiguration.current.screenHeightDp.dp * 0.75f

                AlertDialog(
                    modifier = Modifier.heightIn(max = maxHeight),
                    onDismissRequest = {},
                    confirmButton = {
                        TextButton(onClick = {
                            viewModel.processToFridge()
                            viewModel.showProductsDialog = false
                        }) {
                            Text("Яхшы")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { viewModel.showProductsDialog = false }) {
                            Text("Артка")
                        }
                    },
                    title = {
                        Text("Ашамлыкларны сайлагыз")
                    },
                    text = {
                        val state = viewModel.productsList.value

                        Box(Modifier.fillMaxWidth()) {
                            if (state.list != null && !state.isLoading) {
                                LazyColumn {
                                    items(state.list.size) { index ->
                                        Row(
                                            Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                state.list[index].name.replaceFirstChar { it.uppercase() },
                                                textAlign = TextAlign.Start,
                                                style = MaterialTheme.typography.bodyLarge,
                                                modifier = Modifier.padding(horizontal = 10.dp)
                                            )
                                            Spacer(Modifier.weight(1f))
                                            Checkbox(
                                                checked = viewModel.tempList.contains(state.list[index].id),
                                                onCheckedChange = {
                                                    if (it) viewModel.tempList.add(state.list[index].id)
                                                    else viewModel.tempList.remove(state.list[index].id)
                                                }
                                            )
                                        }
                                        Spacer(Modifier.height(10.dp))
                                    }
                                }
                            }

                            if (state.isLoading) {
                                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                            }
                        }
                    }
                )
            }
        }
    }
}