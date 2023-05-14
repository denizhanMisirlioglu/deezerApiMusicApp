package com.example.musicapp.Data.Repository

import com.example.musicapp.Data.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface DeezerApiService {
    @GET("genre?output=json")  // json endpoint instead of “/”
    suspend fun getGenres(): GenreResponse

    @GET("genre/{genre_id}/artists")
    suspend fun getArtists(@Path("genre_id") genreId: Int): ArtistResponse

    @GET("artist/{artist_id}")
    suspend fun getArtistDetail(@Path("artist_id") artistId: Int): ArtistDetailResponse

    @GET("artist/{artist_id}/albums")
    suspend fun getArtistAlbums(@Path("artist_id") artistId: Int): AlbumResponse

    data class AlbumResponse(val data: List<Album>) {

        fun toAlbumList(): List<Album> {
            return data.map { albumData ->
                Album(
                    id = albumData.id,
                    title = albumData.title,
                    link = albumData.link,
                    cover = albumData.cover,
                    cover_small = albumData.cover_small,
                    cover_medium = albumData.cover_medium,
                    cover_big = albumData.cover_big,
                    cover_xl = albumData.cover_xl,
                    release_date = albumData.release_date,
                    tracklist = albumData.tracklist,
                    type = albumData.type
                )
            }
        }
    }

    @GET("album/{album_id}")
    suspend fun getAlbumDetails(@Path("album_id") albumId: Int): AlbumDetailResponse

    data class AlbumDetailResponse(
        val id: Int,
        val title: String,
        val cover_medium: String,
        val tracks: TracksResponse
    )

    data class TracksResponse(
        val data: List<Song>,
        val inherit_album_cover_medium: String
    )

    // Create a new class for storing response fields.
    data class GenreResponse(val data: List<Category>)
    data class ArtistResponse(val data: List<Artist>)
    data class ArtistDetailResponse(val id: Int, val name: String, val picture_big: String, val albums: List<Album>?)

    companion object {
        private const val BASE_URL = "https://api.deezer.com/"

        fun create(): DeezerApiService {
            val client = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(DeezerApiService::class.java)
        }
    }
}