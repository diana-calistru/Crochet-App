package com.example.crochetapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ViewHolder(patternView: View) : RecyclerView.ViewHolder(patternView) {
    var imgIV: ImageView = patternView.findViewById(R.id.pattern_image)
    var nameTV: TextView = patternView.findViewById(R.id.pattern_name)
}

class CrochetPatternRVAdapter(private var context: Context?,
    var patterns: ArrayList<CrochetPattern>,
    private val recyclerViewInterface: RecyclerViewInterface) : RecyclerView.Adapter<ViewHolder?>() {

    // ViewHolder class for binding views


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rootView: View = LayoutInflater.from(parent.context).inflate(R.layout.card_view_design, parent, false)
        return ViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pattern = patterns[position]

        holder.imgIV.setImageURI(pattern.imageUri)
        holder.nameTV.text = pattern.name

        // Item click
        holder.itemView.setOnClickListener {
            recyclerViewInterface.onItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return patterns.size
    }

    // Update the patterns in the adapter
    fun updatePatterns(newPatterns: List<CrochetPattern>) {
        patterns.clear()
        patterns.addAll(newPatterns)
        notifyDataSetChanged()
    }
}
