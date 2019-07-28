package com.example.voicerecorder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


//class CustomAdapter(var movies: List<ListActivity.Movie>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>(){
class CustomAdapter(var records: List<Record>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var vh = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(vh)
    }

    override fun getItemCount(): Int {
        return records.size
    }

    override fun onBindViewHolder(holder: CustomAdapter.ViewHolder, position: Int) {
        val record : Record = records[position]
        holder.title.text = record.title
//        holder.description.text = movie.year.toString()
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val title = itemView.findViewById<TextView>(R.id.list_title)
//        val description = itemView.findViewById<TextView>(R.id.list_description)
    }
}