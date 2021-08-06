package com.projects.android.todolist.view

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.projects.android.todolist.R
import com.projects.android.todolist.TaskApplication
import com.projects.android.todolist.databinding.ActivityAddTaskBinding
import com.projects.android.todolist.extensions.format
import com.projects.android.todolist.extensions.text
import com.projects.android.todolist.room.Task
import com.projects.android.todolist.viewmodel.AddTaskViewModel
import com.projects.android.todolist.viewmodel.MainViewModel
import com.projects.android.todolist.viewmodel.MainViewModelFactory
import java.util.*

class AddTaskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddTaskBinding

    private val addTaskViewModel: AddTaskViewModel by viewModels()

    private val mainViewModel: MainViewModel by viewModels {
        MainViewModelFactory((application as TaskApplication).repository)
    }

    companion object {
        const val TASK_ID = "task_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent.hasExtra(TASK_ID)) {
            val taskId = intent.getIntExtra(TASK_ID, 0)

            mainViewModel.findById(taskId).observe(this, Observer { task ->
                binding.tilTitle.text = task.title
                binding.tilDate.text = task.date
                binding.tilHour.text = task.hour
            })
        } else {
            addTaskViewModel.getTitle().observe(this) { title ->
                binding.tilTitle.text = title.toString()
            }
            addTaskViewModel.getDate().observe(this) { date ->
                binding.tilDate.text = date.toString()
            }
            addTaskViewModel.getHour().observe(this) { hour ->
                binding.tilHour.text = hour.toString()
            }
        }

        insertListeners()
    }

    override fun onDestroy() {
        super.onDestroy()
        addTaskViewModel.saveTitle(binding.tilTitle.text)
        addTaskViewModel.saveDate(binding.tilDate.text)
        addTaskViewModel.saveHour(binding.tilHour.text)
    }
    private fun insertListeners() {
        binding.tilDate.editText?.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.addOnPositiveButtonClickListener {
                val timeZone = TimeZone.getDefault()
                val offset = timeZone.getOffset(Date().time) * -1
                binding.tilDate.text = Date(it + offset).format()
            }
            datePicker.show(supportFragmentManager, "DATE_PICKER_TAG")
        }

        binding.tilHour.editText?.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build()

            timePicker.addOnPositiveButtonClickListener {
                val minute = if(timePicker.minute in 0..9) "0${timePicker.minute}" else timePicker.minute
                val hour = if(timePicker.hour in 0..9) "0${timePicker.hour}" else timePicker.hour

                binding.tilHour.text = "$hour:$minute"
            }
            timePicker.show(supportFragmentManager, null)
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }

        binding.btnNewTask.setOnClickListener {
            if(binding.tilTitle.text.isEmpty() ||
                    binding.tilDate.text.isEmpty() ||
                        binding.tilHour.text.isEmpty()) {
                Toast.makeText(applicationContext,
                    getString(R.string.toast_empty_fields), Toast.LENGTH_SHORT).show()
            } else {
                val task = Task(
                    title = binding.tilTitle.text,
                    hour = binding.tilHour.text,
                    date = binding.tilDate.text,
                    id = intent.getIntExtra(TASK_ID, 0)
                )
                mainViewModel.insertTask(task)
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }
}