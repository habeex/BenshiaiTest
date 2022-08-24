package ai.benshi.test.viewmodel

import ai.benshi.test.model.*
import ai.benshi.test.repository.Repository
import androidx.lifecycle.*
import androidx.paging.cachedIn
import java.util.*

class MainViewModel(private val repository: Repository) : ViewModel() {
    var selectedPost = MutableLiveData<Post>()
        // cachedIn() shares the paging state across multiple consumers of posts,
        // e.g. different generations of UI across rotation config change
    val getPosts = repository.posts(10)
            .cachedIn(viewModelScope)

    fun getCommentsByPostId(postId: Int): LiveData<List<Comment>> {
        return repository.getCommentsByPostId(postId).asLiveData()
    }

    suspend fun logUserAction(postId: Int, action: String, email: String, deviceId: String,) {
        val currentTime: Date = Calendar.getInstance().time
        val userAction = UserActions(
            id = 0,
            appId = email,
            action = action,
            resourceId = postId,
            userId = deviceId,
            meta = Meta(timestamp = currentTime.time, location = Location(
                lat = "",
                lng = ""
            )
            )
        )
        repository.saveUserAction(userAction)
    }
}