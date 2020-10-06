package com.example.myapplication.Utils

import android.content.Context
import android.util.Log
import androidx.annotation.RawRes
import com.example.myapplication.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStream
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.security.KeyStore
import java.security.cert.CertificateFactory
import java.security.cert.Extension
import java.security.cert.X509Certificate
import java.security.spec.ECField
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory

class Utils {

    companion object {

        fun getTheSSLContext(context:Context, rawRes: Int) : SSLContext {

            // create certificate factory instance
            val cf : CertificateFactory = CertificateFactory.getInstance("X.509")

            // read the local certificate
            var caInputStream :InputStream ?= null
            try {
                caInputStream = BufferedInputStream(context.resources.openRawResource(rawRes))
            }catch (e:Exception){
                e.printStackTrace()
            }

            // creating certificate
            val ca: X509Certificate ?= caInputStream?.use {
                cf.generateCertificate(it) as? X509Certificate
            }

            Log.d("ca=", "${ca?.subjectDN.toString() ?: ""}}")


            // creating the keystore
            val keystore = KeyStore.getInstance(KeyStore.getDefaultType()).apply {
                load(null,null)
                setCertificateEntry("ca",ca)
            }

            // create the trust manager using the keystore
            val tmf: TrustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm()).apply {
                init(keystore)
            }

            // create the ssl context
            val sslContext:SSLContext = SSLContext.getInstance("TLS").apply {
                init(null,tmf.trustManagers,null)
            }

            // return the ssl Context
            return sslContext
        }

        // request the data
        suspend fun requestData(sslContext: SSLContext): String {
            // launch the coroutine Scope with IO dispatcher
            return withContext(Dispatchers.IO) {
                val url = URL("https://en.wikipedia.org/wiki/X.509")
                try {
                    val urlConnection = url.openConnection() as HttpsURLConnection
                    urlConnection.sslSocketFactory = sslContext.socketFactory
                    val reader = BufferedReader(urlConnection.inputStream.reader())
                    try { reader.readText() }
                    catch (e: Exception) { e.message ?: "" }
                }
                catch (e:Exception) { e.message ?: "" }
            }
        }
    }
}