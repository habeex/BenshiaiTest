package ai.benshi.test.repository

import ai.benshi.test.model.*
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun posts(pageSize: Int): Flow<PagingData<Post>>
    suspend fun getPostComments(id: Int) : PostWithComment
    fun getCommentsByPostId(postId: Int): Flow<List<Comment>>
    suspend fun saveUserAction(userActions: UserActions)
    suspend fun getUserActions(): List<UserActions>
    suspend fun deleteUserActionById(id: Int)
}