package com.example.musicapp.Interface

import MusicCategoriesScreen
import TopBar
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.example.musicapp.ui.theme.MusicAppTheme
import com.example.musicapp.ViewModel.CategoriesViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController


class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MusicAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainScreen()
                }
            }
        }


    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val localViewModelStoreOwner = LocalViewModelStoreOwner.current

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        },
        content = { padding ->
            NavHost(navController, startDestination = "musicCategories") {
                composable("musicCategories") {
                    val categoriesViewModel: CategoriesViewModel = viewModel()
                    val categories by categoriesViewModel.categories.collectAsState()

                    MusicCategoriesScreen(
                        categories = categories,
                        onCategorySelected = { category ->
                            navController.navigate("artistsDetail/${category.id}")
                        },
                        topBar = { TopBar(title = "Music Categories") },
                        modifier = Modifier.padding(padding)
                    )
                }
                composable("artistsDetail/{categoryId}") { backStackEntry ->
                    val categoryId = backStackEntry.arguments?.getString("categoryId")?.toIntOrNull()
                    if (categoryId != null) {
                        ArtistsScreen(navController, categoryId)
                    }
                }
                composable("artistDetail/{artistId}") { backStackEntry ->
                    val artistId = backStackEntry.arguments?.getString("artistId")?.toIntOrNull()
                    if (artistId != null) {
                        ArtistDetailScreen(artistId, navController)
                    }
                }
                composable("albumDetail/{albumId}") { backStackEntry ->
                    val albumId = backStackEntry.arguments?.getString("albumId")?.toIntOrNull()
                    if (albumId != null) {
                        if (localViewModelStoreOwner != null) {
                            AlbumDetailScreen(albumId)
                        }
                    }
                }
                composable("favorites") {
                    FavoritesScreen()
                }
            }
        }
    )
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    BottomNavigation  {
        BottomNavigationItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = null) },
            selected = currentRoute == "musicCategories",
            onClick = {
                navController.navigate("musicCategories") {
                // Corrected line:
                popUpTo("musicCategories") {
                inclusive = true
            }
                launchSingleTop = true
            }
            }
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
            selected = currentRoute == "favorites",
            onClick = {
                navController.navigate("favorites") {
                // Corrected line:
                popUpTo("musicCategories") {
                inclusive = true
            }
                launchSingleTop = true
            }
            }
        )
    }
}

