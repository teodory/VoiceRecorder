package com.example.voicerecorder

import android.annotation.SuppressLint
import android.app.Activity
import android.media.MediaPlayer
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.player_popup.*
import java.io.File
import java.io.IOException


private var tmp_player = MediaPlayer()
private lateinit var file: File
//lateinit var popupWindow: PopupWindow

@SuppressLint("Registered")
class CustomMediaPlayer(activity: Activity) : Runnable, AppCompatActivity() {
    private var player = MediaPlayer()
    private lateinit var seekBar: SeekBar
    private lateinit var popupWindow: PopupWindow


//    companion object {
//        val instance = CustomMediaPlayer()
//    }

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

    fun startPlay(record: Record, view: View, selfActivity: Activity){

        if (::seekBar.isInitialized){
            println(seekBar.isActivated)
            println(seekBar.isFocused)
            println(seekBar.progress)
        }
        if (::popupWindow.isInitialized && popupWindow.isShowing && !player.isPlaying){
           popupWindow.dismiss()
        }
//                TODO: not to reload the window after click play

        val playerPopupView = selfActivity.layoutInflater.inflate(R.layout.player_popup, null)
        popupWindow = initialPopupWindow(playerPopupView, selfActivity)
        popupWindow.showAsDropDown(view)
//        setupPopupWindow(playerPopupView, selfActivity)


        val playButton = playerPopupView.findViewById<ImageButton>(R.id.play_record_popup)
        val stopButton = playerPopupView.findViewById<ImageButton>(R.id.stop_play_record_popup)
        this.seekBar = playerPopupView.findViewById<SeekBar>(R.id.simpleSeekBar)

        playButton.setOnClickListener{
            this.startPlay(record, it, selfActivity)
        }

        stopButton.setOnClickListener{
//            popupWindow.dismiss()
            this.stopPlayer()
        }


//        if (!::popupWindow.isInitialized){
////            println("INIT POPUP_WINDOW")
////            popupWindow = initialPopupWindow(playerPopupView, selfActivity)
//            setupPopupWindow(playerPopupView, selfActivity)
//        }


        val recordName = popupWindow.contentView.findViewById<TextView>(R.id.record_title)
        recordName.text = record.name

        this.player.reset()

        try {
            file = File(record.path)
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
                println("!IN onStopTrackingTouch")
                if (!player.isPlaying){
                    seekBar.progress = 0
                }
            }
        })

    }

    fun stopPlayer(){
        player.stop()
    }

    @SuppressLint("InflateParams")
    fun testButton(testButton: Button, selfActivity: Activity) {

        testButton.setOnClickListener{

            val playerView = selfActivity.layoutInflater.inflate(R.layout.player_popup, null)
            popupWindow = PopupWindow(selfActivity)
            popupWindow.animationStyle = R.style.WindowSlideStyle
            popupWindow.contentView = playerView
            popupWindow.isOutsideTouchable = true
            popupWindow.isTouchable = true

            val x = playerView.left + popupWindow.width
            val y = playerView.top + popupWindow.height
            popupWindow.showAtLocation(playerView, Gravity.BOTTOM, x, y)

            setupPopupWindow(playerView, selfActivity)

//            imageView.setOnClickListener{
//                popupWindow.dismiss()
//            }
            popupWindow.showAsDropDown(testButton)
        }
    }

    private fun setupPopupWindow(popupView: View, selfActivity: Activity) {

//        val nameText = popupView.findViewById<TextView>(R.id.record_title)
        val playButton = popupView.findViewById<ImageButton>(R.id.play_record_popup)
        val stopButton = popupView.findViewById<ImageButton>(R.id.stop_play_record_popup)
        val simpleSeekBar = popupView.findViewById<SeekBar>(R.id.simpleSeekBar)
        seekBar = simpleSeekBar

        println()
        println(simpleSeekBar)
        println(simpleSeekBar)
        println(simpleSeekBar)
        println(playButton)
        println(stopButton)

        val record = getLastRecord(selfActivity.applicationContext)
//        nameText.text = record.name

        playButton.setOnClickListener{
            this.startPlay(record, it, selfActivity)
        }

        stopButton.setOnClickListener{
            popupView
            this.stopPlayer()
        }
    }

    private fun initialPopupWindow(playerPopupView: View, activity: Activity): PopupWindow {

        popupWindow = PopupWindow(activity)
        popupWindow.animationStyle = R.style.WindowSlideStyle
        popupWindow.contentView = playerPopupView
        popupWindow.isOutsideTouchable = true
//        popupWindow.isTouchable = true

        val x = playerPopupView.left + popupWindow.width
        val y = playerPopupView.top + popupWindow.height
        popupWindow.showAtLocation(playerPopupView, Gravity.BOTTOM, x, y)

//        setupPopupWindow(playerView, activity)

//            imageView.setOnClickListener{
//                popupWindow.dismiss()
//            }
//        popupWindow.showAsDropDown(view)
        return popupWindow
    }

}
