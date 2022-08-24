package ai.benshi.test.db

import ai.benshi.test.model.Post
import ai.benshi.test.model.PostWithComment
import androidx.paging.PagingSource
import androidx.room.*

@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(posts: List<Post>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: Post)

    @Query("SELECT * FROM posts")
    fun getPosts() : PagingSource<Int, Post>

    @Transaction
    @Query("SELECT * FROM posts WHERE id = :id")
    fun getPostWithComment(id: Int): PostWithComment
//
//    @Transaction
//    @Query("SELECT * FROM posts")
//    fun getPostWithUser(): List<PostWithUser>


    @Query("DELETE FROM posts WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM posts")
    suspend fun deleteAll()

}
