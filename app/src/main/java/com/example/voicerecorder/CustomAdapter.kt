package com.example.voicerecorder

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class CustomAdapter(var records: ArrayList<Record>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vh = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(vh)
    }

    override fun getItemCount(): Int {
        return records.size
    }

    override fun onBindViewHolder(holder: CustomAdapter. ViewHolder, position: Int) {
        val record : Record = records[position]
        holder.title.text = record.name

        holder.playButton.setOnClickListener{
            startPlay(record.path)
        }

        holder.stopButton.setOnClickListener{
            stopPlayer()
        }

        holder.deleteButton.setOnClickListener{

            if (deleteRecord(record.path)){
                records.removeAt(position)
                notifyItemRemoved(position)
            }

//            val uri = Uri.parse(record.path) //Identifier of the audio file (Uniform Resource Identifier)
//            val share = Intent(Intent.ACTION_SEND) //Create a new action_send intent
//            share.type = "audio/*" //What kind of file the intent gets
//            share.putExtra(Intent.EXTRA_STREAM, uri) //Pass the audio file to the intent
//            startActivity(Intent.createChooser(share, "Share Sound File")) //Start the intent

//            val intent = Intent()
//            intent.action = Intent.ACTION_SEND
//            intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(record.path))
//            intent.type = "audio/*"
//
//            startActivity(Intent.createChooser(intent, "Share File"))
//

        }
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val title = itemView.findViewById<TextView>(R.id.list_title)
        val playButton = itemView.findViewById<ImageButton>(R.id.myPlayRecord)
        val stopButton = itemView.findViewById<ImageButton>(R.id.myStropPlayRec)
        val deleteButton = itemView.findViewById<ImageButton>(R.id.myDeleteButton)
    }
}