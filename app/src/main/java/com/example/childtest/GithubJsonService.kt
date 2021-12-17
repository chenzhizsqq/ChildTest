package com.example.childtest

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET

interface GithubJsonService {

    //https://raw.githubusercontent.com/chenzhizsqq/testJson/main/db.json中，
    /*"test":
    {
        "id": 333,
        "title": "fdsafdsaf333"
    }*/

    //https://raw.githubusercontent.com/chenzhizsqq/testJson/main/db.json
    @GET("/chenzhizsqq/testJson/test")
    suspend fun getResponse(): Response<ResponseBody>


}