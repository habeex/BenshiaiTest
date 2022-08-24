package ai.benshi.test.model

import androidx.room.*

@Entity(tableName = "posts")
data class Post (
    @PrimaryKey
    var id: Int,
    var userId: Int,
    var title: String?,
    var body: String?,
    @Embedded var user: PostUser
)

data class PostWithComment(
    @Embedded val post: Post,
    @Relation(
        parentColumn = "id",
        entityColumn = "postId"
    )
    val comments: List<Comment>
)