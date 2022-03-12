package com.example.myrestclient

import retrofit2.Call
import retrofit2.http.*

interface MyServiceAPI {
    @PUT("rest/person")
    fun CreatePerson(@Body person: REST_Person): Call<Void>

    @GET("rest/people")
    fun ListPeople(): Call<List<REST_Person>>

    @GET("rest/person")
    fun ReadPerson(@Query("id") id:Int):Call<REST_Person>

    @PATCH("rest/person")
    fun UpdatePerson(@Query("id")id:Int,@Body person: REST_Person):Call<Void>

    @DELETE("rest/person")
    fun DeletePerson(@Query("id")id:Int):Call<Void>
}