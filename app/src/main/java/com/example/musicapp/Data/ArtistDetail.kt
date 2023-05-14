package com.example.musicapp.Data

data class ArtistDetail(
    val id: Int,
    val name: String,
    val pictureBig: String,
    val albums: List<Album>?,
)
