package com.example.voicerecorder

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.IOException
import java.io.File


class MainActivity : AppCompatActivity() {

    private var mediaRecorder: MediaRecorder? = null
    private lateinit var file: File
    private lateinit var appContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.appContext = applicationContext

        if(!checkPermisions()){
            getPermisions()
        }

        val buttonList = findViewById<Button>(R.id.view_my_list_button)
        val buttonStopRecord = findViewById<Button>(R.id.stop_record)
        val buttonStartRecord = findViewById<Button>(R.id.start_record)
        val buttonPlay = findViewById<Button>(R.id.play_record)
        val buttonStopPlayRec = findViewById<Button>(R.id.stop_play_record)

        buttonList.setOnClickListener {
            val intent = Intent(this, ListActivity::class.java)
            startActivity(intent)
        }

        buttonStartRecord.setOnClickListener{
            if(checkPermisions()){
                startRecord()
                buttonStopRecord.setEnabled(true)
            }else{
                Toast.makeText(this, "Permissions required!", Toast.LENGTH_SHORT).show()
            }
        }

        buttonStopRecord.setOnClickListener{
            stopRecording()
            buttonStopRecord.setEnabled(false)
        }

        buttonPlay.setOnClickListener{

            startPlay(getLastRecord(appContext).path)
        }

        buttonStopPlayRec.setOnClickListener{
            stopPlayer()
        }


//        val path = System.getProperty("user.dir")
//        println(path)
//        testFileWriting()
//        testReadingFiles()
    }

    private fun checkPermisions(): Boolean {

        return (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
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
            file = File(getRecordingDir(appContext) + File.separator + getDate() + ".mp3")
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
    }

}
