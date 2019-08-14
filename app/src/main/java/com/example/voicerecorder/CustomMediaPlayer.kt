package com.example.voicerecorder

import android.media.MediaPlayer
import android.widget.SeekBar
import java.io.File
import java.io.IOException


private var tmp_player = MediaPlayer()
private lateinit var file: File


class CustomMediaPlayer(private var seekBar: SeekBar) : Runnable {
    private var player = MediaPlayer()

    override fun run() {
        var currentPosition = player.currentPosition
        val total = player.duration


        while (player.isPlaying && currentPosition < total) {
            try {
                Thread.sleep(500)
                currentPosition = player.currentPosition

            } catch (e: InterruptedException) {
                return
            } catch (e: Exception) {
                return
            }

            this.seekBar.progress = currentPosition
        }
    }

    fun startPlay(path: String){
        this.player.reset()

        try {
            file = File(path)
            player.setDataSource(file.path)
            player.prepare()

        }
        catch (e: IOException){
            println(e)
        }

        player.start()

        this.seekBar.progress = 0
        this.seekBar.max = player.duration

        Thread(this).start()

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                if (b) {
                    player.seekTo(i)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

                if (player.isPlaying) {
                    player.seekTo(seekBar.progress)
                }
                println("!IN onStartTrackingTouch")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
//            Toast.makeText(applicationContext,"!IN onStopTrackingTouch", Toast.LENGTH_SHORT).show()
                println("!IN onStopTrackingTouch")
            }
        })

    }

    fun stopPlayer(){
        player.stop()
    }
}



//    simpleSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
//
//        override fun onProgressChanged(sb: SeekBar, progress: Int, b: Boolean) {
//            // Display the current progress of SeekBar
////                            text_view.text = "Progress : $i"
//
//            val percent = progress / sb.getMax().toDouble()
//            val offset = sb.getThumbOffset()
//            val seekWidth = sb.getWidth()
//            val v = Math.round(percent * (seekWidth - 2 * offset)).toInt()
//            println("================")
//            println(v)
//
//            if (progress > 0 && player != null && !player.isPlaying()) {
//                clearMediaPlayer()
//                simpleSeekBar.progress = 0
//            }
//
//        }
//
//        override fun onStartTrackingTouch(sb: SeekBar?) {
//            // Do something
////            Toast.makeText(applicationContext,"start tracking",Toast.LENGTH_SHORT).show()
//            if (player != null && player.isPlaying()) {
//                if (sb != null) {
//                    println("*****"+ sb.progress)
//                    player.seekTo(sb.progress)
//                }
//            }
//        }
//
//        override fun onStopTrackingTouch(sb: SeekBar?) {
//            // Do something
////            Toast.makeText(applicationContext,"stop tracking",Toast.LENGTH_SHORT).show()
//        }
//
//    })



//private fun clearMediaPlayer() {
//    player.stop()
//    player.release()
//}


//TODO: delete
fun startPlay(path: String) {

    tmp_player.reset()

    try {
        file = File(path)
        tmp_player.setDataSource(file.absolutePath)
    } catch (e: IOException) {
        println(e)
    }

    try {
        tmp_player.prepare ()
    } catch (e: IOException) {

        println(e)
    }

    tmp_player.start()
}

fun stopPlayer(){
    tmp_player.stop()
}