package com.example.workmanagerdemo1

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.workmanagerdemo1.UploadWorker.Companion.KEY_WORKER
import java.text.SimpleDateFormat
import java.util.*

class CompressingWorker(context : Context, params : WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {

        try {
            for(i in 0..300) {
                Log.d("MYTAG", "Compressing $i")
            }

            return Result.success()
        } catch(e : Exception) {
            return Result.failure()
        }

    }


}