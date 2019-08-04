package com.example.voicerecorder

import android.content.Context
import java.io.File


data class Record(val name : String, val path: String)


fun getAllRecords(context: Context) : ArrayList<Record> {
    val allRecords = ArrayList<Record>()

    val recordsDir = File(getRecordingDir(context))

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


fun getLastRecord(context: Context) : Record{

    return getAllRecords(context).first()
}


fun deleteRecord(path: String): Boolean {
    val file = File(path)
    if (file.exists()){
        file.delete()
        return true
    }
    return false
}
