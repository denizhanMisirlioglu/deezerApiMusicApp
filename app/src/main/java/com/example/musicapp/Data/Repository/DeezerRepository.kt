package com.example.musicapp.Data.Repository

import com.example.musicapp.Data.Album
import com.example.musicapp.Data.Artist
import com.example.musicapp.Data.ArtistDetail
import com.example.musicapp.Data.Category
import com.example.musicapp.Data.AlbumDetails


class DeezerRepository {
    private val deezerApiService = DeezerApiService.create()

    suspend fun getCategories(): List<Category> {
        val response = deezerApiService.getGenres()
        return response.data.map { category ->
            Category(category.id, category.name, category.picture_medium)
        }
    }

    suspend fun getArtists(genreId: Int): List<Artist> {
        val response = deezerApiService.getArtists(genreId)
        return response.data
    }

    suspend fun getArtistDetail(artistId: Int): ArtistDetail {
        val response = deezerApiService.getArtistDetail(artistId)

        return ArtistDetail(
            id = response.id,
            name = response.name,
            pictureBig = response.picture_big,
            albums = response.albums
        )
    }

    suspend fun getArtistAlbums(artistId: Int): List<Album> {
        val response = deezerApiService.getArtistAlbums(artistId)
        return response.toAlbumList()
    }

    suspend fun getAlbumDetails(albumId: Int): AlbumDetails {
        val response = deezerApiService.getAlbumDetails(albumId)
        return AlbumDetails(
            id = response.id,
            title = response.title,
            cover_medium = response.cover_medium,
            songs = response.tracks.data
        )
    }
}