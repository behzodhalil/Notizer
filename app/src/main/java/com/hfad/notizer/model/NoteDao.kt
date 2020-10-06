package com.hfad.notizer.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDao{

    @Query("SELECT * FROM notes ORDER BY id DESC")
    fun getAllNotes(): LiveData<List<Note>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Update
    suspend fun update(note: Note)

    @Delete()
    suspend fun deleteNote(note: Note)

    @Query("DELETE FROM notes")
    suspend fun deleteAll() : Int

    @Query("DELETE FROM notes WHERE id = :key")
    fun delete(key: Long)
}