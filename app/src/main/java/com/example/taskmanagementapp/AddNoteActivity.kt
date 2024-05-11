package com.example.taskmanagementapp

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.example.taskmanagementapp.databinding.ActivityAddNoteBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private lateinit var textDate: TextView
private lateinit var buttonDate: Button

class AddNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var db: NoteDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NoteDatabaseHelper(this)

        binding.saveButton.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            val content = binding.contentEditText.text.toString()
            val date = binding.textDate.text.toString()
            val priority = binding.priority.selectedItem.toString()
            val note = Note(0, title, content,date,priority)
            db.insertNote(note)
            finish()
            Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show()
        }

        //date
        textDate = findViewById(R.id.textDate)
        buttonDate = findViewById(R.id.buttonDate)

        val calendarBox = Calendar.getInstance()
        val dateBox = DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
            calendarBox.set(Calendar.YEAR, year)
            calendarBox.set(Calendar.MONTH, month)
            calendarBox.set(Calendar.DAY_OF_MONTH, day)

            updateText(calendarBox)
        }
        buttonDate.setOnClickListener {
            DatePickerDialog(
                this,
                dateBox,
                calendarBox.get(Calendar.YEAR),
                calendarBox.get(Calendar.MONTH),
                calendarBox.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        //spinner
        val priorityId = findViewById<Spinner>(R.id.priority)

        val priorities = arrayOf("High","Medium","Low")
        val arrayAdp = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,priorities)
        priorityId.adapter = arrayAdp


    }

    private fun updateText(calendar: Calendar) {
        val dateFormat = "dd-MM-yyyy"
        val simple = SimpleDateFormat(dateFormat, Locale.UK)
        textDate.text = simple.format(calendar.time)
    }
}
