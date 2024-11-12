package com.debugdesk.bot.di

import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.debugdesk.bot.datastore.DataStore
import com.debugdesk.bot.db.AppDatabase
import com.debugdesk.bot.presentation.screens.botapp.BotAppViewModel
import com.debugdesk.bot.presentation.screens.note.NoteViewModel
import com.debugdesk.bot.presentation.screens.settings.SettingViewModel
import com.debugdesk.bot.presentation.screens.tracker.TrackerViewModel
import com.debugdesk.bot.repo.RoomRepository
import com.debugdesk.bot.repo.RoomRepositoryImpl
import com.debugdesk.bot.utils.appstate.AppStateManager
import com.debugdesk.bot.utils.appstate.AppStateManagerImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object KoinModule {
    private const val APP_PREFERENCES_NAME = "app_preferences"

    val appModule = module {

        single<AppDatabase> {
            Room.databaseBuilder(
                context = androidApplication(),
                klass = AppDatabase::class.java,
                name = "bot-app-database"
            ).build()
        }

        /**
         * Koin Module for providing a singleton instance of DataStore. The DataStore is configured with
         * specific parameters for data storage, corruption handling, migrations, and coroutine scope.
         *
         * @return Singleton instance of DataStore configured with a PreferenceDataStoreFactory to handle
         * corruption using ReplaceFileCorruptionHandler, migrations from SharedPreferences, and a
         * custom coroutine scope for IO operations.
         */
        single<DataStore> {
            DataStore(
                dataStore = PreferenceDataStoreFactory.create(corruptionHandler = ReplaceFileCorruptionHandler(
                    produceNewData = { emptyPreferences() }), migrations = listOf(
                    SharedPreferencesMigration(
                        androidApplication(), APP_PREFERENCES_NAME
                    )
                ), scope = CoroutineScope(Dispatchers.IO + SupervisorJob()), produceFile = {
                    androidApplication().preferencesDataStoreFile(
                        APP_PREFERENCES_NAME
                    )
                })
            )
        }
        single<RoomRepository> { RoomRepositoryImpl(appDatabase = get()) }
        single<AppStateManager> { AppStateManagerImpl(dataStore = get()) }


        viewModel {
            BotAppViewModel(
                appStateManager = get(),
            )
        }
        viewModel {
            SettingViewModel(
                appStateManager = get(),
                roomRepository = get()
            )
        }

        viewModel {
            TrackerViewModel(
                roomRepo = get(),
            )
        }
        viewModel {
            NoteViewModel(
                roomRepository = get(),
            )
        }
    }

}