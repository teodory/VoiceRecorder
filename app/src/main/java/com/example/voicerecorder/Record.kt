package com.example.voicerecorder

import java.io.File


data class Record(val name : String, val path: String)


fun getAllRecords() : ArrayList<Record> {
    var allRecords = ArrayList<Record>()

    var recordsDir = File(getRecordingDir())

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


fun deleteRecord(path: String) {
    var file = File(path)
    if (file.exists()){
        file.delete()
    }
}
