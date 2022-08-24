package ai.benshi.test.adapter

import ai.benshi.test.model.Comment
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ai.benshi.test.databinding.CommentItemLayoutBinding

class CommentAdapter : RecyclerView.Adapter<CommentAdapter.MyViewHolder>() {

    private var comments = emptyList<Comment>()

    class MyViewHolder(val binding: CommentItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            CommentItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val  comment = comments[position]
        holder.binding.commentAuthorNameTxt.text = "${comment.name} (${comment.email})"
        holder.binding.commentBodyTxt.text = comment.body
    }

    override fun getItemCount(): Int {
        return comments.size
    }

    fun setData(newData: List<Comment>){
        comments = newData
        notifyDataSetChanged()
    }

}