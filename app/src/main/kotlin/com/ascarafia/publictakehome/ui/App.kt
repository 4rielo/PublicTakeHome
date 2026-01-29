package com.ascarafia.publictakehome.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ascarafia.publictakehome.domain.model.DataError
import com.ascarafia.publictakehome.ui.create_task.CreateTaskAction
import com.ascarafia.publictakehome.ui.create_task.CreateTaskRoot
import com.ascarafia.publictakehome.ui.create_task.CreateTaskViewModel
import com.ascarafia.publictakehome.ui.main.MainAction
import com.ascarafia.publictakehome.ui.main.MainRoot
import com.ascarafia.publictakehome.ui.main.MainViewModel
import com.ascarafia.publictakehome.ui.navigation.FloatingAction
import com.ascarafia.publictakehome.ui.navigation.NavigationIndex
import com.ascarafia.publictakehome.ui.navigation.TopBar
import com.ascarafia.publictakehome.ui.theme.PublicTakeHomeTheme
import com.ascarafia.publictakehome.ui.util.SnackBarController
import com.ascarafia.publictakehome.ui.util.SnackBarScreenHandler
import com.ascarafia.publictakehome.ui.util.UserEvent
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val navController = rememberNavController()

    var hideCreateTaskButton by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    PublicTakeHomeTheme {
        Scaffold(
            contentWindowInsets = WindowInsets.safeDrawing,
            topBar = {
                TopBar(
                    navController = navController,
                    viewModelStoreOwner = null,
                    showDrawer = {}
                )
            },
            floatingActionButton = {
                FloatingAction(
                    navController = navController,
                    hideFab = hideCreateTaskButton
                )
            },
            snackbarHost = {
                SnackbarHost( snackbarHostState )
            },
        ) { innerPadding ->
            SnackBarScreenHandler(
                snackbarHostState = snackbarHostState
            )

            NavHost(
                navController = navController,
                startDestination = NavigationIndex.Home,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                composable<NavigationIndex.Home> {
                    val mainViewModel: MainViewModel = koinViewModel()
                    MainRoot(
                        modifier = Modifier.fillMaxSize(),
                        onAction = { action ->
                            when (action) {
                                is MainAction.HideCreateTask -> {
                                    hideCreateTaskButton = action.hide
                                }
                                is MainAction.OnEditTaskClick -> {
                                    navController.navigate(NavigationIndex.CreateTask(action.taskId))
                                }
                                else -> mainViewModel.onAction(action)
                            }
                        }
                    )
                }

                composable<NavigationIndex.CreateTask> {
                    val createTaskViewModel: CreateTaskViewModel = koinViewModel()

                    LaunchedEffect(Unit) {
                        createTaskViewModel.screenEvents.collect { event ->
                            when (event) {
                                UserEvent.GoBack -> navController.popBackStack()
                                is UserEvent.DataError -> {
                                    SnackBarController.showErrorSnackbar(event.error as DataError)
                                }
                                else -> Unit
                            }
                        }
                    }

                    CreateTaskRoot(
                        modifier = Modifier.fillMaxSize(),
                        viewModel = createTaskViewModel,
                        onAction = { action ->
                            when(action) {
                                is CreateTaskAction.OnDiscardClicked -> navController.popBackStack()
                                else -> createTaskViewModel.onAction(action)
                            }
                        }
                    )
                }
            }
        }
    }
}
