package com.example.workmanagerdemo1

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.work.*
import com.example.workmanagerdemo1.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object {
        const val KEY_COUNT_VALUE = "key_count"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val btnClick = binding.btnClick
        val tvHelloWorld = binding.tvHelloWorld

        btnClick.setOnClickListener {
            //setOneTimeWorkRequest()
            setPeriodicWorkRequest()
        }
    }

    private fun setOneTimeWorkRequest() {
        val workManager = WorkManager.getInstance(applicationContext)

        val data: Data = Data.Builder()
            .putInt(KEY_COUNT_VALUE, 125)
            .build()

        val constraints = Constraints.Builder()
            .setRequiresCharging(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val uploadRequest = OneTimeWorkRequest.Builder(UploadWorker::class.java)
            .setConstraints(constraints)
            .setInputData(data)
            .build()

        val filteringRequest = OneTimeWorkRequest.Builder(FilteringWorker::class.java)
            .build()

        val compressingRequest = OneTimeWorkRequest.Builder(CompressingWorker::class.java)
            .build()

        val downloadingRequest = OneTimeWorkRequest.Builder(DownloadingWorker::class.java)
            .build()

        val parallelWorks = mutableListOf<OneTimeWorkRequest>()
        parallelWorks.add(downloadingRequest)
        parallelWorks.add(filteringRequest)


        workManager
            .beginWith(parallelWorks)
            .then(compressingRequest)
            .then(uploadRequest)
            .enqueue()

        workManager.getWorkInfoByIdLiveData(uploadRequest.id).observe(this, Observer {
            binding.tvHelloWorld.text = it.state.name

            if (it.state.isFinished) {
                val data = it.outputData
                val message = data.getString(UploadWorker.KEY_WORKER)
                Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun setPeriodicWorkRequest() {
        val periodicWorkRequest = PeriodicWorkRequest.Builder(DownloadingWorker::class.java, 16, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(applicationContext).enqueue(periodicWorkRequest)
    }

}