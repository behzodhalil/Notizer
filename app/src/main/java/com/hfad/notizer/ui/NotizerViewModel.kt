package com.hfad.notizer.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.hfad.notizer.model.Note
import com.hfad.notizer.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotizerViewModel(application: Application):AndroidViewModel(application) {

    private var repository: NoteRepository = NoteRepository(application)

    fun getAllNotes() = repository.getNotes()


    fun insert(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(note)
    }
    fun delete(note:Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(note)
    }

    fun allDelete() = viewModelScope.launch(Dispatchers.IO) {
        repository.allDelete()
    }
}