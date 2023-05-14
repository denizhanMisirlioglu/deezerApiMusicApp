package com.example.musicapp.Data

data class AlbumDetails(
    val id: Int,
    val title: String,
    val cover_medium: String,
    val songs: List<Song>
)