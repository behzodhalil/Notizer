package com.hfad.notizer.repository

import android.app.Application
import com.hfad.notizer.model.Note
import com.hfad.notizer.model.NoteDao
import com.hfad.notizer.model.NotesDatabase

class NoteRepository(application: Application){

    private var dao:NoteDao

    init{
        val database : NotesDatabase = NotesDatabase.getInstance(application)
        dao = database.getDao()
    }
    fun getNotes() = dao.getAllNotes()

    suspend fun insert(note: Note){
        dao.insertNote(note)
    }
    suspend fun delete(note:Note) {
        dao.deleteNote(note)
    }

    suspend fun allDelete() {
        dao.deleteAll()
    }


}