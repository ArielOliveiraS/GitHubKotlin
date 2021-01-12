package com.example.appgithub.view.activity

import android.animation.ObjectAnimator
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appgithub.R
import com.example.appgithub.model.Item
import com.example.appgithub.view.adapter.RepositoriesAdapter
import com.example.appgithub.viewmodel.GitHubViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.generic_error.*

class MainActivity() : AppCompatActivity() {

    private val list = mutableListOf<Item>()
    private val adapter = RepositoriesAdapter(list)
    private lateinit var viewModel: GitHubViewModel
    private val factory = GitHubViewModel.Factory()

    var page = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        setupLoading()
        setupError()
        scrollView()
        repositorios.layoutManager = LinearLayoutManager(this)
        viewModel.getAllRepositories(page)
        viewModel.repositorytResult.observe(this, Observer {
            adapter.updateList(it.items)
        })
    }

    fun initViews() {
        viewModel = ViewModelProviders.of(this, factory).get(GitHubViewModel::class.java)
        repositorios.adapter = adapter
    }

    fun nextPage() {
        if (list.size == list.lastIndex + 1) {
            page++
            viewModel.getAllRepositories(page)
        }
    }

    private fun scrollView() = with(repositorios) {
        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    nextPage()
                }
            }
        })
    }

    fun setupLoading() {
        val colorProgress = Color.parseColor("#CAC8C3")
        viewModel.loadingResult.observe(this, Observer {
            if (it) {
                progressBar.visibility = View.VISIBLE
                progressBar.indeterminateTintList = ColorStateList.valueOf(colorProgress)
            } else {
                progressBar.visibility = View.GONE
            }
        })
    }

    fun setupError() {
        viewModel.errorResult.observe(this, Observer {
            if (it) {
                genericErrorView.visibility = View.VISIBLE
                repositorios.visibility = View.GONE
                errorClick { viewModel.getAllRepositories(page) }
                progressBar.visibility = View.GONE
            } else {
                genericErrorView.visibility = View.GONE
                repositorios.visibility = View.VISIBLE
            }
        })
    }

    private fun errorClick(action: () -> Unit) {
        genericErrorView?.setOnClickListener {
            action?.invoke()
            genericErrorImageView?.rotate()
        }
    }

    fun View.rotate() {
        val rotation = ObjectAnimator.ofFloat(this, "rotation", 0f, 360f)
        rotation.duration = 1500L
        rotation.start()
    }

//    override fun onMovieClicked(movieResult: MovieResponse) {
//        val intent = Intent(this, MovieDetailsActivity::class.java)
//        intent.putExtra("movie", movieResult)
//        startActivity(intent)
//    }
}