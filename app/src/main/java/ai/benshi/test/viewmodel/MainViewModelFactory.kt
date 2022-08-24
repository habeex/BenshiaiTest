package ai.benshi.test.viewmodel

import ai.benshi.test.repository.Repository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class MainViewModelFactory constructor(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("ViewModel not found..")
    }
}
