package com.projects.android.todolist.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.projects.android.todolist.TaskApplication
import com.projects.android.todolist.databinding.ActivityMainBinding
import com.projects.android.todolist.viewmodel.MainViewModel
import com.projects.android.todolist.viewmodel.MainViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val adapter by lazy { TaskListAdapter() }

    private val applicationScope = CoroutineScope(SupervisorJob())

    private val mainViewModel: MainViewModel by viewModels {
        MainViewModelFactory((application as TaskApplication).repository)
    }

    companion object {
        private const val CREATE_NEW_TASK = 1000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvTasks.adapter = adapter

        mainViewModel.allTasks.observe(this, Observer { tasks ->
            tasks?.let {
                adapter.submitList(it)
                updateList()

            }
        })

        insertListeners()
        updateList()
    }

    private fun insertListeners() {
        binding.fab.setOnClickListener {
            startActivityForResult(Intent(this, AddTaskActivity::class.java), CREATE_NEW_TASK)
        }

        adapter.listenerEdit = {
            val intent = Intent(this, AddTaskActivity::class.java)
            intent.putExtra(AddTaskActivity.TASK_ID, it.id)
            startActivityForResult(intent, CREATE_NEW_TASK)
        }

        adapter.listenerDelete = {
            mainViewModel.deleteTask(it)
            updateList()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == CREATE_NEW_TASK && resultCode == Activity.RESULT_OK) {
            updateList()

        }
    }

    private fun updateList() {
        applicationScope.launch {
            binding.includeEmpty.emptyState.visibility = if (mainViewModel.countTasks() == 0) View.VISIBLE else View.GONE
        }
        adapter.notifyDataSetChanged()
    }
}