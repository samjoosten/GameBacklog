package com.example.gamebacklog.add

import android.app.Activity
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.gamebacklog.R
import com.example.gamebacklog.model.Game
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.content_add.*
import java.util.*


class AddActivity : AppCompatActivity() {

    private lateinit var addViewModel: AddViewModel
    private var releaseDate: Date? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        setSupportActionBar(toolbar)

        initViews()
        initViewModel()
    }

    private fun initViews() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        fab.setOnClickListener { onSaveClick() }

        etReleaseDate.setOnClickListener { onEditDateClick() }

        etReleaseDate.setOnFocusChangeListener { _: View?, hasFocus: Boolean ->
            if (hasFocus) {
                onEditDateClick()
            }
        }
    }

    private fun initViewModel() {
        addViewModel = ViewModelProviders.of(this).get(AddViewModel::class.java)
    }

    @Suppress("DEPRECATION")
    private fun onEditDateClick() {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(window.decorView.rootView.windowToken, 0)

        val calendarInstance = Calendar.getInstance()
        val ciYear = calendarInstance.get(Calendar.YEAR)
        val ciMonth = calendarInstance.get(Calendar.MONTH)
        val ciDay = calendarInstance.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                releaseDate =
                    Date(year, monthOfYear, dayOfMonth, 0, 0)
                val releaseDateText = "${dayOfMonth}/${monthOfYear + 1}/${year}"
                etReleaseDate.setText(releaseDateText)
            }, ciYear, ciMonth, ciDay
        )
        datePickerDialog.show()
    }

    private fun onSaveClick() {
        val toast = Toast.makeText(this, "", Toast.LENGTH_SHORT)
        if (etGameTitle.text.isNullOrBlank()) {
            toast.setText(R.string.val_title)
            toast.show()
        } else if (etPlatform.text.isNullOrBlank()) {
            toast.setText(R.string.val_platform)
            toast.show()
        } else if (releaseDate == null) {
            toast.setText(R.string.val_date)
            toast.show()
        } else {
            addViewModel.insertGame(
                Game(
                    null,
                    etGameTitle.text.toString(),
                    etPlatform.text.toString(),
                    releaseDate!!
                )
            )
            etGameTitle.text?.clear()
            etPlatform.text?.clear()
            etReleaseDate.text?.clear()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return try {
            finish()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
