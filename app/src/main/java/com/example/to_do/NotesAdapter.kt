package com.example.to_do

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class NotesAdapter(private var originalNotesList: List<Note>, private val context: Context): RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    private val db: NotesDatabaseHelper = NotesDatabaseHelper(context)
    private var filteredNotesList: List<Note> = originalNotesList

    class NoteViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val titleTextView: TextView = view.findViewById(R.id.titleTextView)
        val contentTextView: TextView = view.findViewById(R.id.contentTextView)
        val priorityTextView: TextView = itemView.findViewById(R.id.priorityTextView)
        val deadlineTextView: TextView = itemView.findViewById(R.id.deadlineTextView)
        val updateButton: ImageView = itemView.findViewById(R.id.updateButton)
        val deleteButton: ImageView = itemView.findViewById(R.id.deleteButton)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(view)
    }

    override fun getItemCount(): Int{
        return filteredNotesList.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = filteredNotesList[position]
        holder.titleTextView.text = note.title
        holder.contentTextView.text = note.content
        holder.priorityTextView.text = "Priority: ${note.priority}"
        holder.deadlineTextView.text = "Deadline: ${note.deadline}"

        holder.updateButton.setOnClickListener {
            val intent = Intent(holder.itemView.context, UpdateActivity::class.java).apply {
                putExtra("note_id", note.id)
            }
            holder.itemView.context.startActivity(intent)
        }

        holder.deleteButton.setOnClickListener {
            db.deleteNote(note.id)
            setNotes(db.getAllNotes())
            Toast.makeText(holder.itemView.context, "Marked as Completed!", Toast.LENGTH_SHORT).show()
        }
    }




    // Update the list with filtered results
    fun filter(query: String){
        filteredNotesList = if (query.isEmpty()){
            originalNotesList
        }else{
            originalNotesList.filter {
                it.title.contains(query,true) || it.content.contains(query, ignoreCase = true)
            }
        }
        notifyDataSetChanged()
    }

    fun setNotes(newNotesList: List<Note>){
        originalNotesList = newNotesList
        filteredNotesList = newNotesList
        notifyDataSetChanged()
    }
}
