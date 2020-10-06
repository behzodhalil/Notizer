package com.hfad.notizer.ui


import android.annotation.SuppressLint

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hfad.notizer.R
import com.hfad.notizer.databinding.NoteItemBinding
import com.hfad.notizer.model.Note
import kotlinx.android.synthetic.main.note_item.view.*
import java.util.*
import kotlin.concurrent.schedule


@Suppress("UNREACHABLE_CODE")
class NotizerAdapter(private var item: MutableList<Note>,
                  private val clickListener: (note:Note,position:Int) -> Unit)
    : RecyclerView.Adapter<NotizerAdapter.NoteViewHolder>(){
    class NoteViewHolder(val binding: NoteItemBinding):RecyclerView.ViewHolder(binding.root)

    private lateinit var timerTask: Timer
    private val searchList: MutableList<Note> by lazy { item }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.note_item,parent,false)
        val viewHolder = NoteViewHolder(NoteItemBinding.bind(view))
        view.setOnClickListener {
            clickListener.invoke(item[viewHolder.adapterPosition],viewHolder.adapterPosition)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.binding.note = item[position]
        val gradientDrawable: GradientDrawable = (holder.itemView.lineViewNote.background as GradientDrawable)

        if (item[position].color != "") gradientDrawable
            .setColor(Color.parseColor(item[position].color))
        else gradientDrawable.setColor(Color.parseColor("#333333"))


    }
    fun get(position: Int): Note {
        return item[position]
    }

    override fun getItemCount() : Int = item.size

    @SuppressLint("DefaultLocale")
    fun search(searchKeyword: String) {
        timerTask = Timer()
        timerTask.schedule(500){
            if (searchKeyword.isNotEmpty()) {
                val temp = mutableListOf<Note>()
                for (i in searchList){
                    if(i.title!!.toLowerCase().contains(searchKeyword.toLowerCase())
                        || i.desc!!.toLowerCase().contains(searchKeyword.toLowerCase())){
                        temp.add(i)
                    }
                    item = temp
                }
            } else item = searchList
            Handler(Looper.getMainLooper()).post {
                notifyDataSetChanged()
            }
        }
    }

    fun cancelTimer() {
        if(::timerTask.isInitialized) {
            timerTask.cancel()
        }
    }
}

