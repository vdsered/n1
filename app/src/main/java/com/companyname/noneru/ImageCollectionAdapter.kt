package com.companyname.noneru

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.companyname.noneru.ImageCollectionAdapter.ImageViewHolder

class ImageCollectionAdapter(
    private val activity: AppCompatActivity,
    private val images: List<String>
) : RecyclerView.Adapter<ImageViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(activity)
    var clickListener: OnClickListener = OnClickListener {  }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val attachToRoot = false
        val view = inflater.inflate(R.layout.offer_image_layout, parent, attachToRoot)
        return ImageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (images.isNotEmpty())  images.size else 1
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        if (images.isEmpty())
        {
            holder.bind(activity, R.drawable.placeholder)
            return
        }

        val image = images.get(position)
        holder.clickListener = clickListener
        holder.bind(activity, image)
    }

    class ImageViewHolder(val item: View): RecyclerView.ViewHolder(item) {
        var clickListener: OnClickListener = OnClickListener {  }

        val imageView = item as ImageView
        fun bind(activity: Activity, image: String) {
            Glide
                .with(activity)
                .load(image)
                .apply(RequestOptions().placeholder(R.drawable.placeholder))
                .into(imageView)
            imageView.setOnClickListener {
                clickListener.onClick(it)
            }
        }


        fun bind(activity: Activity, drawableId: Int) {
            imageView.setImageDrawable(ContextCompat.getDrawable(activity, drawableId))
        }
    }
}