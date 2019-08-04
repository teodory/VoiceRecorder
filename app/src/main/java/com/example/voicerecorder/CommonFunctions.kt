package com.example.voicerecorder

import android.content.Context
import java.io.File
import java.security.AccessController.getContext
import java.util.*


const val RECORD_EXTENSION = "mp3"


fun getDate(): String {
    val t = Calendar.getInstance()

    return String.format("%d_%02d_%02d-%02dh%02dm%02ds",
        t.get(Calendar.YEAR),
        t.get(Calendar.MONTH),
        t.get(Calendar.DAY_OF_MONTH),
        t.get(Calendar.HOUR),
        t.get(Calendar.MINUTE),
        t.get(Calendar.SECOND)
    )
}

fun getRecordingDir(context: Context) : String{
//    val baseDir = "/data/user/0/com.example.voicerecorder/files/records"
    val baseDir = context.filesDir.absolutePath + "/records"

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


fun testReadingFiles(){
    println("-------- IN TEST READING --------")

//    val file = File(getRecordingDir())
    val file = File("/data/user/0/com.example.voicerecorder/files/records")

    println( "" + file.exists() + " " + file)
    println("" + file.isDirectory + "   " + file.isFile)

    file.walkTopDown().forEach {
        println("---" + it)
    }

//    file.walkTopDown().forEach {
//
//        println(it)
//        println(it.extension)
//        println(it.nameWithoutExtension)
//    }
    println("-------- EXIT TEST READING --------")

}


fun testFileWriting() {
    println("\n\nIN TEST\n\n")

//        String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath()
//        val baseDir = Environment.getDataDirectory().absolutePath
//        val baseDir = "/storage/emulated/0/Seba/"
    val baseDir = "/storage/emulated/0/Music/MyRecords"
    val recordingDir = File(baseDir)
    if (!recordingDir.exists()){
        recordingDir.mkdirs()
    }
    val fileName = "myFile" + getDate() + ".txt"

// Not sure if the / is on the path or not
    val f = File(baseDir + File.separator + fileName)
//        val f = File(baseDir)
//    File(baseDir).listFiles().forEach {
//        println(it)
//    }

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
