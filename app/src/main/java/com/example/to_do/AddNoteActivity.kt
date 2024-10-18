package com.example.to_do

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.to_do.databinding.ActivityAddNoteBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var db: NotesDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate called")
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NotesDatabaseHelper(this)

        // Set up click listener for the deadline EditText
        binding.deadlineEditText.setOnClickListener {
            showDatePickerDialog()
        }

        binding.saveButton.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            val content = binding.contentEditText.text.toString()
            val priority = binding.priorityEditText.text.toString().toIntOrNull() ?: 0 // Default to 0 if empty
            val deadlineString = binding.deadlineEditText.text.toString() // Get the date string

            // Convert the date string to a Long timestamp
            val deadline = convertDateStringToLong(deadlineString)

            // Create a new note with the additional fields
            val note = Note(0, title, content, priority, deadline)
            db.insertNote(note)  // Ensure you have this method in your database helper

            Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    // Function to show DatePickerDialog
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear)
            binding.deadlineEditText.setText(formattedDate)
        }, year, month, day)

        datePickerDialog.show()
    }

    // Convert date string to long timestamp
    private fun convertDateStringToLong(dateString: String): Long {
        return try {
            val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) // Adjust the format as needed
            val date = format.parse(dateString)
            date?.time ?: 0L // Return timestamp or 0 if parsing fails
        } catch (e: Exception) {
            0L // Return 0 if there's an exception
        }
    }
}
