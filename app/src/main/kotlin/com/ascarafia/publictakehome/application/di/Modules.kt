package com.ascarafia.publictakehome.application.di

import com.ascarafia.publictakehome.data.datasources.TaskLocalDataSource
import com.ascarafia.publictakehome.data.repository.TaskRepositoryImpl
import com.ascarafia.publictakehome.domain.datasources.TaskDataSource
import com.ascarafia.publictakehome.domain.repositories.TaskRepository
import com.ascarafia.publictakehome.ui.mainscreen.MainViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModules: List<Module> get() = listOf(
    viewModelsModule, repositoriesModule, dataSourcesModule
)

val viewModelsModule = module {
    viewModelOf(::MainViewModel)
}

val repositoriesModule = module {
    singleOf(::TaskRepositoryImpl) bind TaskRepository::class
}

val dataSourcesModule = module {
    singleOf(::TaskLocalDataSource) bind TaskDataSource::class
}
