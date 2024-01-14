package com.example.draw

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class PencilAdapter(private val context: Context, private val paint: Paint) :
    RecyclerView.Adapter<PencilAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.grid_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageView = holder.itemView.findViewById<ImageView>(R.id.images)
        val imageResource = "pencil_" + (position + 1)
        val imageId =
            context.resources.getIdentifier(imageResource, "drawable", context.packageName)
        imageView.setOnClickListener { view: View? ->
            when (position) {
                0 -> {
                    paint.color = Color.rgb(216, 30, 6)
                }

                1 -> {
                    paint.color = Color.rgb(212, 35, 122)
                }

                2 -> {
                    paint.color = Color.rgb(244, 234, 42)
                }

                3 -> {
                    paint.color = Color.rgb(26, 250, 41)
                }

                4 -> {
                    paint.color = Color.rgb(18, 150, 219)
                }

                5 -> {

                    paint.color = Color.rgb(19, 34, 122)
                }
            }
        }
        imageView.setImageResource(imageId)
    }

    override fun getItemCount(): Int {
        return 6
    }

    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!)
}
