package ai.benshi.test.api

import ai.benshi.test.model.Comment
import ai.benshi.test.model.Post
import ai.benshi.test.model.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("posts")
    suspend fun getPosts() : Response<List<Post>>

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") number: Int) : Response<User>

    @GET("posts/{postId}/comments")
    suspend fun getComments(@Path("postId") number: Int) : Response<List<Comment>>

}