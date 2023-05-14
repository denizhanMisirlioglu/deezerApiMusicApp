package com.example.musicapp.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.Data.AlbumDetails
import com.example.musicapp.Data.Repository.DeezerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AlbumDetailsViewModel : ViewModel() {
    private val deezerRepository = DeezerRepository()

    private val _albumDetails = MutableStateFlow<AlbumDetails?>(null)
    val albumDetails: StateFlow<AlbumDetails?>
        get() = _albumDetails

    fun fetchAlbumDetails(albumId: Int) {
        viewModelScope.launch {
            try {
                val albumDetails = deezerRepository.getAlbumDetails(albumId)
                _albumDetails.value = albumDetails
            } catch (e: Exception) {
                Log.e("AlbumDetailsViewModel", "Failed to fetch album details: " + e.message)
            }
        }
    }
}