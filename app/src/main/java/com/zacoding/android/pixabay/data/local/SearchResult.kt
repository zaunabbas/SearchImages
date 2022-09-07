package com.zacoding.android.pixabay.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import javax.annotation.concurrent.Immutable

@Entity(tableName = "search_results")
@Immutable
data class SearchResult(
    @PrimaryKey
    val id: Long,
    val tags: String,
    val previewURL: String,
    val largeImageURL: String,
    val downloads: Int,
    val likes: Int,
    val comments: Int,
    val user: String,
) : Serializable {

    fun getTagsList(): List<String> {
        return tags.split(",")
    }

    companion object {
        fun mock() = arrayListOf(
            SearchResult(
                id = 575547,
                tags = "cherry, fruit, food",
                previewURL = "https://cdn.pixabay.com/photo/2014/12/21/23/34/cherry-575547_150.png",
                largeImageURL = "https://pixabay.com/get/g859c78663c4ac3a7e3fc5d52f3105741f01e21a24f370fdaf07248ea6cb2e98cf4188adbefbbf3d5778dbf81489461d8f14ba755f3d281620f4ff90ac64bd948_1280.png",
                downloads = 61668,
                likes = 343,
                comments = 60,
                user = "OpenClipart-Vectors",
            ),
            SearchResult(
                id = 2788662,
                tags = "apple, red, hand",
                previewURL = "https://cdn.pixabay.com/photo/2017/09/26/13/42/apple-2788662_150.jpg",
                largeImageURL = "https://pixabay.com/get/g2ad38adca2a6c2b884f4033547b4d5cd6e5f970dbac1b16d8d0466f39085e0c1bb2416a7a63ebd6ea288bd28a6e6860855e35f9a827c289fa7934eb7dbd0cc4d_1280.jpg",
                downloads = 61668,
                likes = 343,
                comments = 60,
                user = "OpenClipart-Vectors",
            )
        )
    }

}

@Entity(tableName = "search_query_results", primaryKeys = ["searchQuery", "searchedDataId"])
data class SearchQueryResult(
    val searchQuery: String,
    val searchedDataId: Long,
    val queryPosition: Int
)