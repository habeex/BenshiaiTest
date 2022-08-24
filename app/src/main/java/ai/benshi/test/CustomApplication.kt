package ai.benshi.test

import android.app.Application
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner


class CustomApplication: Application(), DefaultLifecycleObserver {

    companion object {
        private var isAppForeground = true
        @Synchronized
        fun isAppInForeground(): Boolean = isAppForeground
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        isAppForeground = false
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        isAppForeground = true
    }

}
