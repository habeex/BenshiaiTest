package ai.benshi.test.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comments")
data class Comment (
    @PrimaryKey
    val id: Int,
    val postId: Int,
    val name: String,
    val email: String,
    val body: String
)