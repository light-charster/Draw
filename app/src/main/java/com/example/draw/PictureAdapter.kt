package com.example.draw

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class PictureAdapter (private val pictureList: List<Results>):RecyclerView.Adapter<PictureAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image :MyImageView = view.findViewById(R.id.image)
        val text :TextView =view.findViewById(R.id.text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.picture_item,parent,false)
        val viewHolder = ViewHolder(view)
        viewHolder.image.setOnClickListener{
            val position = viewHolder.adapterPosition
            val pict = pictureList[position]
            val picture = pict.url
            val intent = Intent(parent.context, DrawActivity::class.java)
            intent.putExtra("P",picture)
            parent.context.startActivity(intent)
        }

        return viewHolder
    }

    override fun getItemCount(): Int =pictureList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val picture =pictureList[position]
        holder.image.setImageURL(picture.url)
        holder.text.text = picture.name
    }
}