package com.example.musicapp.Interface

import TopBar
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.musicapp.ViewModel.PlayerViewModel
import com.example.musicapp.Data.Song
import androidx.compose.runtime.*

import androidx.compose.runtime.DisposableEffect

@Composable
fun FavoritesScreen() {
    val context = LocalContext.current
    val playerViewModel = remember { PlayerViewModel(context) }
    val favorites by playerViewModel.favorites.collectAsState(emptyList<Song>())

    Scaffold(
        topBar = { TopBar(title = "Favorites") },
        content = { padding ->
            Column(modifier = Modifier.padding(padding)) {
                FavoritesList(favorites, playerViewModel)
            }
        }
    )

    DisposableEffect(Unit) {
        onDispose {
            playerViewModel.stopSong()
        }
    }
}

@Composable
fun FavoritesList(
    favorites: List<Song>,
    playerViewModel: PlayerViewModel
) {
    LazyColumn {
        items(favorites.size) { index ->
            val song = favorites[index]
            AlbumDetailItem(song, playerViewModel)
        }
    }
}
