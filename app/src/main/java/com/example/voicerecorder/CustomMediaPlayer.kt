package com.example.voicerecorder

import android.media.MediaPlayer
import java.io.File
import java.io.IOException

private var player = MediaPlayer()
private lateinit var file: File


fun startPlay(path : String) {
//    player = MediaPlayer ()

//        player?.setOnCompletionListener (this)

    player.reset()
    println(path)
    println(path)

    try {
//        file = File(getRecordingDir() + File.separator + getDate() + ".mp3")
//        file = File(getRecordingDir() + "test" + getDate() + ".mp3")
        file = File(path)
        player.setDataSource(file.absolutePath)
    } catch (e: IOException) {
    }

    try {
        player.prepare ()
    } catch (e: IOException) {

        println(e)
    }

    player.start()

//        tv1.text = "Ready to play"
}