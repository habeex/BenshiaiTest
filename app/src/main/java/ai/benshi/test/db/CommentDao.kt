package ai.benshi.test.db

import ai.benshi.test.model.Comment
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CommentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(comments: List<Comment>)

    @Query("DELETE FROM comments WHERE postId = :postId")
    suspend fun deleteAllById(postId: Int)

    @Query("DELETE FROM comments")
    suspend fun deleteAll()

    @Query("SELECT * FROM comments WHERE postId = :postId")
    fun getCommentsByPostId(postId: Int): Flow<List<Comment>>
}
