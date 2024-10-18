package com.example.to_do
import android.os.Bundle

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.to_do.databinding.ActivityUpdateBinding

class UpdateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateBinding
    private lateinit var db: NotesDatabaseHelper
    private var noteId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NotesDatabaseHelper(this)

        noteId = intent.getIntExtra("note_id", -1)
        if (noteId == -1) {
            finish()
            return
        }

        val note = db.getNoteByID(noteId)
        binding.updateTitleEditText.setText(note.title)
        binding.updateContentEditText.setText(note.content)
        binding.updatePriorityEditText.setText(note.priority.toString()) // Set the existing priority

        binding.updateSaveButton.setOnClickListener {
            val newTitle = binding.updateTitleEditText.text.toString()
            val newContent = binding.updateContentEditText.text.toString()
            val newPriorityText = binding.updatePriorityEditText.text.toString()

            // Parse priority and handle potential errors
            val newPriority = try {
                newPriorityText.toInt()
            } catch (e: NumberFormatException) {
                0 // Default priority if parsing fails
            }

            // Create a new Note object including the updated priority
            val updatedNote = Note(noteId, newTitle, newContent, newPriority, note.deadline) // Preserve the existing deadline
            db.updateNote(updatedNote)
            Toast.makeText(this, "Changes Saved", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
