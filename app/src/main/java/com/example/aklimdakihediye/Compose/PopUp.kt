package com.example.aklimdakihediye.Compose

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.aklimdakihediye.ui.theme.ColorDailyUserInfoBc
import com.example.aklimdakihediye.ui.theme.ColorUserMainPlaceLovers

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
                    containerColor = ColorDailyUserInfoBc
                )
            ){
                Text(confirmText, color = Color.White)
            }
        },
        dismissButton = {
            Button(
                onClick = dismissButton,
                colors = ButtonDefaults.buttonColors(
                    containerColor = ColorUserMainPlaceLovers
                )
            ) {
                Text(dismissText, color = Color.White)
            }
        },
        title = {
            Text(
                title,
                fontWeight = FontWeight.Bold
            )
        }
    )
}