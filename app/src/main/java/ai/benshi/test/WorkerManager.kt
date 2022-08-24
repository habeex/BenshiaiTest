package ai.benshi.test

import ai.benshi.test.serviceLocator.ServiceLocator
import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class WorkerManager(
    context: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams) {

    companion object {
        const val WORK_TAG = "user_action_Tag"
    }

    override suspend fun doWork(): Result {
        return if (CustomApplication.isAppInForeground()) {
            val repo = ServiceLocator.instance(applicationContext).getPostRepository()
            // Result.retry()
            val userActions = repo.getUserActions()
            userActions.forEach {
                Log.d("Worker", "${it.userId}")
                //Send an email use javamail-android then delete record once is sent
                //repo.deleteUserActionById(it.id)
            }
            Result.success()
        } else {
            return Result.failure()
        }
    }
}