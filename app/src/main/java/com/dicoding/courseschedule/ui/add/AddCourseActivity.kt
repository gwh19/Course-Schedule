package com.dicoding.courseschedule.ui.add

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.util.TimePickerFragment
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddCourseActivity: AppCompatActivity(), TimePickerFragment.DialogTimeListener {

    private lateinit var viewModel: AddCourseViewModel
    private lateinit var addCourse: TextInputEditText
    private lateinit var addLecturer: TextInputEditText
    private lateinit var addNote: TextInputEditText
    private lateinit var addDay: Spinner
    private var timeStart: String = ""
    private var timeEnd: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_course)
        supportActionBar?.show()
        supportActionBar?.title = getString(R.string.add_course)

        viewModel = ViewModelProvider(this, AddCourseViewModelFactory.createFactory(this))[AddCourseViewModel::class.java]

        viewModel.saved.observe(
            this
        ) {
            if (it.getContentIfNotHandled() == true) {
                onBackPressedDispatcher.onBackPressed()
            } else {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.input_empty_message),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDialogTimeSet(tag: String?, hour: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        if(tag == "startPicker") {
            findViewById<TextView>(R.id.tv_start_time).text = timeFormat.format(calendar.time)
            timeStart = timeFormat.format(calendar.time)
        } else if (tag == "endPicker") {
            findViewById<TextView>(R.id.tv_end_time).text = timeFormat.format(calendar.time)
            timeEnd = timeFormat.format(calendar.time)
        }
    }

    fun showStartTimePicker(view: View) {
        val timePickerFragment = TimePickerFragment()
        timePickerFragment.show(supportFragmentManager, "startPicker")
    }

    fun showEndTimePicker(view: View) {
        val timePickerFragment = TimePickerFragment()
        timePickerFragment.show(supportFragmentManager, "endPicker")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_insert -> {
                addCourse = findViewById(R.id.ed_course_name)
                addLecturer = findViewById(R.id.ed_lecturer)
                addNote = findViewById(R.id.ed_note)
                addDay = findViewById(R.id.spinner_day)
                val courseName = addCourse.text.toString().trim()
                val lecturerName = addLecturer.text.toString().trim()
                val note = addNote.text.toString().trim()
                val day = addDay.selectedItemPosition

                viewModel.insertCourse(courseName, day, timeStart, timeEnd, lecturerName, note)
                true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }
}