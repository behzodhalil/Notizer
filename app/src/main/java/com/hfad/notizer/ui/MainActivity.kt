package com.hfad.notizer.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hfad.notizer.R
import com.hfad.notizer.model.Note
import kotlinx.android.synthetic.main.activity_main.*




class MainActivity : AppCompatActivity() {
    companion object{
        private const val REQUEST_CODE_ADD_NOTE = 1
        private const val REQUEST_CODE_UPDATE_NOTE = 2
        private const val REQUEST_CODE_SHOW_NOTE = 3
    }

    private lateinit var notesAdapter: NotizerAdapter
    private lateinit var noteList: MutableList<Note>
    private lateinit var viewModel: NotizerViewModel
    private var noteClickPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this,NotizerViewModelFactory(application))
            .get(NotizerViewModel::class.java)
        initRecyclerView()
        initClick()
        searchNote()
    }

    private fun initClick() {
        fabBtn.setOnClickListener {
            startActivityForResult(Intent(this,
                AddNotes::class.java),REQUEST_CODE_ADD_NOTE)
        }
    }
    @Suppress("SameParameterValue")
    private fun getNote(requestCode: Int,isNoteDelete:Boolean) {
        viewModel.getAllNotes().observe(this, Observer {
            when (requestCode) {
                REQUEST_CODE_SHOW_NOTE -> {
                    noteList.addAll(it)
                    notesAdapter.notifyDataSetChanged()
                }
                REQUEST_CODE_ADD_NOTE -> {
                    noteList.add(0,it[0])
                    notesAdapter.notifyItemInserted(0)
                    noteRecyclerView.smoothScrollToPosition(0)
                }
                REQUEST_CODE_UPDATE_NOTE -> {
                    noteList.removeAt(noteClickPosition)
                    if(isNoteDelete){
                        notesAdapter.notifyItemRemoved(noteClickPosition)
                    } else{
                        noteList.add(noteClickPosition,it[noteClickPosition])
                        notesAdapter.notifyItemChanged(noteClickPosition)
                    }
                }
            }
        })
    }

    private fun initRecyclerView() {
        noteRecyclerView.apply {
            noteList = mutableListOf()
            notesAdapter = NotizerAdapter(noteList){ note, position ->
                noteClickPosition = position
                val intent = Intent(this@MainActivity,AddNotes::class.java)
                intent.putExtra("isViewOrUpdate",true)
                intent.putExtra("note",note)
                startActivityForResult(intent,REQUEST_CODE_UPDATE_NOTE)
            }
            this.layoutManager = LinearLayoutManager(context)

            this.adapter = notesAdapter
            this.setHasFixedSize(true)
        }
        getNote(REQUEST_CODE_SHOW_NOTE,false)
    }

    private fun searchNote() {
        inputSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if(noteList.isNotEmpty()) {
                    notesAdapter.search(p0.toString())
                }
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                notesAdapter.cancelTimer()
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE_ADD_NOTE && resultCode == Activity.RESULT_OK) {
            getNote(REQUEST_CODE_ADD_NOTE,false)
        } else if(requestCode == REQUEST_CODE_UPDATE_NOTE && resultCode == RESULT_OK) {
            if(data!=null) {
                getNote(REQUEST_CODE_UPDATE_NOTE, data.getBooleanExtra("isNoteDelete",false))
            }
        }
    }
}