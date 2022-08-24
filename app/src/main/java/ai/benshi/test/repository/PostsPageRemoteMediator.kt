package ai.benshi.test.repository

import ai.benshi.test.api.ApiService
import ai.benshi.test.db.CommentDao
import ai.benshi.test.db.PostDao
import ai.benshi.test.db.RoomDb
import ai.benshi.test.db.UserDao
import ai.benshi.test.model.Post
import ai.benshi.test.model.PostUser
import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.LoadType.*
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class PostsPageRemoteMediator(
    private val db: RoomDb,
    private val apiService: ApiService,
) : RemoteMediator<Int, Post>() {
    private val postDao: PostDao = db.postsDao()
    private val userDao: UserDao = db.userDao()
    private val commentDao: CommentDao = db.commentDao()

    override suspend fun initialize(): InitializeAction {
        // Require that remote REFRESH is launched on initial load and succeeds before launching
        // remote PREPEND / APPEND.
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Post>
    ): MediatorResult {
        try {
            val loadKey = when (loadType) {
                REFRESH -> null
                PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                APPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
            }

            val data = apiService.getPosts()
            val items  = data.body()?.map {it}

            items?.forEach {
                db.withTransaction {
                    val isUserExist = userDao.isUserIsExist(it.userId)
                    if(!isUserExist){
                        val  userResponse = apiService.getUserById(it.userId)
                        if(userResponse.isSuccessful) {
                            userResponse.body()?.let {
                                    it1 -> userDao.insert(it1)
                                it.user = PostUser(
                                    name = it1.name,
                                    username = it1.username,
                                    address = it1.address,
                                    email = it1.email,
                                    phone = it1.phone,
                                    website = it1.website,
                                )
                            }
                        }
                    }else {
                        val user = userDao.getUser(it.userId)
                        it.user = PostUser(
                            name = user.name,
                            username = user.username,
                            address = user.address,
                            email = user.email,
                            phone = user.phone,
                            website = user.website,
                        )
                    }
                    postDao.insert(it)
                }
            }

            items?.forEach {
                db.withTransaction {
                    val  commentsResponse = apiService.getComments(it.id)
                    if (commentsResponse.isSuccessful){
                        commentDao.deleteAllById(it.id)
                        commentsResponse.body()?.let {
                                it1 -> commentDao.insertAll(it1)
                        }
                    }
                }
            }

            return MediatorResult.Success(endOfPaginationReached = items!!.isEmpty())
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }
}
