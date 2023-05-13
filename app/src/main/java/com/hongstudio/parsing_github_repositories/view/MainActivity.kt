package com.hongstudio.parsing_github_repositories.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hongstudio.parsing_github_repositories.R
import com.hongstudio.parsing_github_repositories.adapter.RepositoryRecyclerViewAdapter
import com.hongstudio.parsing_github_repositories.databinding.ActivityMainBinding
import com.hongstudio.parsing_github_repositories.model.RepositoryModel
import com.hongstudio.parsing_github_repositories.retrofitinterface.NetworkServiceInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.mainActivity = this@MainActivity
    }

    val buttonSearchClickListener = View.OnClickListener {
        loadRepositoriesData(binding.editTextSearch.text.toString())
    }

    private fun loadRepositoriesData(searchedWord: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val networkService: NetworkServiceInterface =
            retrofit.create(NetworkServiceInterface::class.java)

        val searchedResultCall = networkService.getSearchedResult(searchedWord)

        searchedResultCall.enqueue(object : Callback<RepositoryModel> {
            override fun onResponse(
                call: Call<RepositoryModel>,
                response: Response<RepositoryModel>
            ) {
                val searchedResult = response.body()
                if (searchedResult?.items != null) {
                    val adapter = RepositoryRecyclerViewAdapter(searchedResult.items)
                    binding.recyclerViewRepositories.apply {
                        this.adapter = adapter
                        layoutManager = LinearLayoutManager(this@MainActivity)
                        addItemDecoration(DividerItemDecoration(this@MainActivity, LinearLayoutManager.VERTICAL))
                    }
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<RepositoryModel>, t: Throwable) {
                call.cancel()
            }

        })
    }
}