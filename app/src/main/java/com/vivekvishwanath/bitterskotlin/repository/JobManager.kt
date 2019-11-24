package com.vivekvishwanath.bitterskotlin.repository

import android.util.Log
import com.vivekvishwanath.bitterskotlin.util.TAG
import kotlinx.coroutines.Job

open class JobManager(private val className: String) {

    private val jobs: HashMap<String, Job> = hashMapOf()

    fun addJob(functionName: String, job: Job) {
        cancelJob(functionName)
        jobs[functionName] = job
    }

    private fun cancelJob(functionName: String) {
        getJob(functionName)?.cancel()
    }

    private fun getJob(functionName: String): Job? {
        if (jobs.containsKey(functionName)) {
            jobs[functionName]?.let { job ->
                return job
            }
        }
        return null
    }

    fun cancelActiveJobs() {
        for ((functionName, job) in jobs) {
            if (job.isActive) {
                Log.d(TAG, "${this.javaClass.simpleName}: Cancelling job in $functionName")
            }
        }
    }

}