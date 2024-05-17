package com.devmasterteam.tasks.service.repository.remote

import com.devmasterteam.tasks.service.model.PersonModel
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface PersonService {

    @POST("Authentication/Login")
    @FormUrlEncoded
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<PersonModel>

    @POST("Authentication/Create")
    @FormUrlEncoded
    fun create(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<PersonModel>
}

/*
* {
    "name": "RaulDev",
    "token": "+szBEkFrk043WtXth2zLEBHYqhY/Jdksz3vf4R1fccw=",
    "personKey": "amiHpJhg2f0OEfPE/xIEZNok8S3K5mT584iEYeECFVU="
}
*
* */