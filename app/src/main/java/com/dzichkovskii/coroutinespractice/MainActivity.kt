package com.dzichkovskii.coroutinespractice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

class MainActivity : AppCompatActivity() {

    companion object{
        private const val JOB_TIME = 1900L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            setNewText("Click!")

            CoroutineScope(IO).launch {
                fakeApiRequest()
            }
        }
    }

    private suspend fun fakeApiRequest(){
        withContext(IO) {
            val job = withTimeoutOrNull(JOB_TIME){

                val result1 = getResult1FromApi()
                setTextOnMainThread("Got $result1")

                val result2 = getResult2FromApi()
                setTextOnMainThread("Got $result2")
            }

            if (job == null) {
                val cancelMessage = "Cancelling job... Job took longer than ${JOB_TIME}ms"
                setTextOnMainThread(cancelMessage)
            }
        }
    }

    private fun setNewText(input: String) {
        val newText = text.text.toString() + "\n$input"
        text.text = newText
    }

    private suspend fun setTextOnMainThread(input: String) {
        withContext(Main) {
            setNewText(input)
        }
    }

    private suspend fun getResult1FromApi(): String {
        delay(1000)
        return "Result #1"
    }

    private suspend fun getResult2FromApi(): String {
        delay(1000)
        return "Result #2"
    }
}
