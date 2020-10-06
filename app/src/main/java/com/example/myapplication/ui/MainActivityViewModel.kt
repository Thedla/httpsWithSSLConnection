package com.example.myapplication.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.R
import com.example.myapplication.Utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.net.ssl.SSLContext

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    val data: MutableLiveData<String> = MutableLiveData()

    fun requestWithCorrectCertificate(){
        GlobalScope.launch(Dispatchers.Main){
            data.value = Utils.requestData(Utils.getTheSSLContext(getApplication(), R.raw.wikipedia))
        }
    }

    fun requestWithWrongCertificate(){
        GlobalScope.launch(Dispatchers.Main){
            data.value = Utils.requestData(Utils.getTheSSLContext(getApplication(), R.raw.random))
        }
    }

}