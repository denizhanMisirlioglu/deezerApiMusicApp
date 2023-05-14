package com.example.musicapp.Interface

import TopBar
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.musicapp.Data.Song
import com.example.musicapp.ViewModel.AlbumDetailsViewModel
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.example.musicapp.ViewModel.PlayerViewModel
import com.example.musicapp.R
import androidx.lifecycle.ViewModelStoreOwner
import com.example.musicapp.Data.Repository.DeezerRepository


@Composable
fun AlbumDetailScreen(
    albumId: Int
) {
    val context = LocalContext.current
    val albumDetailsViewModel: AlbumDetailsViewModel = viewModel()

    val playerViewModel = remember { PlayerViewModel(context) }
    albumDetailsViewModel.fetchAlbumDetails(albumId)
    val albumDetails = albumDetailsViewModel.albumDetails.collectAsState().value
    albumDetails?.let { details ->
        Scaffold(
            topBar = {
                TopBar(title = details.title)
            },
            content = { padding ->
                Column(
                    modifier = Modifier.padding(padding)
                ) {
                    LazyColumn {
                        itemsIndexed(details.songs) { _, song -> // Use itemsIndexed instead of items
                            AlbumDetailItem(song, playerViewModel)
                        }
                    }
                }
            }
        )
    }
    DisposableEffect(Unit) {
        onDispose {
            playerViewModel.stopSong() // Add a stopSong function to the PlayerViewModel, when page changes stop the song
        }
    }
}

@Composable
fun AlbumDetailItem(song: Song,  playerViewModel: PlayerViewModel) {
    val context = LocalContext.current
    // Observe the favorite status of the song
    val isFavoriteState = remember { mutableStateOf(playerViewModel.isFavorite(song)) }
    // Watch for changes in the favorites and update the isFavoriteState appropriately
    LaunchedEffect(playerViewModel.favorites) {
        isFavoriteState.value = playerViewModel.isFavorite(song)
    }
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(
                color = Color.Black.copy(alpha = 0.5f),
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        val painter = rememberImagePainter(
            data = song.album.cover_medium,
            builder = {
                crossfade(true)
                transformations(CircleCropTransformation())
            }
        )

        Image(
            painter = painter,
            contentDescription = song.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.CenterVertically)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp)
                .align(Alignment.CenterVertically)
        ) {
            Text(
                text = song.title,
                style = MaterialTheme.typography.subtitle1,
                color = Color.White
            )
            Text(
                text = "${song.duration}",
            style = MaterialTheme.typography.caption,
            color = Color.White
            )
        }

        IconButton(
            onClick = {
                if (playerViewModel.currentSong.value?.id == song.id) {
                    if (playerViewModel.isPlaying.value) {
                        playerViewModel.pauseSong()
                    } else {
                        playerViewModel.resumeSong()
                    }
                } else {
                    playerViewModel.playSong(context, song)
                }
            },
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(8.dp)
        ) {
            Icon(
                painter = painterResource(
            id = if (playerViewModel.currentSong.value?.id == song.id && playerViewModel.isPlaying.value) R.drawable.pause
            else R.drawable.play_buttton),
            contentDescription = if (playerViewModel.currentSong.value?.id == song.id && playerViewModel.isPlaying.value) "Pause"
            else "Play",
            modifier = Modifier.size(24.dp)
            )
        }
        IconButton(
            onClick = {
                if (playerViewModel.isFavorite(song)) {
                    playerViewModel.removeFavoriteSong(song)
                } else {
                    playerViewModel.addFavoriteSong(song)
                }
                // Update isFavoriteState when heart icon is clicked
                isFavoriteState.value = !isFavoriteState.value
            },
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(8.dp)
        ) {
            Icon(
                painter = painterResource(id = if (isFavoriteState.value) R.drawable.heart_full else R.drawable.heart_empty),
                contentDescription = if (isFavoriteState.value) "Favorite" else "Not Favorite",
            modifier = Modifier.size(24.dp)
            )
        }
    }
}