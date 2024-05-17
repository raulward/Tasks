package com.devmasterteam.tasks.service.repository

import android.content.Context
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.PriorityModel
import com.devmasterteam.tasks.service.repository.local.TaskDatabase
import com.devmasterteam.tasks.service.repository.remote.PriorityService
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PriorityRepository(context: Context): BaseRepository(context) {

    private val remote = RetrofitClient.getService(PriorityService::class.java)
    private val db = TaskDatabase.getDatabase(context).priorityDAO()

    companion object {
        private val cache = mutableMapOf<Int, String>()

        fun getDescription (id: Int): String {
            return cache[id] ?: ""
        }

        fun setDescription(id: Int, description: String) {
            cache[id] = description
        }
    }

    fun getDescription(id: Int) : String {
        val cached = PriorityRepository.getDescription(id)
        if (cached == "") {
            val description = db.getDescription(id)
            PriorityRepository.setDescription(id, description)
            return description
        } else {
            return cached
        }
    }

    fun list(listener: APIListener<List<PriorityModel>>) {

        if (!isConnectionAvaible()) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        val call = remote.list()
        executeCall(call, listener)
    }

    fun list(): List<PriorityModel> {
        return db.list()
    }

    fun save(list: List<PriorityModel>) {
        db.clear()
        db.save(list)
    }

}