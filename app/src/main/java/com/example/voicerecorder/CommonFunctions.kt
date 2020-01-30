package com.example.voicerecorder

import android.annotation.SuppressLint
import android.content.Context
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


const val RECORD_EXTENSION = "mp3"
@SuppressLint("SimpleDateFormat")
var dateFormat = SimpleDateFormat("yyyy_MM_dd-HH'h'mm'm'ss's'")


fun getDate(): String {
    val time = Calendar.getInstance()
    return dateFormat.format(time.time)
}

fun getRecordingDir(filesDir: String) : String{

    val baseDir = filesDir + File.separator + "records"
    val recordingDir = File(baseDir)
    if (!recordingDir.exists()){
        recordingDir.mkdir()
    }

    return recordingDir.absolutePath
}


fun generateName(name: String): String? {
    val nameRegex = Regex("(\\d{4})_(\\d{2})_(\\d{2})-(\\d{2})h(\\d{2})m(\\d{2})s")

    val matches = nameRegex.matchEntire(name)
    if (matches != null) {
        return String.format("Rec %s-%s-%s %s:%s.%s",
            matches.groups[1]?.value,
            matches.groups[2]?.value,
            matches.groups[3]?.value,
            matches.groups[4]?.value,
            matches.groups[5]?.value,
            matches.groups[6]?.value)
    }
    return null
}
