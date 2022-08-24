package ai.benshi.test.adapter

import ai.benshi.test.model.Post
import ai.benshi.test.viewmodel.MainViewModel
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil

/**
 * A simple adapter implementation that shows posts.
 */
class PostsAdapter(private  val mainViewModel: MainViewModel, private val clickListener: PostsViewHolder.ClickListener)
    : PagingDataAdapter<Post, PostsViewHolder>(POST_COMPARATOR) {

    override fun onBindViewHolder(holder: PostsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onBindViewHolder(
        holder: PostsViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {
            val item = getItem(position)
            holder.updatePost(item)
        } else {
            onBindViewHolder(holder, position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsViewHolder {
        return PostsViewHolder.create(parent, mainViewModel, clickListener)
    }

    companion object {
        private val PAYLOAD_SCORE = Any()
        val POST_COMPARATOR = object : DiffUtil.ItemCallback<Post>() {
            override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean =
                oldItem.id == newItem.id

            override fun getChangePayload(oldItem: Post, newItem: Post): Any? {
                return if (sameExceptScore(oldItem, newItem)) {
                    PAYLOAD_SCORE
                } else {
                    null
                }
            }
        }

        private fun sameExceptScore(oldItem: Post, newItem: Post): Boolean {
            // DON'T do this copy in a real app, it is just convenient here for the demo :)
            // because randomizes post, we want to pass it as a payload to minimize
            // UI updates between refreshes
            return oldItem.copy(id = newItem.id) == newItem
        }
    }
}
