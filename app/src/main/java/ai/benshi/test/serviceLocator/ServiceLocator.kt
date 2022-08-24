/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ai.benshi.test.serviceLocator

import ai.benshi.test.api.RetrofitInstance
import ai.benshi.test.db.RoomDb
import ai.benshi.test.preferences.PrefManager
import ai.benshi.test.repository.Repository
import ai.benshi.test.repository.RepositoryImpl
import android.app.Application
import android.content.Context
import androidx.annotation.VisibleForTesting

/**
 * Super simplified service locator implementation to allow us to replace default implementations
 * for testing.
 */
interface ServiceLocator {
    companion object {
        private val LOCK = Any()
        private var instance: ServiceLocator? = null
        fun instance(context: Context): ServiceLocator {
            synchronized(LOCK) {
                if (instance == null) {
                    instance = DefaultServiceLocator(app = context.applicationContext as Application,)
                }
                return instance!!
            }
        }

        /**
         * Allows tests to replace the default implementations.
         */
        @VisibleForTesting
        fun swap(locator: ServiceLocator) {
            instance = locator
        }
    }

    fun getPostRepository(): Repository

}

/**
 * default implementation of ServiceLocator that uses production endpoints.
 */
open class DefaultServiceLocator(val app: Application) : ServiceLocator {
    private val db by lazy {
        RoomDb.create(app)
    }

    private val api by lazy {
        RetrofitInstance.api
    }

    override fun getPostRepository(): Repository {
        return RepositoryImpl(
            roomDb = db,
            apiService = api,)
    }

}