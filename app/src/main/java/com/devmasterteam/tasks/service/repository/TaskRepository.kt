package com.devmasterteam.tasks.service.repository

import android.content.Context
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient
import com.devmasterteam.tasks.service.repository.remote.TaskService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskRepository (context: Context): BaseRepository(context){

    private val remote = RetrofitClient.getService(TaskService::class.java)

    fun list(listener: APIListener<List<TaskModel>>) {

        if (!isConnectionAvaible()) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        val call = remote.list()
        executeCall(call, listener)
    }

    fun listNextSevenDays(listener: APIListener<List<TaskModel>>) {

        if (!isConnectionAvaible()) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        val call = remote.listNextSevenDays()
        executeCall(call, listener)
    }

    fun listOverdue(listener: APIListener<List<TaskModel>>) {

        if (!isConnectionAvaible()) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        val call = remote.listOverDue()
        executeCall(call, listener)

    }


    fun create(task: TaskModel, listener: APIListener<Boolean>) {

        if (!isConnectionAvaible()) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        val call = remote.create(task.priorityId, task.description, task.dueDate, task.complete)
        executeCall(call, listener)
    }

    fun delete(id: Int, listener: APIListener<Boolean>) {

        if (!isConnectionAvaible()) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        val call = remote.delete(id)
        executeCall(call, listener)
    }

    fun completeTask(id: Int, listener: APIListener<Boolean>) {

        if (!isConnectionAvaible()) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        executeCall(remote.completeTask(id), listener)
    }

    fun undoTask(id: Int, listener: APIListener<Boolean>) {

        if (!isConnectionAvaible()) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        executeCall(remote.undoTask(id), listener)
    }

    fun load(id: Int, listener: APIListener<TaskModel>) {

        if (!isConnectionAvaible()) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        executeCall(remote.load(id), listener)
    }

    fun update(task: TaskModel, listener: APIListener<Boolean>) {

        if (!isConnectionAvaible()) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        val call = remote.update(task.id, task.priorityId, task.description, task.dueDate, task.complete)
        executeCall(call, listener)
    }

}