package com.example.voicerecorder

import android.annotation.SuppressLint
import android.app.Activity
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
import android.widget.Toast
import java.sql.DriverManager


class CustomAdapter(var records: ArrayList<Record>, context: Context, private val selfActivity: Activity) : RecyclerView.Adapter<CustomAdapter.ViewHolder>(){

    private val appContext = context
    private val mediaPlayer = CustomMediaPlayer()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(viewHolder)
    }

    override fun getItemCount(): Int {
        return records.size
    }

    @SuppressLint("ResourceType")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val record : Record = records[position]
        holder.title.text = record.name

        holder.playButton.setOnClickListener{
            println("\nStart player: " + record.path)
            mediaPlayer.startPlay(record, it, selfActivity)
        }

        holder.stopButton.setOnClickListener{
            println("STOP PLAYER")
            mediaPlayer.stopPlayer()
        }

        holder.myPopupMenu.setOnClickListener{
            val popupMenu = PopupMenu(appContext, holder.myPopupMenu)

            popupMenu.menuInflater.inflate(R.layout.record_menu, popupMenu.menu)
            setForceShowIcon(popupMenu)

            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->

                when(item.itemId) {
                    R.id.myShareButton -> shareRecord(record)
                    R.id.myDeleteButton -> deleteRecord(record, position)
                }
                true
            })

            popupMenu.show()

        }
    }

    private fun shareRecord(record: Record) {
        val newFile = File(record.path)
        val contentUri = getUriForFile(appContext, "com.example.voicerecorder.myfileprovider", newFile)
        val shareIntent = Intent.createChooser(Intent(), "Share Record")

        shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type = "audio/*"
        shareIntent.putExtra(Intent.EXTRA_STREAM,  contentUri)

        appContext.startActivity(shareIntent)
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val title = itemView.findViewById<TextView>(R.id.list_title)
        val playButton = itemView.findViewById<ImageButton>(R.id.myPlayRecord)
        val stopButton = itemView.findViewById<ImageButton>(R.id.myStropPlayRec)
        val myPopupMenu = itemView.findViewById<ImageButton>(R.id.myPopupMenu)
    }

    fun deleteRecord(record: Record, position: Int) {
        val msg = " >>>> " + record.name + " IND: " + position
        Toast.makeText(appContext, msg, Toast.LENGTH_SHORT).show()

        if (deleteFile(record.path)) {

            notifyItemRemoved(position)
            notifyItemRangeChanged(position, getRecordsCount(appContext.filesDir.absolutePath))
            records.removeAt(position)
            println(" >>> " + record.name + " Deleted!!!")
        }
        else {
            Toast.makeText(appContext, "File Not Found!", Toast.LENGTH_SHORT).show()
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