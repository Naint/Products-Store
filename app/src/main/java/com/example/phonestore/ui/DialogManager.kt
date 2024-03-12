package com.example.phonestore.ui

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import com.example.phonestore.R
import com.example.phonestore.data.Product
import com.example.phonestore.utils.GlideApp

class DialogManager {

    companion object{
        fun showProductDialog(context: Context, product: Product){
            val builder = AlertDialog.Builder(context)
            val customView = LayoutInflater.from(context).inflate(R.layout.full_product_dialog, null)
            builder.setView(customView)

            val ivImage = customView.findViewById<ImageView>(R.id.ivProduct)
            val tvTitle = customView.findViewById<TextView>(R.id.tvTitle)
            val tvPrice = customView.findViewById<TextView>(R.id.tvPrice)
            val tvRating = customView.findViewById<TextView>(R.id.tvRating)
            val tvDescription = customView.findViewById<TextView>(R.id.tvDescription)

            tvTitle.text = product.title
            tvPrice.text = "${product.price} ₽"
            tvRating.text = "${product.rating}⭐"
            tvDescription.text = product.description

            GlideApp.with(context)
                .load(product.thumbnail)
                .into(ivImage)

            val dialog = builder.create()
            dialog.show()

        }
    }

}