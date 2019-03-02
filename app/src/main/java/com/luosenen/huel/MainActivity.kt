package com.luosenen.huel

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.TextView
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity(), View.OnClickListener{

    var responseText: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var sendRequest = findViewById<Button>(R.id.send_request)
        responseText = findViewById<TextView>(R.id.response_text)

        sendRequest.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if(v!!.id == R.id.send_request) {
            sendRequestWithHttpURLConnection()
        }
    }


    fun showResponse(responseData: String?) {
        runOnUiThread(object : Runnable {
            override fun run() {
                responseText?.text = responseData
            }
        })
    }

    fun sendRequestWithHttpURLConnection() {
        Thread(object : Runnable {
            override fun run() {
                var connection: HttpURLConnection? = null
                var reader: BufferedReader? = null

                try {
                    var url = URL("https://wechat.laixuanzuo.com/index.php/reserve/index.html?f=wechat")
                    connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = "GET"
                    connection.setRequestProperty("Connection","keep-alive")
                    connection.setRequestProperty("Host","wechat.laixuanzuo.com")
                    connection.setRequestProperty("Referer","https://wechat.laixuanzuo.com/index.php/reserve/layout/libid=10065.html&"+(System.currentTimeMillis()))
                    connection.setRequestProperty("Accept","application/json, text/javascript, */*; q=0.01")
                    connection.setRequestProperty("Accept-Encoding","gzip, deflate")
                    connection.setRequestProperty("Accept-Language","zh-CN,zh;q=0.8,en-us;q=0.6,en;q=0.5;q=0.4")
                    connection.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36 MicroMessenger/6.5.2.501 NetType/WIFI WindowsWechat QBCore/3.43.901.400 QQBrowser/9.0.2524.400")
                    connection.setRequestProperty("X-Requested-With","XMLHttpRequest")
                    connection.setRequestProperty("wechatSESS_ID","")
                    connection.connectTimeout = 8000
                    connection.readTimeout = 8000

                    var inStream = connection.inputStream
                    reader = BufferedReader(InputStreamReader(inStream))
                    var response = StringBuilder()
                    var allText = reader.use(BufferedReader::readText)
                    response.append(allText)
                    showResponse(response.toString())

                }catch (ex: Exception) {
                    ex.printStackTrace()
                } finally {

                    reader?.let {
                        try {
                            it.close()
                        } catch (exInner: Exception) {
                            exInner.printStackTrace()
                        }
                    }

                    connection?.let {
                        it.disconnect()
                    }

                }

            }
        }).start()
    }
}
