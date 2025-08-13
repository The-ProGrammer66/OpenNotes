package com.programmer.opennotes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.*
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {

    private val notes = mutableListOf<String>()
    private lateinit var notesAdapter: NotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        notesAdapter = NotesAdapter(notes) { position -> deleteNote(position) }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = notesAdapter

        val addButton = findViewById<ImageButton>(R.id.addButton)
        addButton.setOnClickListener {
            showAddNoteDialog()
        }
    }

    private fun showAddNoteDialog() {
        val editText = EditText(this)
        AlertDialog.Builder(this)
            .setTitle("Add Note")
            .setView(editText)
            .setPositiveButton("Add") { _, _ ->
                val note = editText.text.toString()
                if (note.isNotBlank()) {
                    notes.add(note)
                    notesAdapter.notifyItemInserted(notes.size - 1)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deleteNote(position: Int) {
        notes.removeAt(position)
        notesAdapter.notifyItemRemoved(position)
    }
}

class NotesAdapter(
    private val notes: List<String>,
    private val onDelete: (Int) -> Unit
) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    class NoteViewHolder(view: View, val onDelete: (Int) -> Unit) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.noteText)
        val deleteButton: ImageButton = view.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(v, onDelete)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.textView.text = notes[position]
        holder.deleteButton.setOnClickListener {
            onDelete(position)
        }
    }

    override fun getItemCount() = notes.size
}
