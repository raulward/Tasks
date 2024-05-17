package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.service.model.ValidationModel
import com.devmasterteam.tasks.service.repository.PriorityRepository
import com.devmasterteam.tasks.service.repository.TaskRepository

class TaskListViewModel(application: Application) : AndroidViewModel(application) {

    private val taskRepository = TaskRepository(application.applicationContext)
    private val priorityRepository = PriorityRepository(application.applicationContext)
    private var _taskFilter = 0

    private var _tasks: MutableLiveData<List<TaskModel>> = MutableLiveData()
    var tasks: LiveData<List<TaskModel>> = _tasks

    private var _delete: MutableLiveData<ValidationModel> = MutableLiveData()
    var delete: LiveData<ValidationModel> = _delete

    private var _status: MutableLiveData<ValidationModel> = MutableLiveData()
    var status: LiveData<ValidationModel> = _status

    fun list(taskFilter: Int) {
        _taskFilter = taskFilter
        val listener = object : APIListener<List<TaskModel>> {
            override fun onSuccess(result: List<TaskModel>) {

                result.forEach {
                    it.priorityDescription = priorityRepository.getDescription(it.priorityId)
                }

                _tasks.value = result
            }
            override fun onFailure(message: String) {

            }
        }

        when (taskFilter) {
            TaskConstants.FILTER.ALL -> taskRepository.list(listener)
            TaskConstants.FILTER.NEXT  -> taskRepository.listNextSevenDays(listener)
            TaskConstants.FILTER.EXPIRED  -> taskRepository.listOverdue(listener)
        }


    }

    fun delete(id: Int) {
        taskRepository.delete(id, object : APIListener<Boolean>{
            override fun onSuccess(result: Boolean) {
                list(_taskFilter)
                _delete.value = ValidationModel()
            }

            override fun onFailure(message: String) {
                _delete.value = ValidationModel(message)
            }

        })
    }

    fun status(id: Int, complete: Boolean) {

        val listener = object : APIListener<Boolean>{
            override fun onSuccess(result: Boolean) {
                list(_taskFilter)
            }

            override fun onFailure(message: String) {
                _status.value = ValidationModel(message)
            }

        }

        if (complete) {
            taskRepository.completeTask(id, listener)
        } else {
            taskRepository.undoTask(id, listener)
        }


    }

}