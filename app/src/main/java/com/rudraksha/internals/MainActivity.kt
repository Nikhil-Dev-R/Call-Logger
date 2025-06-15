package com.rudraksha.internals

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.rudraksha.internals.ui.theme.InternalsTheme

class MainActivity : ComponentActivity() {

    @RequiresPermission(allOf = [Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_PHONE_STATE])
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            InternalsTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    CallLogScreen(contentResolver)
                }
            }
        }
    }
}
