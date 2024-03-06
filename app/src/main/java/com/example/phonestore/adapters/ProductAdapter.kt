package com.example.phonestore.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.phonestore.utils.GlideApp
import com.example.phonestore.R
import com.example.phonestore.data.Product
import com.example.phonestore.databinding.ItemListProductBinding

class ProductAdapter(val context: Context): ListAdapter<Product, ProductAdapter.Holder>(Comparator()){

    class Holder(view: View): RecyclerView.ViewHolder(view){
        private val binding = ItemListProductBinding.bind(view)

        fun bind(product: Product, context: Context) = with(binding){
            tvTitle.text = product.title
            tvPrice.text = product.price.toString()
            tvDescription.text = product.description

            GlideApp.with(context)
                .load(product.thumbnail)
                .into(ivThumbnail)
        }
    }

    class Comparator: DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_product, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position), context)
    }
}