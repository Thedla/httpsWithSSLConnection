package com.example.myapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    var viewModel : MainActivityViewModel ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize View model
        viewModel = application?.let { ViewModelProvider.AndroidViewModelFactory(it).create(MainActivityViewModel::class.java) }

        // click listeners

        // correct certificate button click listener
        btn_wiki_correct_crt.setOnClickListener {
            viewModel?.let {
                progress_bar.visibility = View.VISIBLE
                it.requestWithCorrectCertificate()
            }
        }

        // wrong certificate button click listener
        btn_wiki_wrong_crt.setOnClickListener {
            viewModel?.let {
                progress_bar.visibility = View.VISIBLE
                it.requestWithWrongCertificate()
            }
        }

        viewModel?.data?.observe(this, Observer {
            it?.let {
                tv_response_txt.text = it
            }
            progress_bar.visibility = View.GONE
        })

    }
}

