package com.hongstudio.parsing_github_repositories.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hongstudio.parsing_github_repositories.R
import com.hongstudio.parsing_github_repositories.model.RepositoryModel
import com.hongstudio.parsing_github_repositories.retrofitinterface.NetworkServiceInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val networkService: NetworkServiceInterface =
            retrofit.create(NetworkServiceInterface::class.java)

        val searchedResultCall = networkService.getSearchedResult("cafe")

        searchedResultCall.enqueue(object : Callback<RepositoryModel> {
            override fun onResponse(
                call: Call<RepositoryModel>,
                response: Response<RepositoryModel>
            ) {
                val searchedResult = response.body()
            }

            override fun onFailure(call: Call<RepositoryModel>, t: Throwable) {
                call.cancel()
            }

        })
    }
}