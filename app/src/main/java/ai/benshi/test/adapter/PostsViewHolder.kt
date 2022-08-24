package ai.benshi.test.adapter

import ai.benshi.test.R
import ai.benshi.test.databinding.PostItemLayoutBinding
import ai.benshi.test.model.Post
import ai.benshi.test.utils.Constants.Companion.IMAGE_BASE_URL
import ai.benshi.test.utils.Utils.Companion.sha256
import ai.benshi.test.viewmodel.MainViewModel
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation


class PostsViewHolder(private val binding: PostItemLayoutBinding, private val mainViewModel: MainViewModel, private val clickListener: ClickListener)
    : RecyclerView.ViewHolder(binding.root) {
    private var post : Post? = null
    init {
        binding.root.setOnClickListener {
            mainViewModel.selectedPost.value = post
            findNavController(binding.root).navigate(R.id.action_homeFragment_to_postDetailFragment)
            clickListener.onItemClicked(post!!)
        }
    }

    fun bind(post: Post?) {
        this.post = post
        binding.titleTextView.text = post?.title?.trim()
        binding.bodyTextView.text = post?.body?.trim()
        binding.postImage.load("$IMAGE_BASE_URL/${post?.title?.sha256()}/400/400"){
            crossfade(true)
            placeholder(R.drawable.ic_baseline_image_24)
            transformations(CircleCropTransformation())
        }
        binding.userNameTxt.text = "${post!!.user?.name} (@${post!!.user?.username})"
    }


    companion object {
        fun create(parent: ViewGroup, mainViewModel: MainViewModel, clickListener: ClickListener): PostsViewHolder {
            val view = PostItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return PostsViewHolder(view, mainViewModel, clickListener)
        }
    }

    fun updatePost(item: Post?) {
        post = item
    }

    interface ClickListener {
        fun onItemClicked(post: Post)
    }
}