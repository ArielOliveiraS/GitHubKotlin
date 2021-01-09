package com.example.appgithub.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.appgithub.R
import com.example.appgithub.model.Item
import com.example.appgithub.view.adapter.RepositoriesAdapter
import com.example.appgithub.viewmodel.GitHubViewModel
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val list = mutableListOf<Item>()
    private val adapter = RepositoriesAdapter(list)
    private lateinit var viewModel: GitHubViewModel
    private val factory = GitHubViewModel.Factory()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
       // setupLoading()

        repositorios.layoutManager = LinearLayoutManager(this)

        viewModel.getAllMovies()

        viewModel.movieListResult.observe(this, Observer {
            adapter.updateList(it.items)
        })
    }

    fun initViews() {
        viewModel = ViewModelProviders.of(this, factory).get(GitHubViewModel::class.java)
        repositorios.adapter = adapter
    }

//    fun setupLoading() {
//        val colorProgress = Color.parseColor("#CAC8C3")
//        viewModel.loadingResult.observe(this, Observer {
//            if (it) {
//                progressBar.visibility = View.VISIBLE
//                progressBar.indeterminateTintList = ColorStateList.valueOf(colorProgress)
//                movieGroup.visibility = View.GONE
//            } else {
//                progressBar.visibility = View.GONE
//                movieGroup.visibility = View.VISIBLE
//            }
//        })
//    }

//    override fun onMovieClicked(movieResult: MovieResponse) {
//        val intent = Intent(this, MovieDetailsActivity::class.java)
//        intent.putExtra("movie", movieResult)
//        startActivity(intent)
//    }
}