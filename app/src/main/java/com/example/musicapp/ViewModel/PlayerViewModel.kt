package com.example.musicapp.ViewModel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.musicapp.Data.Song
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.gson.Gson
import com.google.android.exoplayer2.MediaItem
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class PlayerViewModel(private val context: Context) : ViewModel() {
    var currentPlayer = mutableStateOf<SimpleExoPlayer?>(null)
    var currentSong = mutableStateOf<Song?>(null)
    var isPlaying = mutableStateOf(false)

    private val sharedPreferences = context.getSharedPreferences("music_app", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val _favorites = MutableStateFlow<List<Song>>(emptyList())
    val favorites: StateFlow<List<Song>> = _favorites.asStateFlow()

    init {
        // Load favorites during ViewModel initialization
        _favorites.value = loadFavorites()
    }

    fun createExoPlayer(context: Context): SimpleExoPlayer {
        return SimpleExoPlayer.Builder(context).build()
    }

    fun playSong(context: Context, song: Song) {
        currentPlayer.value?.let {
            if (currentSong.value?.id == song.id) {
                if (isPlaying.value) {
                    pauseSong()
                } else {
                    resumeSong()
                }
                return
            } else {
                it.pause()
                it.release()
            }
        }

        currentSong.value = song
        val player = createExoPlayer(context)
        player.setMediaItem(MediaItem.fromUri(song.preview))
        player.prepare()
        player.play()
        isPlaying.value = true
        currentPlayer.value = player
    }

    fun pauseSong() {
        currentPlayer.value?.pause()
        isPlaying.value = false
    }

    fun resumeSong() {
        currentPlayer.value?.play()
        isPlaying.value = true
    }
    fun stopSong() {
        currentPlayer.value?.stop()
        isPlaying.value =false
    }

    override fun onCleared() {
        super.onCleared()
        currentPlayer.value?.release()
    }

    // Add a private function to load favorites from SharedPreferences
    private fun loadFavorites(): List<Song> {
        val json = sharedPreferences.getString("favorites", "[]") ?: "[]"
        val type = object : TypeToken<List<Song>>() {}.type
        return gson.fromJson(json, type)
    }

    // Save favorites to SharedPreferences
    private fun saveFavorites() {
        sharedPreferences.edit().putString("favorites", gson.toJson(_favorites.value)).apply()
    }
    // Update addFavoriteSong and removeFavoriteSong functions to save favorites after making changes
    fun addFavoriteSong(song: Song) {
        _favorites.value = _favorites.value.toMutableList().apply { add(song) }
        saveFavorites()
    }

    fun removeFavoriteSong(song: Song) {
        _favorites.value = _favorites.value.toMutableList().apply { remove(song) }
        saveFavorites()
    }

    fun isFavorite(song: Song): Boolean {
        return _favorites.value.contains(song)
    }

}
