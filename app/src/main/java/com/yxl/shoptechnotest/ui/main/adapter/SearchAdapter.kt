package com.yxl.shoptechnotest.ui.main.adapter

import android.view.LayoutInflater
import com.yxl.shoptechnotest.data.model.Product
import com.yxl.shoptechnotest.databinding.ItemProductBinding
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import com.yxl.shoptechnotest.R

class SearchAdapter : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) = with(binding){
            tvDescription.text = product.description
            tvTitle.text = product.title
            ivProduct.load(product.thumbnail) {
                crossfade(true)
                placeholder(R.drawable.ic_image)
                scale(Scale.FIT)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemProductBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun getItemCount() = if(differ.currentList.size > 0) differ.currentList.size else 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (differ.currentList.isNotEmpty()) {
            holder.bind(differ.currentList[position])
        }
    }

    private val differCallBack = object : DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }
     val differ = AsyncListDiffer(this, differCallBack)
}