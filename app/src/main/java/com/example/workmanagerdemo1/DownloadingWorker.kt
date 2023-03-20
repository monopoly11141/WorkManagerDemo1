package com.example.workmanagerdemo1

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.workmanagerdemo1.UploadWorker.Companion.KEY_WORKER
import java.text.SimpleDateFormat
import java.util.*

class DownloadingWorker(context : Context, params : WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {

        try {
            for(i in 0..3000) {
                Log.d("MYTAG", "Downloading $i")
            }

            return Result.success()
        } catch(e : Exception) {
            return Result.failure()
        }

    }


}