package com.example.musicapp.Interface

import TopBar
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.musicapp.Data.Album
import com.example.musicapp.ViewModel.ArtistsViewModel

@Composable
fun ArtistDetailScreen(artistId: Int,navController: NavController) {
    val artistsViewModel: ArtistsViewModel = viewModel()
    artistsViewModel.fetchArtistDetails(artistId)
    val artistDetails by artistsViewModel.artistDetails.collectAsState()

    artistDetails.let { details ->
        val artistDetail = details.firstOrNull()
        artistDetail?.let {
            Scaffold(
                topBar = {
                    TopBar(title = it.name)
                },
                content = { padding ->
                    Column(modifier = Modifier.padding(padding)) {
                        val painter = rememberAsyncImagePainter(model = it.pictureBig)
                        Image(
                            painter = painter,
                            contentDescription = it.name,
                            contentScale = ContentScale.FillWidth,
                            modifier = Modifier
                                .fillMaxWidth(0.5f) // Change this line to set the image width to half
                                .padding(horizontal = 16.dp)
                                .align(Alignment.CenterHorizontally) // Center the image horizontally
                        )


                        if (it.albums.isNullOrEmpty()) {
                            Log.d("ArtistDetailScreen", "No albums found for artist ${it.name}")
                        }

                        LazyColumn {
                            items(it.albums ?: emptyList()) { album ->
                                ArtistDetailItem(album, navController) // Pass the navController to ArtistDetailItem
                            }
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun ArtistDetailItem(album: Album, navController: NavController, // Add the navController parameter
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(
                color = Color.Black.copy(alpha = 0.5f), // Change the alpha value here
                shape = RoundedCornerShape(8.dp)
            )
            .border( // Add this line to draw an outline around the item
                width = 1.dp,
                color = Color.LightGray,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(onClick = { navController.navigate("albumDetail/${album.id}") }) // Add this line for navigation

    ) {
        val painter = rememberAsyncImagePainter(model = album.cover_medium)
        Image(
            painter = painter,
            contentDescription = album.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.CenterVertically)
        )

        Column(
            modifier = Modifier
                .weight(1f) // 2/3 of the width
                .padding(start = 16.dp)
                .align(Alignment.CenterVertically)
        ) {
            Text(text = album.title ?: "Unknown title", style = MaterialTheme.typography.subtitle1, color = Color.White)
            Text(text = album.release_date ?: "Unknown release date", style = MaterialTheme.typography.caption, color = Color.White)

        }
    }
}