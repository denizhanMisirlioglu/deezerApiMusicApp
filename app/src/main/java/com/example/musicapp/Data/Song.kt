package com.example.musicapp.Data

data class Song(
    val id: Int,
    val title: String,
    val duration: Int, // in seconds
    val album: Album,
    val cover_medium: String,
    val preview: String // 30 seconds preview URL
)