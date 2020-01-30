package com.example.voicerecorder

import android.content.Context
import java.io.File


data class Record(val name : String, val path: String)


fun getAllRecords(filesDir: String) : ArrayList<Record> {

    val allRecords = ArrayList<Record>()

    val recordsDir = File(getRecordingDir(filesDir))

    recordsDir.walkTopDown().sortedByDescending { it.name }.forEach {

        if(it.extension == RECORD_EXTENSION){

            val recName = generateName(it.nameWithoutExtension)
            if(recName != null){
                allRecords.add(Record(name = recName, path = it.absolutePath))
            }
        }
    }

    return allRecords
}

fun getRecordsCount (filesDir: String) : Int {
    return getAllRecords(filesDir).size
}

fun getLastRecord(filesDir: String) : Record {
    println("\tRECORDS SIZE: " + getAllRecords(filesDir).size)
    return getAllRecords(filesDir).first()
}


fun deleteFile(path: String): Boolean {
    val file = File(path)
    if (file.exists()){
        file.delete()
        return true
    }
    return false
}
