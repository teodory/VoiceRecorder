package com.example.voicerecorder

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.sql.DriverManager.println
import java.util.*
import androidx.core.content.FileProvider.getUriForFile



class CustomAdapter(var records: ArrayList<Record>, context: Context) : RecyclerView.Adapter<CustomAdapter.ViewHolder>(){

    private val appContext = context

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

        holder.shareButton.setOnClickListener{
            println(File(record.path).exists())

            val shareIntent = Intent()
            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.type = "audio/*"

            val recordPath = File(appContext.filesDir, "records")
            println(recordPath)
//            val newFile = File(imagePath, "default_image.jpg")
            val newFile = File(record.path)
            println("" + newFile.exists() + "   " + newFile)
            val contentUri = getUriForFile(appContext, "com.example.voicerecorder.myfileprovider", newFile)


            shareIntent.putExtra(Intent.EXTRA_STREAM,  contentUri)
//            shareIntent.putExtra(Intent.EXTRA_TEXT,  "Kyp")

            println(shareIntent)
            println(shareIntent)
            println(shareIntent)

            appContext.startActivity(Intent.createChooser(shareIntent, "Share Record"))
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
//            appContext.startActivity(Intent.createChooser(share, "Share Sound File")) //Start the intent
//            val p ="/data/user/0/com.example.voicerecorder/cache/MyRecords/2019_06_30-08h34m20s.mp3"
//            val p ="/data/user/0/com.example.voicerecorder/cache/2019_06_30-08h34m20s.mp3"
//            println(File(p).exists())


//            val intent = Intent()
//            intent.action = Intent.ACTION_SEND
//            intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(record.path))
//            intent.type = "audio/*"
//            appContext.startActivity(Intent.createChooser(intent, "Share File"))

        }
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val title = itemView.findViewById<TextView>(R.id.list_title)
        val playButton = itemView.findViewById<ImageButton>(R.id.myPlayRecord)
        val stopButton = itemView.findViewById<ImageButton>(R.id.myStropPlayRec)
        val shareButton = itemView.findViewById<ImageButton>(R.id.myShareButton)
        val deleteButton = itemView.findViewById<ImageButton>(R.id.myDeleteButton)
    }
}