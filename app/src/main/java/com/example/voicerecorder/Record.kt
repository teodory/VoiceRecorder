package com.example.voicerecorder

//data class Record(val name : String)
data class Record(val name : String, val title: String, val year: Int){

    fun getRecordName() : String{

        return name
    }
}