package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.PriorityModel
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.service.model.ValidationModel
import com.devmasterteam.tasks.service.repository.PriorityRepository
import com.devmasterteam.tasks.service.repository.TaskRepository

class TaskFormViewModel(application: Application) : AndroidViewModel(application) {

    private val priorityRepository = PriorityRepository(application.applicationContext)
    private val taskRepository = TaskRepository(application.applicationContext)

    private var _listPriorities = MutableLiveData<List<PriorityModel>>()
    var listPriorities = _listPriorities

    private var _taskSave: MutableLiveData<ValidationModel> = MutableLiveData()
    var taskSave: LiveData<ValidationModel> = _taskSave

    private var _task: MutableLiveData<TaskModel> = MutableLiveData()
    var task: LiveData<TaskModel> = _task

    private var _loadStatus: MutableLiveData<ValidationModel> = MutableLiveData()
    var loadStatus: LiveData<ValidationModel> = _loadStatus

    fun loadPriorities() {
        _listPriorities.value = priorityRepository.list()
    }

    fun load(id: Int) {
        taskRepository.load(id, object : APIListener<TaskModel> {
            override fun onSuccess(result: TaskModel) {
                _task.value = result
            }

            override fun onFailure(message: String) {
                _loadStatus.value = ValidationModel(message)
            }

        })
    }

    fun save(task: TaskModel) {

        val listener = object : APIListener<Boolean>{
                override fun onSuccess(result: Boolean) {
                    _taskSave.value = ValidationModel()
                }

                override fun onFailure(message: String) {
                    _taskSave.value = ValidationModel(message)
                }

            }

        if (task.id == 0) {
            taskRepository.create(task, listener)
        } else {
            taskRepository.update(task, listener)
        }

    }


}