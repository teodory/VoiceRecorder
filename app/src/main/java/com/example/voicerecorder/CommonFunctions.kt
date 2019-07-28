package com.example.voicerecorder

import java.io.File
import java.util.*
import kotlin.collections.ArrayList

var RECORD_EXTENSION = "mp3"


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


fun getAllRecords() : List<Record>{
    var allRecords = ArrayList<Record>()

    var recordsDir = File(getRecordingDir())

    recordsDir.walkTopDown().forEach {

        if(it.extension == RECORD_EXTENSION){

            val recName = generateName(it.nameWithoutExtension)
            if(recName != null){
                allRecords.add(Record(name = recName, title = recName, year = 0))
            }
        }

    }
    println(allRecords)
    return allRecords
}

fun generateName(name: String): String? {
//        2019_06_28-05h15m01s
    val nameRegex = Regex("(\\d{4})_(\\d{2})_(\\d{2})-(\\d{2})h(\\d{2})m(\\d{2})s")
//"""(\w+?)(?<num>\d+)""".toRegex().matchEntire("area51")!!.groups["num"]!!.value
    val matches = nameRegex.matchEntire(name)
    if (matches != null) {
        print(matches.groups[1]?.value)
        var str = String.format("Rec %s-%s-%s %s:%s.%s",
            matches.groups[1]?.value,
            matches.groups[2]?.value,
            matches.groups[3]?.value,
            matches.groups[4]?.value,
            matches.groups[5]?.value,
            matches.groups[6]?.value)

        println("----------" + str)
        return str
    }
    return null
}


fun testReadingFiles(){
    println("-------- IN TEST READING --------")
    var file = File(getRecordingDir())

    file.walkTopDown().forEach {

        println(it)
        println(it.extension)
        println(it.nameWithoutExtension)
    }


    println("-------- EXIT TEST READING --------")

}


fun testFileWriting() {
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
