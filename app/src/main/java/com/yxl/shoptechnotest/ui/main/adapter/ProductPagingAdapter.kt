package com.yxl.shoptechnotest.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import com.yxl.shoptechnotest.R
import com.yxl.shoptechnotest.data.model.Product
import com.yxl.shoptechnotest.databinding.ItemProductBinding

class ProductPagingAdapter(private val listener: OnItemClickListener) :
    PagingDataAdapter<Product, ProductPagingAdapter.ProductViewHolder>(ProductDiffCallBack()) {

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bind(item)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemProductBinding.inflate(inflater)
        return ProductViewHolder(binding)
    }

    inner class ProductViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) = with(binding) {
            tvDescription.text = product.description
            tvTitle.text = product.title
            ivProduct.load(product.thumbnail) {
                crossfade(true)
                placeholder(R.drawable.ic_image)
                scale(Scale.FIT)
            }

            itemView.setOnClickListener { listener.onItemClick(product.id) }
        }
    }

    class ProductDiffCallBack : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: Product,
            newItem: Product
        ) = oldItem == newItem

    }

    interface OnItemClickListener {
        fun onItemClick(productId: Int)
    }
}