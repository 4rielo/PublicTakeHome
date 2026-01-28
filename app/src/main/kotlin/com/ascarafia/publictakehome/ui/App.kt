package com.ascarafia.publictakehome.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ascarafia.publictakehome.ui.create_task.CreateTaskAction
import com.ascarafia.publictakehome.ui.create_task.CreateTaskRoot
import com.ascarafia.publictakehome.ui.create_task.CreateTaskViewModel
import com.ascarafia.publictakehome.ui.main.MainAction
import com.ascarafia.publictakehome.ui.main.MainRoot
import com.ascarafia.publictakehome.ui.navigation.FloatingAction
import com.ascarafia.publictakehome.ui.navigation.NavigationIndex
import com.ascarafia.publictakehome.ui.navigation.TopBar
import com.ascarafia.publictakehome.ui.theme.PublicTakeHomeTheme
import com.ascarafia.publictakehome.ui.util.UserEvent
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val navController = rememberNavController()

    var hideCreateTaskButton by remember { mutableStateOf(false) }

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
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = NavigationIndex.Home,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                composable<NavigationIndex.Home> {
                    MainRoot(
                        modifier = Modifier.fillMaxSize(),
                        onAction = { action ->
                            when (action) {
                                is MainAction.HideCreateTask -> {
                                    hideCreateTaskButton = action.hide
                                }
                            }
                        }
                    )
                }

                composable<NavigationIndex.CreateTask> {
                    val createTaskViewModel: CreateTaskViewModel = koinViewModel()
                    LifecycleResumeEffect( createTaskViewModel.screenActions, LocalLifecycleOwner.current,) {
                        lifecycleScope.launch {
                            createTaskViewModel.screenActions.collect { event ->
                                when (event) {
                                    UserEvent.GoBack -> navController.popBackStack()
                                    is UserEvent.DataError -> {
                                        //TODO: Show toast
                                    }

                                    else -> Unit
                                }
                            }
                        }

                        onPauseOrDispose {

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
