package ai.benshi.test.repository

import ai.benshi.test.api.ApiService
import ai.benshi.test.db.CommentDao
import ai.benshi.test.db.PostDao
import ai.benshi.test.db.RoomDb
import ai.benshi.test.db.UserActionDao
import ai.benshi.test.model.Comment
import ai.benshi.test.model.PostWithComment
import ai.benshi.test.model.UserActions
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow

class RepositoryImpl(val roomDb: RoomDb, val apiService: ApiService) : Repository{
    private val postsDao: PostDao = roomDb.postsDao()
    private val commentDao: CommentDao = roomDb.commentDao()
    private val userActionDao: UserActionDao = roomDb.userActionDao()

    @OptIn(ExperimentalPagingApi::class)
    override fun posts(pageSize: Int) = Pager(
        config = PagingConfig(pageSize),
        remoteMediator = PostsPageRemoteMediator(roomDb, apiService)
    ) {
        roomDb.postsDao().getPosts()
    }.flow

    override suspend fun getPostComments(id: Int): PostWithComment {
        return  postsDao.getPostWithComment(id)
    }

    override fun getCommentsByPostId(postId: Int): Flow<List<Comment>> {
        return commentDao.getCommentsByPostId(postId)
    }

    override suspend fun saveUserAction(userActions: UserActions) {
        roomDb.withTransaction {
            userActionDao.insert(userActions)
        }
    }

    override suspend fun getUserActions(): List<UserActions> {
        return  userActionDao.getUserAction()
    }

    override suspend fun deleteUserActionById(id: Int) {
        roomDb.withTransaction {
            userActionDao.deleteById(id)
        }
    }


}