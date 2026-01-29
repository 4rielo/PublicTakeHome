package com.ascarafia.publictakehome.ui.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun MultiSelectionBanner(
    modifier: Modifier,
    onUnPinTasksClicked: () -> Unit,
    onPinTasksClicked: () -> Unit,
    onDeleteTaskClicked: () -> Unit,
    onCancelClicked: () -> Unit
) {
    Row(
        modifier = modifier
            .background(MaterialTheme.colorScheme.secondaryContainer),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onCancelClicked
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Cancel"
            )
        }

        IconButton(
            onClick = onUnPinTasksClicked
        ) {
            Icon(
                imageVector = Icons.Outlined.PushPin,
                contentDescription = "Unpin tasks"
            )
        }

        IconButton(
            onClick = onPinTasksClicked
        ) {
            Icon(
                imageVector = Icons.Default.PushPin,
                contentDescription = "Pin tasks"
            )
        }

        IconButton(
            onClick = onDeleteTaskClicked
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete tasks"
            )
        }
    }
}