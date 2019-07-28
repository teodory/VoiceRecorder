package com.example.voicerecorder

import java.io.File
import java.util.*


fun getDate(): String {
    val t = Calendar.getInstance()

    var dateStr = String.format("%d_%02d_%02d-%02dh%02dm%02ds",
        t.get(Calendar.YEAR),
        t.get(Calendar.MONTH),
        t.get(Calendar.DAY_OF_MONTH),
        t.get(Calendar.HOUR),
        t.get(Calendar.MINUTE),
        t.get(Calendar.SECOND)
    )
    return dateStr
}

fun getRecordingDir() : String{

    var baseDir = "/storage/emulated/0/Music/MyRecords"
    var recordingDir = File(baseDir)
    if (!recordingDir.exists()){
        recordingDir.mkdirs()
    }

    return recordingDir.absolutePath
}

private fun testFileWriting() {
    println("\n\nIN TEST\n\n")

//        String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath()
//        var baseDir = Environment.getDataDirectory().absolutePath
//        var baseDir = "/storage/emulated/0/Seba/"
    var baseDir = "/storage/emulated/0/Music/MyRecords"
    var recordingDir = File(baseDir)
    if (!recordingDir.exists()){
        recordingDir.mkdirs()
    }
    var fileName = "myFile" + getDate() + ".txt"

// Not sure if the / is on the path or not
    var f = File(baseDir + File.separator + fileName)
//        var f = File(baseDir)
    File(baseDir).listFiles().forEach {
        println(it)
    }

//        f.walkTopDown().forEach {
//            println(it)
//        }

//        f.writer("----- TEST -----")
    println(f.absolutePath)
    println(f.absoluteFile)
    println(f.appendText("TEST"))

    f.createNewFile()
    println()
//        f.write("----- TEST -----")
//        f.flush()
//        f.close()

    println("================= END TEST ===============")

//        File("").walkBottomUp().forEach {
//            println(it)
//        }


}
