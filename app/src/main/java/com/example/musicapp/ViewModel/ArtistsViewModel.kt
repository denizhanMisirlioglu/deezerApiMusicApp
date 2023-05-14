package com.example.musicapp.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.Data.Artist
import com.example.musicapp.Data.ArtistDetail
import com.example.musicapp.Data.Repository.DeezerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ArtistsViewModel : ViewModel() {
    private val deezerRepository = DeezerRepository()

    private val _artists = MutableStateFlow<List<Artist>>(emptyList())
    val artists: StateFlow<List<Artist>>
        get() = _artists

    private val _artistDetails = MutableStateFlow<List<ArtistDetail>>(emptyList())
    val artistDetails: StateFlow<List<ArtistDetail>>
        get() = _artistDetails

    fun fetchArtists(genreId: Int) {
        viewModelScope.launch {
            try {
                val artists = deezerRepository.getArtists(genreId)
                _artists.value = artists
            } catch (e: Exception) {
                Log.e("MusicViewModel", "Failed to fetch artists: " + e.message)
            }
        }
    }

    fun fetchArtistDetails(artistId: Int) {
        viewModelScope.launch {
            val artistDetailsData = deezerRepository.getArtistDetail(artistId)

            // Fetch artist’s albums
            val artistAlbums = deezerRepository.getArtistAlbums(artistId)

            // Combine artist details and albums
            val artistWithAlbums = artistDetailsData.copy(albums = artistAlbums)

            _artistDetails.value = listOf(artistWithAlbums)
        }
    }
}
