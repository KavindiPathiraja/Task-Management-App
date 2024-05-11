package com.example.taskmanagementapp

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.example.taskmanagementapp.databinding.ActivityUpdateNoteBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private lateinit var updateTextDate: TextView
private lateinit var updateButtonDate: Button

class UpdateNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateNoteBinding
    private lateinit var db: NoteDatabaseHelper
    private var noteId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NoteDatabaseHelper(this)

        noteId = intent.getIntExtra("note_id",-1)
        if(noteId == -1){
            finish()
            return
        }

        val note = db.getNoteByID(noteId)
        binding.updateTitleEditText.setText(note.title)
        binding.updateContentEditText.setText(note.content)
        binding.updateTextDate.setText(note.date) // Set the date text instead of content text
        val priorityArray = resources.getStringArray(R.array.priority_array)
        val priorityIndex = priorityArray.indexOf(note.priority)

        // Setting up spinner
        val priorityId = binding.updatePriority
        val priorities = arrayOf("High", "Medium", "Low")
        val arrayAdp = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, priorities)
        priorityId.adapter = arrayAdp

        binding.updatePriority.setSelection(priorityIndex)

        binding.updateSaveButton.setOnClickListener{
            val newTitle = binding.updateTitleEditText.text.toString()
            val newContent = binding.updateContentEditText.text.toString()
            val newDate = binding.updateTextDate.text.toString()
            val newPriority = binding.updatePriority.selectedItem.toString()
            val updatedNote = Note(noteId, newTitle, newContent, newDate, newPriority) // Preserve date and priority
            db.updateNote(updatedNote)
            finish()
            Toast.makeText(this,"Changes Saved",Toast.LENGTH_SHORT).show()
        }

        // Setting up date picker
        updateTextDate = findViewById(R.id.updateTextDate)
        updateButtonDate = findViewById(R.id.updateButtonDate)
        val calendarBox = Calendar.getInstance()
        val dateBox = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendarBox.set(Calendar.YEAR, year)
            calendarBox.set(Calendar.MONTH, month)
            calendarBox.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateText(calendarBox)
        }
        updateButtonDate.setOnClickListener {
            DatePickerDialog(
                this,
                dateBox,
                calendarBox.get(Calendar.YEAR),
                calendarBox.get(Calendar.MONTH),
                calendarBox.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun updateText(calendar: Calendar) {
        val dateFormat = "dd-MM-yyyy"
        val simple = SimpleDateFormat(dateFormat, Locale.UK)
        binding.updateTextDate.text = simple.format(calendar.time)
    }
}
