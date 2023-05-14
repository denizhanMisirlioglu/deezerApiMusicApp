package com.example.musicapp.Interface

import TopBar
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.musicapp.Data.Artist
import com.example.musicapp.ViewModel.ArtistsViewModel
import com.example.musicapp.ViewModel.CategoriesViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue


@Composable
fun ArtistsScreen(navController: NavController, categoryId: Int) {
    val categoriesViewModel: CategoriesViewModel = viewModel()
    val allCategories by categoriesViewModel.categories.collectAsState()
    val category = allCategories.firstOrNull { it.id == categoryId }

    val artistsViewModel: ArtistsViewModel = viewModel()
    val artists by artistsViewModel.artists.collectAsState()
    artistsViewModel.fetchArtists(categoryId)

    if (category != null) {
        Scaffold(
            topBar = { TopBar(title = category.name) },
            content = { padding ->
                Column(modifier = Modifier.padding(padding)) {
                    ArtistsScreenContent(
                        artists = artists,
                        onArtistSelected = { artist -> // FIX HERE
                            navController.navigate("artistDetail/${artist.id}")
                        }
                    )
                }
            }
        )
    } else {
        Text("Category not found")
    }
}

@Composable
fun ArtistsScreenContent(
    artists: List<Artist>,
    onArtistSelected: (Artist) -> Unit = {},
) {
    Column {

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(artists) { artist ->
                ArtistItem(artist) { onArtistSelected(artist) } // FIX HERE
            }
        }
    }
}

@Composable
private fun ArtistItem(artist: Artist, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .padding(8.dp)
            .clickable(onClick = onClick)
            .aspectRatio(1f),
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp,
        border = BorderStroke(width = 1.dp, color = Color.LightGray)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            val painter = rememberAsyncImagePainter(model = artist.picture_medium)

            Image(
                painter = painter,
                contentDescription = artist.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Text(
                text = artist.name,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .background(color = Color.Black.copy(alpha = 0.7f))
                    .fillMaxWidth()
                    .padding(4.dp),
                style = MaterialTheme.typography.body2,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}