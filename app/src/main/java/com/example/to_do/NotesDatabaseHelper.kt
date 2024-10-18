package com.example.to_do

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class NotesDatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "notesapp.db"
        private const val DATABASE_VERSION = 2
        private const val TABLE_NAME = "allnotes"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_CONTENT = "content"
        private const val COLUMN_PRIORITY = "priority" // New column for priority
        private const val COLUMN_DEADLINE = "deadline" // New column for deadline
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_TITLE TEXT, $COLUMN_CONTENT TEXT, $COLUMN_PRIORITY INTEGER, $COLUMN_DEADLINE LONG)"
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) { // Check if the old version is lower than the new version
            // Adding new columns one by one
            db?.execSQL("ALTER TABLE $TABLE_NAME ADD COLUMN priority INTEGER DEFAULT 0")

        }
    }


    /* create function */
    fun insertNote(note: Note) {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_TITLE, note.title)
            put(COLUMN_CONTENT, note.content)
            put(COLUMN_PRIORITY, note.priority)
            put(COLUMN_DEADLINE, note.deadline)
        }
        db.insert(TABLE_NAME, null, contentValues)
        db.close()
    }

    /* read function */
    fun getAllNotes(): List<Note> {
        val notesList = mutableListOf<Note>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
            val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))
            val priority = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRIORITY))
            val deadline = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_DEADLINE))

            val note = Note(id, title, content, priority, deadline)
            notesList.add(note)
        }
        cursor.close()
        db.close()
        return notesList
    }

    /* update function */
    fun updateNote(note: Note) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, note.title)
            put(COLUMN_CONTENT, note.content)
            put(COLUMN_PRIORITY, note.priority)
            put(COLUMN_DEADLINE, note.deadline)
        }
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(note.id.toString())
        db.update(TABLE_NAME, values, whereClause, whereArgs)
        db.close()
    }

    /* get note by id function */
    fun getNoteByID(noteId: Int): Note {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = $noteId"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()

        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
        val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))
        val priority = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRIORITY))
        val deadline = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_DEADLINE))

        cursor.close()
        db.close()
        return Note(id, title, content, priority, deadline)
    }

    /* delete function */
    fun deleteNote(noteId: Int) {
        val db = writableDatabase
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(noteId.toString())
        db.delete(TABLE_NAME, whereClause, whereArgs)
        db.close()
    }
}