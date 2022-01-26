package com.example.simpletodo

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset


class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //define what happens when item is long Clicked
        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onItemLongClicked(position: Int) {
                //1. Remove item from list
                listOfTasks.removeAt(position)
                //2.Notify adapter list has been updated
                adapter.notifyDataSetChanged()

                saveItems()
            }
        }

        loadItems()

        //Look up recyclerView in the layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        //create adapter that passes in user data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)
        //attach adapter to recyclerView
        recyclerView.adapter = adapter
        //set layout manager that tells RV how to set up itself
        recyclerView.layoutManager = LinearLayoutManager(this)

        //
        //set up button and input field, so used can enter task by clicking button

        val inputTextField = findViewById<EditText>(R.id.addTaskField)

        //get ref to button then set OnClickListener
        findViewById<Button>(R.id.button).setOnClickListener {
            //1.Grab text user input into @+id/addTaskField
            //grabs text from user and changes it into a string..toString()
            val userInputtedTask = inputTextField.text.toString()

            //2.Add string to list of tasks: listOfTasks
            listOfTasks.add(userInputtedTask)
            //Notify data adapter that data has been updated
            adapter.notifyItemInserted(listOfTasks.size - 1)

            //3.Reset text Field
            inputTextField.setText("")

            saveItems()
        }
    }

    //save data that user has inputted, by reading and writing from a file

    //create a method to get data file we need
    private fun getDataFile() : File {

        //every line in the file is going to represent a specific task
        return File(filesDir, "data.txt")
    }

    //Load the items by reading every line in the file
    private fun loadItems(){
        try {
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException ){
            ioException.printStackTrace()
        }
    }

    //Save items by writing them into our data file
    fun saveItems(){
        try {
            FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch (ioException: IOException ){
            ioException.printStackTrace()
        }
    }

}


