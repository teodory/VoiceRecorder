package com.example.voicerecorder

import java.io.File


data class Record(val name : String, val path: String)


fun getAllRecords() : ArrayList<Record> {
    val allRecords = ArrayList<Record>()

    val recordsDir = File(getRecordingDir())

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


fun getLastRecord() : Record{

    return getAllRecords().first()
}


fun deleteRecord(path: String): Boolean {
    val file = File(path)
    if (file.exists()){
        file.delete()
        return true
    }
    return false
}
