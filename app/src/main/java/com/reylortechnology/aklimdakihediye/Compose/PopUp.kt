package com.reylortechnology.aklimdakihediye.Compose

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.reylortechnology.aklimdakihediye.ui.theme.errorContainerLight
import com.reylortechnology.aklimdakihediye.ui.theme.errorLight
import com.reylortechnology.aklimdakihediye.ui.theme.onPrimaryContainerLight
import com.reylortechnology.aklimdakihediye.ui.theme.primaryContainerLight
import com.reylortechnology.aklimdakihediye.ui.theme.primaryLight
import com.reylortechnology.aklimdakihediye.ui.theme.secondaryContainerLight


@Composable
fun AlertDialog(
    modifier: Modifier = Modifier,
    onDismissRequest : () -> Unit,
    confirmButton : () -> Unit,
    dismissButton : () -> Unit,
    title : String,
    confirmText : String,
    dismissText : String
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest =  onDismissRequest,
        confirmButton = {
            Button(
                onClick = confirmButton,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ){
                Text(confirmText, color = MaterialTheme.colorScheme.onPrimary)
            }
        },
        dismissButton = {
            Button(
                onClick = dismissButton,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text(dismissText, color = MaterialTheme.colorScheme.onError)
            }
        },
        title = {
            Text(
                title,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        },
        containerColor = MaterialTheme.colorScheme.primaryContainer
    )
}

@Preview
@Composable
private fun private() {
    AlertDialog(
        onDismissRequest = {},
        confirmButton = {},
        dismissButton = {},
        title = "test",
        confirmText = "test",
        dismissText = "test"
    )
}