package com.example.voicerecorder

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.IOException
import java.io.File


class MainActivity : AppCompatActivity() {

    private var output: String? = null
    private var mediaRecorder: MediaRecorder? = null
    private var player: MediaPlayer? = null
    private lateinit var file: File
//    private var state: Boolean = false
//    private var recordingStopped: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var buttonList = findViewById<Button>(R.id.view_my_list_button)
        var buttonStopRecord = findViewById<Button>(R.id.stop_record)
        var buttonStartRecord = findViewById<Button>(R.id.start_record)
        var buttonPlay = findViewById<Button>(R.id.play_record)
        var buttonStopPlayRec = findViewById<Button>(R.id.stop_play_record)

        buttonList.setOnClickListener {
            val intent = Intent(this, ListActivity::class.java)
            startActivity(intent)
        }

        buttonStartRecord.setOnClickListener{
            if(!checkPermisions()){
                startRecord()
                buttonStopRecord.setEnabled(true)
            }
        }

        buttonStopRecord.setOnClickListener{
            stopRecording()
            buttonStopRecord.setEnabled(false)
        }

        buttonPlay.setOnClickListener{
            startPlay()
        }

        buttonStopPlayRec.setOnClickListener{
            player?.stop()
        }

        if(!checkPermisions()){
            getPermisions()
        }

//        val path = System.getProperty("user.dir")
//        println(path)
//        testFileWriting()
//        testReadingFiles()
    }

    private fun checkPermisions(): Boolean {

        return (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)

    }

    private fun getPermisions() {
        val permissions = arrayOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        ActivityCompat.requestPermissions(this, permissions,0)
    }

    private fun startRecord() {

        try {
            file = File(getRecordingDir() + File.separator + getDate() + ".mp3")
//            file = File(getRecordingDir() + File.separator + "test" + ".mp3")
            file.createNewFile()

        } catch (e: IOException) {
            println("\n\nException in fun startRecord()\n" + e)
        }

        mediaRecorder = MediaRecorder()
        mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        mediaRecorder?.setOutputFile(file.absolutePath)
        try {
            mediaRecorder?.prepare()
            mediaRecorder?.start()
//            state = true
            Toast.makeText(this, "Recording started!", Toast.LENGTH_SHORT).show()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun stopRecording() {
        mediaRecorder?.stop()
        mediaRecorder?.release()
        Toast.makeText(this, "Record saved!", Toast.LENGTH_SHORT).show()
//        if(state){
//            state = false
//        }else{
//            Toast.makeText(this, "You are not recording right now!", Toast.LENGTH_SHORT).show()
//        }
    }

    private fun startPlay() {
        //TODO: play last record

        player = MediaPlayer ()

//        player?.setOnCompletionListener (this)

        try {
//            file = File(getRecordingDir() + File.separator + getDate() + ".mp3")
            file = File(getRecordingDir() + File.separator + "test" + ".mp3")
            player?.setDataSource(file.absolutePath)
        } catch (e: IOException) {
            println(e)
        }

        try {
            player?.prepare ()
        } catch (e: IOException) {
            println(e)
        }

        player?.start()

    }

    private fun setupMediaRecorder() {

        mediaRecorder = MediaRecorder()

        mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        mediaRecorder?.setOutputFile(getDate() + ".mp3")

    }

}
