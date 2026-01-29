package com.ascarafia.publictakehome.application.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.ascarafia.publictakehome.data.database.DatabaseFactory
import com.ascarafia.publictakehome.data.database.TaskDao
import com.ascarafia.publictakehome.data.database.TaskDatabase
import com.ascarafia.publictakehome.data.database.migrations.DatabaseMigrations
import com.ascarafia.publictakehome.data.datasources.TaskLocalDataSource
import com.ascarafia.publictakehome.data.datasources.TaskRemoteDataSource
import com.ascarafia.publictakehome.data.network.HttpClientFactory
import com.ascarafia.publictakehome.data.repository.TaskRepositoryImpl
import com.ascarafia.publictakehome.domain.datasources.TaskDataSource
import com.ascarafia.publictakehome.domain.repositories.TaskRepository
import com.ascarafia.publictakehome.ui.create_task.CreateTaskViewModel
import com.ascarafia.publictakehome.ui.main.MainViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModules: List<Module> get() = listOf(
    viewModelsModule, repositoriesModule, dataSourcesModule, databaseModule
)

val viewModelsModule = module {
    viewModelOf(::MainViewModel)
    viewModelOf(::CreateTaskViewModel)
}

val repositoriesModule = module {
    val repositoryScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    single {
        TaskRepositoryImpl(
            get(TaskLocalDataSource::class),
            get(TaskRemoteDataSource::class),
            repositoryScope
        )
    } bind TaskRepository::class
}

val dataSourcesModule = module {
    singleOf(::TaskLocalDataSource) bind TaskDataSource::class
    singleOf(::TaskRemoteDataSource) bind TaskDataSource::class

    single {
        HttpClientFactory
            .create( OkHttp.create() )
    } bind HttpClient::class
}

val databaseModule = module {
    single {
        DatabaseFactory(androidApplication())
            .create()
            .addMigrations(DatabaseMigrations.MIGRATION_1_2)
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    } bind TaskDatabase::class
    single { get<TaskDatabase>().taskDao } bind TaskDao::class
}
