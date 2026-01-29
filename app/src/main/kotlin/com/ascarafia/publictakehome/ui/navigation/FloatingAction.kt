package com.ascarafia.publictakehome.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import com.publicapp.takehome.R as Res

@Composable
fun FloatingAction(
    navController: NavController,
    hideFab: Boolean = false
) {
    val backStackEntry: NavBackStackEntry? by navController.currentBackStackEntryAsState()

    when {
        backStackEntry?.destination?.hasRoute<NavigationIndex.Home>() == true -> {
            AnimatedVisibility(
                visible = !hideFab
            ) {
                FloatingActionButton(
                    onClick = {
                        navController.navigate(NavigationIndex.CreateTask())
                    },
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.onPrimaryContainer,
                            MaterialTheme.shapes.medium
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Filled.Add,
                            contentDescription = "Create Task",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                        Text(
                            stringResource(Res.string.task_list_create_new_task),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }
        else -> {

        }
    }
}