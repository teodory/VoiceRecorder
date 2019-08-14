package com.example.voicerecorder

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
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
import android.widget.PopupMenu
import java.sql.DriverManager


class CustomAdapter(var records: ArrayList<Record>, context: Context) : RecyclerView.Adapter<CustomAdapter.ViewHolder>(){

    private val appContext = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vh = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(vh)
    }

    override fun getItemCount(): Int {
        return records.size
    }

    @SuppressLint("ResourceType")
    override fun onBindViewHolder(holder: CustomAdapter. ViewHolder, position: Int) {
        val record : Record = records[position]
        holder.title.text = record.name

        holder.playButton.setOnClickListener{
            println("Start player" + record.path)
            println("Start player" + record.path)
            println("Start player" + record.path)
            startPlay(record.path)
        }

        holder.stopButton.setOnClickListener{
            println("STOP PLAYER")
            stopPlayer()
        }

        holder.myPopupMenu.setOnClickListener{
            val popupMenu = PopupMenu(appContext, holder.myPopupMenu)

            popupMenu.menuInflater.inflate(R.layout.record_menu, popupMenu.menu)
            setForceShowIcon(popupMenu)

            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->

                when(item.itemId) {
                    R.id.myShareButton ->
                        shereRecord(record)
                    R.id.myDeleteButton ->
                        deleteRecord(record, position)
                }
                true
            })

            popupMenu.show()

        }
    }

    private fun shereRecord(record: Record) {

        val shareIntent = Intent()
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type = "audio/*"

        val newFile = File(record.path)
        val contentUri = getUriForFile(appContext, "com.example.voicerecorder.myfileprovider", newFile)

        shareIntent.putExtra(Intent.EXTRA_STREAM,  contentUri)

        appContext.startActivity(Intent.createChooser(shareIntent, "Share Record"))
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val title = itemView.findViewById<TextView>(R.id.list_title)
        val playButton = itemView.findViewById<ImageButton>(R.id.myPlayRecord)
        val stopButton = itemView.findViewById<ImageButton>(R.id.myStropPlayRec)
        val myPopupMenu = itemView.findViewById<ImageButton>(R.id.myPopupMenu)
    }

    private fun deleteRecord(record: Record, position: Int){

        if (deleteRecord(record.path)){
            records.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    private fun setForceShowIcon(popupMenu: PopupMenu) {

        try {
            val fieldMPopup = PopupMenu::class.java.getDeclaredField("mPopup")
            fieldMPopup.isAccessible = true
            val mPopup = fieldMPopup.get(popupMenu)
            mPopup.javaClass
                .getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                .invoke(mPopup, true)
        } catch (e: Exception){
            Log.e("CustomAdapter", "Error showing menu icons.", e)
        }
    }

}