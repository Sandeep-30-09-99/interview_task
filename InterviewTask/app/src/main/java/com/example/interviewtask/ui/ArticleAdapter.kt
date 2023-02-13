package com.example.interviewtask.ui

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View

import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.interviewtask.R
import com.example.interviewtask.databinding.ListViewBinding
import com.example.interviewtask.model.Article
import com.example.interviewtask.ui.article.ArticleActivity

class ArticleAdapter(val context: Context, val listener: AdapterCallback) :
    RecyclerView.Adapter<ArticleAdapter.Holder>() {

    private var list = ArrayList<Article>()


    class Holder(binding: ListViewBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: ListViewBinding

        init {
            this.binding = binding
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = DataBindingUtil.inflate<ListViewBinding>(
            LayoutInflater.from(parent.context), R.layout.list_view, parent, false
        )

        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        Log.i("dsfsf", ArticleActivity.localDataLoaded.toString())
        holder.binding.ivDelete.visibility =
            if (ArticleActivity.localDataLoaded) View.VISIBLE else View.GONE
        holder.binding.bean = list[position]
        holder.binding.pos = position
        holder.binding.callback = listener
    }

    fun getList(): ArrayList<Article> {
        return this.list
    }


    @SuppressLint("NotifyDataSetChanged")
    fun setList(data: ArrayList<Article>) {
        this.list = data
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun deletePos(pos: Int) {
        if (list.isNotEmpty() && list.size > pos) {
            this.list.removeAt(pos)
            notifyItemRemoved(pos)
        }

    }
}