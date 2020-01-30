package com.example.voicerecorder

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import java.io.File
import java.io.IOException
import java.lang.RuntimeException


class MainActivity : AppCompatActivity() {


    class AppCollection (private val appContext: Context) {

        var mediaPlayer = CustomMediaPlayer()
        private var appPath = this.appContext.filesDir.absolutePath
        private lateinit var mediaRecorder: MediaRecorder
        private lateinit var file: File

        fun setMyMediaRecorder(mediaRecorder: MediaRecorder) {
            this.mediaRecorder = mediaRecorder
        }

        fun getMyMediaRecorder() : MediaRecorder {
            return this.mediaRecorder
        }

        fun setMyFile (workFile: File) {
            this.file = workFile
        }

        fun getFileInst(): File {
            val fileName = getRecordingDir(this.appContext.filesDir.absolutePath) + File.separator + getDate() + ".mp3"
            return File(fileName)
        }
    }

//    private lateinit var popupWindow: PopupWindow
//    private lateinit var mediaPlayer: CustomMediaPlayer
    lateinit var appContext: Context
    lateinit var appCollection: AppCollection


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.appContext = applicationContext
        this.appCollection = AppCollection(this.appContext)

        if(!checkPermissions()){
            getPermissions()
        }

        initializeButtons()

        initializeIntents()
    }

    private fun initializeIntents() {
        val intent = Intent(this, ListActivity::class.java)

        val constraintLayout = findViewById<View>(R.id.constraint_layout_id)
        constraintLayout.setOnTouchListener(object : OnSwipeTouchListener() {
            override fun onSwipeLeft() {
                Log.e("ViewSwipe", "Left")
                startActivity(intent)
            }
        })
    }

    private fun initializeButtons() {

        val buttonList = findViewById<ImageButton>(R.id.view_my_list_button)
        val buttonStartRecord = findViewById<ImageButton>(R.id.start_record)
        val buttonStopRecord = findViewById<ImageButton>(R.id.stop_record)
        val buttonPlay = findViewById<ImageButton>(R.id.play_record)

        buttonList.setOnClickListener {
            val intent = Intent(this, ListActivity::class.java)
            startActivity(intent)
        }

        buttonStartRecord.setOnClickListener{
            if(checkPermissions()){
                val file = this.appCollection.getFileInst()
                startRecord(file, MediaRecorder())
                buttonStopRecord.isEnabled = true
                buttonStopRecord.isVisible = true

                buttonStartRecord.isEnabled = false
                buttonStartRecord.isVisible = false
            }else{
                Toast.makeText(this, "Permissions required!", Toast.LENGTH_SHORT).show()
            }
        }

        buttonStopRecord.setOnClickListener{
            stopRecording()

            buttonStopRecord.isEnabled = false
            buttonStopRecord.isVisible = false

            buttonStartRecord.isEnabled = true
            buttonStartRecord.isVisible = true
        }

        buttonPlay.setOnClickListener{
//            startPlay(getLastRecord(appContext).path)
//            startPlay(getLastRecord(appContext).path, simpleSeekBar)
//            this.appCollection.mediaPlayer.startPlay(getLastRecord(appContext), it, this)
            this.appCollection.mediaPlayer.startPlay(
                getLastRecord(appContext.filesDir.absolutePath), it, this
            )
        }

    }


    private fun checkPermissions(): Boolean {

        return (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
    }

    private fun getPermissions() {
        val permissions = arrayOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        ActivityCompat.requestPermissions(this, permissions,0)
    }

    fun startRecord(file: File, mediaRecorder: MediaRecorder) {

        try {
            file.createNewFile()

        } catch (e: IOException) {
            error("\n\nIOException in fun startRecord()\n" + e)
        }

        try {
            appCollection.setMyMediaRecorder(mediaRecorder)
            appCollection.getMyMediaRecorder().setAudioSource(MediaRecorder.AudioSource.MIC)
            appCollection.getMyMediaRecorder().setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            appCollection.getMyMediaRecorder().setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            appCollection.getMyMediaRecorder().setOutputFile(file.absolutePath)
            appCollection.getMyMediaRecorder().prepare()
            appCollection.getMyMediaRecorder().start()

        } catch (e: IllegalStateException) {
            e.printStackTrace()
            error(e)
        } catch (e: IOException) {
            e.printStackTrace()
            error(e)
        }

        runOnUiThread {
            Toast.makeText(this, "Recording started!", Toast.LENGTH_SHORT).show()
        }
    }

    fun stopRecording() {

        try {
            appCollection.getMyMediaRecorder().stop()
//            appCollection.getMyMediaRecorder()?.reset()  // clean the media
            appCollection.getMyMediaRecorder().release()   // delete the media
        }
        catch (e: RuntimeException) {
            println("RuntimeException in stopRecording()")
        }

        runOnUiThread {
            Toast.makeText(this, "Record saved!", Toast.LENGTH_SHORT).show()
        }
    }

}
