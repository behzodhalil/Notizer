package com.hfad.notizer.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room.*
import androidx.room.RoomDatabase

@Database(entities = [Note::class],version = 3,exportSchema = false)
abstract class NotesDatabase : RoomDatabase() {

    abstract fun getDao(): NoteDao

    companion object{
        @Volatile
        private var INSTANCE: NotesDatabase? = null

        fun getInstance(context: Context):NotesDatabase{
            synchronized(this){
                var instance = INSTANCE
                if(instance == null){
                    instance = databaseBuilder(context.applicationContext,
                        NotesDatabase::class.java, "notes_database")
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()
                }
                return instance
            }
        }
    }
}