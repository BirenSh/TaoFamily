package com.example.taofamily.core.di

import com.example.taofamily.db.DatabaseWrapper
import com.example.taofamily.features.initiation.data.local.InitLocalDataSource
import com.example.taofamily.features.initiation.data.local.InitLocalDataSourceImpl
import com.example.taofamily.features.initiation.data.local.SettingPreFrance
import com.example.taofamily.features.initiation.data.local.SettingPrefImp
import com.example.taofamily.features.initiation.data.remote.InitRemoteDataSource
import com.example.taofamily.features.initiation.data.remote.InitRemoteDataSourceImpl
import com.example.taofamily.features.initiation.data.repository.InitiationRepositoryImpl
import com.example.taofamily.features.initiation.data.repository.InitiationRepository
import com.example.taofamily.features.initiation.presentation.form_screen.InitiationFormViewModel
import com.example.taofamily.features.initiation.presentation.login_screen.LoginViewModel
import com.example.taofamily.features.initiation.presentation.syncScreen.SyncViewModel
import com.example.taofamily.features.initiation.presentation.taochin_screen.MemberListViewModel
import com.russhwolf.settings.Settings
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

val initiationModule: Module = module{

    //for database module
    single { DatabaseWrapper(driverFactory = get()) }

    // A. DATA SOURCES (Implementation classes must be imported)
    single<InitLocalDataSource> { InitLocalDataSourceImpl(dbWrapper = get ()) }
    single<InitRemoteDataSource> {
        InitRemoteDataSourceImpl(
            client = get(named("apiClient")),
            auth = get()
        )
    }

    // B. REPOSITORY
    single<InitiationRepository> {
        InitiationRepositoryImpl(
            localDataSource = get(),
            remoteDataSource = get(),
            dispatcher = Dispatchers.Default
        )
    }

    //Setting Prefrence
    single<SettingPreFrance> {
        SettingPrefImp(settings = Settings())
    }

    //for viewModels
    factory {
        MemberListViewModel(
            initiationRepository = get(),
            settingPreFrance = get()
        )
    }
    factory {
        InitiationFormViewModel(
            initiationRepository = get(),
        )
    }
    factory {
        LoginViewModel(settingPreFrance = get())

    }

    factory {
        SyncViewModel(initiationRepository = get())

    }


}