package com.hfad.notizer.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.hfad.notizer.R
import com.hfad.notizer.model.Note
import com.hfad.notizer.utils.getRealPathFromURI
import kotlinx.android.synthetic.main.add_notes.*
import kotlinx.android.synthetic.main.layout_colors.*
import kotlinx.android.synthetic.main.layout_colors.view.*

import java.text.SimpleDateFormat
import java.util.*

class AddNotes : AppCompatActivity() {
    companion object {
        private const val IMAGE_PICK_CODE = 1000
        private const val PERMISSION_CODE = 1001
    }

    private lateinit var viewModel: NotizerViewModel
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var colorNote: String
    private lateinit var img: String
    private lateinit var alreadyAvailableNote: Note

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_notes)
        viewModel = ViewModelProvider(this, NotizerViewModelFactory(application))
            .get(NotizerViewModel::class.java)

        initClick()
        initView()

    }

    private fun initView() {
        colorNote = "#333333"
        img = ""
        dateTxt.text = SimpleDateFormat("MM'.'dd'.' E HH:mm", Locale.getDefault()).format(Date())
        bottomSheetBehavior = BottomSheetBehavior.from(layoutColors)
        if (intent.getBooleanExtra("isViewOrUpdate", false)) {
            alreadyAvailableNote = intent.getParcelableExtra<Note>("note")!!
            setViewOrUpdateNote()
        }

        layoutColors.findViewById<TextView>(R.id.colorsText).setOnClickListener {
            if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            } else {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
        initMiscellaneous()
        setSubtitleIndicator()
    }

    private fun initClick() {
        addNoteSaveBtn.setOnClickListener {
            saveNote()
        }
        addNoteBackBtn.setOnClickListener {
            finish()
        }

        imgAdd.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) ==
                    PackageManager.PERMISSION_DENIED
                ) {
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permissions, PERMISSION_CODE)
                } else pickImageFromGallery()
            } else pickImageFromGallery()
        }





        addNoteImgDelete.setOnClickListener {
            img = ""
            imgNoteAdd.visibility = View.GONE
            addNoteImgDelete.visibility = View.GONE
        }


    }

    private fun saveNote() {
        if (addNoteTitle.text?.trim().isNullOrEmpty()) {
            Toast.makeText(this, "Note title can't be empty!", Toast.LENGTH_SHORT).show()
        } else if (addNoteDesc.text?.trim().isNullOrEmpty()) {
            Toast.makeText(this, "Note can't be empty!", Toast.LENGTH_SHORT).show()
        }
        val note = Note(
            title = addNoteTitle.text.toString(),
            date = dateTxt.text.toString(),
            desc = addNoteDesc.text.toString(),
            img = img, color = colorNote,
        )
        if (::alreadyAvailableNote.isInitialized) {
            note.id = alreadyAvailableNote.id
        }
        viewModel.insert(note)
        hideKeyboard()
        setResult(Activity.RESULT_OK, Intent())
        finish()
    }
    private fun deleteNote(note:Note) {
        viewModel.delete(note)
        val intent = Intent()
        intent.putExtra("isNoteDelete",true)
        setResult(Activity.RESULT_OK,intent)
        finish()
    }

    private fun setViewOrUpdateNote() {
        addNoteTitle.setText(alreadyAvailableNote.title)
        addNoteDesc.setText(alreadyAvailableNote.desc)


        if (!alreadyAvailableNote.img.isNullOrEmpty()) {
            imgNoteAdd.visibility = View.VISIBLE
            imgNoteAdd.setImageBitmap(BitmapFactory.decodeFile(alreadyAvailableNote.img))
            img = alreadyAvailableNote.img!!
            addNoteImgDelete.visibility = View.VISIBLE
        }
        if(::alreadyAvailableNote.isInitialized) {
            deleteBtn.visibility = View.VISIBLE
            deleteBtn.setOnClickListener {
               deleteNote(alreadyAvailableNote)
            }
        }

    }

    private fun setSubtitleIndicator() {
        val gradientDrawable: GradientDrawable = (viewIndicator.background as GradientDrawable)
        gradientDrawable.setColor(Color.parseColor(colorNote))
    }

    private fun initMiscellaneous() {
        with(layoutColors) {
            colorDefault.setOnClickListener {
                colorNote = "#00574B"
                colorDefault.setImageResource(R.drawable.ic_done)
                itemColorPrimrose.setImageResource(0)
                itemColorSuo.setImageResource(0)
                itemColorKimberly.setImageResource(0)
                itemColorSantaGray.setImageResource(0)
                setSubtitleIndicator()
            }

            itemColorPrimrose.setOnClickListener {
                colorNote = "#F0E7A0"
                colorDefault.setImageResource(0)
                itemColorPrimrose.setImageResource(R.drawable.ic_done)
                itemColorSuo.setImageResource(0)
                itemColorKimberly.setImageResource(0)
                itemColorSantaGray.setImageResource(0)
                setSubtitleIndicator()
            }

            itemColorSuo.setOnClickListener {
                colorNote = "#EE7979"
                colorDefault.setImageResource(0)
                itemColorPrimrose.setImageResource(0)
                itemColorSuo.setImageResource(R.drawable.ic_done)
                itemColorKimberly.setImageResource(0)
                itemColorSantaGray.setImageResource(0)
                setSubtitleIndicator()
            }

            itemColorKimberly.setOnClickListener {
                colorNote = "#676295"
                colorDefault.setImageResource(0)
                itemColorPrimrose.setImageResource(0)
                itemColorSuo.setImageResource(0)
                itemColorKimberly.setImageResource(R.drawable.ic_done)
                itemColorSantaGray.setImageResource(0)
                setSubtitleIndicator()
            }

            itemColorSantaGray.setOnClickListener {
                colorNote = "#A4A1B7"
                colorDefault.setImageResource(0)
                itemColorPrimrose.setImageResource(0)
                itemColorSuo.setImageResource(0)
                itemColorKimberly.setImageResource(0)
                itemColorSantaGray.setImageResource(R.drawable.ic_done)
                setSubtitleIndicator()
            }
        }

        if (::alreadyAvailableNote.isInitialized
            && !alreadyAvailableNote.color.isNullOrEmpty()
        ) {
            when (alreadyAvailableNote.color) {
                "#FFFFFF" -> layoutColors.colorDefault.performClick()
                "#F0E7A0" -> layoutColors.itemColorPrimrose.performClick()
                "#EE7979" -> layoutColors.itemColorSuo.performClick()
                "#676295" -> layoutColors.itemColorKimberly.performClick()
                "#A4A1B7" -> layoutColors.itemColorSantaGray.performClick()
            }
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        if (intent.resolveActivity(packageManager) != null) {
            intent.type = "image/*"
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            startActivityForResult(intent, IMAGE_PICK_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    pickImageFromGallery()
                } else Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            imgNoteAdd.visibility = View.VISIBLE
            imgNoteAdd.setImageURI(data!!.data)
            img = getRealPathFromURI(this, data.data!!)!!
            addNoteImgDelete.visibility = View.VISIBLE
        }
    }

    private fun hideKeyboard() {
        if (this.currentFocus != null) {
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(this.currentFocus!!.windowToken, 0)
        }
    }
}

