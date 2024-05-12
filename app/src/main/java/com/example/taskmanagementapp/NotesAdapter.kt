package com.example.taskmanagementapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView

class NotesAdapter(private var notes: List<Note>, context: Context):
    RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    private val db: NoteDatabaseHelper = NoteDatabaseHelper(context)
        class NoteViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
            val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
            val contentTextView: TextView = itemView.findViewById(R.id.contentTextView)
            val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
            val priorityTextView: TextView = itemView.findViewById(R.id.priorityTextView)
            val updateButton: ImageView = itemView.findViewById(R.id.updateButton)
            val deleteButton: ImageView = itemView.findViewById(R.id.deleteButton)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item,parent,false)
        return NoteViewHolder(view)
    }

    override fun getItemCount(): Int = notes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.titleTextView.text = note.title
        holder.contentTextView.text = note.content
        holder.dateTextView.text = note.date
        holder.priorityTextView.text = note.priority

        holder.updateButton.setOnClickListener {
            val intent = Intent(holder.itemView.context,UpdateNoteActivity::class.java).apply {
                putExtra("note_id",note.id)
            }
            holder.itemView.context.startActivity(intent)
        }

        holder.deleteButton.setOnClickListener {
            val alertDialog = AlertDialog.Builder(holder.itemView.context)
                .setTitle("Confirm Deletion")
                .setMessage("Are you sure you want to delete this task?")
                .setPositiveButton("Yes") { _, _ ->
                    // Delete the note
                    db.deleteNote(note.id)
                    refreshData(db.getAllNotes())
                    Toast.makeText(holder.itemView.context, "Note Deleted", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()

            alertDialog.show()
        }

    }

    fun refreshData(newNotes: List<Note>){
        notes = newNotes
        notifyDataSetChanged()
    }

}