package ai.benshi.test.ui

import ai.benshi.test.R
import ai.benshi.test.WorkerManager
import ai.benshi.test.databinding.ActivityMainBinding
import ai.benshi.test.serviceLocator.ServiceLocator
import ai.benshi.test.viewmodel.MainViewModel
import ai.benshi.test.viewmodel.MainViewModelFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.work.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        setupActionBarWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id == R.id.splashScreenFragment) {
                supportActionBar?.hide()
            } else {
                supportActionBar?.show()
            }
        }

        val repo = ServiceLocator.instance(this).getPostRepository()
        val viewModelFactory = MainViewModelFactory(repo)
        ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        initWorker()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun initWorker() {
        val workManager = WorkManager.getInstance(this)
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val myWorkRequest = PeriodicWorkRequestBuilder<WorkerManager>(
            repeatInterval = 1L,
            repeatIntervalTimeUnit = TimeUnit.MINUTES,
        ).setConstraints(constraints).build()

        workManager.enqueueUniquePeriodicWork(
            WorkerManager.WORK_TAG,
            ExistingPeriodicWorkPolicy.REPLACE,
            myWorkRequest
        )
    }
}