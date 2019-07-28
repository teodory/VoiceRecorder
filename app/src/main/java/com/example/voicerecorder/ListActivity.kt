package com.example.voicerecorder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import kotlinx.android.synthetic.main.activity_list.*
import java.util.*
import java.util.Arrays.asList
import kotlin.collections.ArrayList


class ListActivity : AppCompatActivity() {

    data class Movie(val title: String, val year: Int)

    private val myDataset = listOf(
        Movie("Raising Arizona", 1987),
        Movie("Vampire's Kiss", 1988),
        Movie("Con Air", 1997),
        Movie("Gone in 60 Seconds", 1997),
        Movie("National Treasure", 2004),
        Movie("The Wicker Man", 2006),
        Movie("Ghost Rider", 2007),
        Movie("Knowing", 2009)
    )

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        val recyclerView = findViewById<RecyclerView>(R.id.my_recycle_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

//        val adapter = CustomAdapter(myDataset)
        val adapter = CustomAdapter(getAllRecords())
        recyclerView.adapter = adapter

//        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

    }

}



//1. Declare the RecyclerView in an activity layout and reference it in the activity Kotlin file.
//2. Create a custom item XML layout for RecyclerView for its items.
//3. Create the view holder for view items, connect the data source of the RecyclerView
//   and handle the view logic by creating a RecyclerView Adapter.
//4. Attach the adapter to the RecyclerView.
