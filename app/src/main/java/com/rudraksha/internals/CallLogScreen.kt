package com.rudraksha.internals

import android.Manifest
import android.content.ContentResolver
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@RequiresPermission(allOf = [Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_PHONE_STATE])
@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CallLogScreen(contentResolver: ContentResolver, viewModel: CallLogViewModel = viewModel()) {
    val callLogPermissionState = rememberPermissionState(Manifest.permission.READ_CALL_LOG)
    val phoneStatePermissionState = rememberPermissionState(Manifest.permission.READ_PHONE_STATE)

    if (callLogPermissionState.status.isGranted && phoneStatePermissionState.status.isGranted) {
        val logs = viewModel.logs.collectAsState().value

        LaunchedEffect(Unit) {
            viewModel.loadCallLogs(contentResolver)
        }
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Call Log") },
                    actions = {
                        IconButton(
                            onClick = { viewModel.loadCallLogs(contentResolver) }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = "Send"
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                items(logs) {
                    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Date: ${it.date}")
                            Text("Time: ${it.time}")
                            Text("Type: ${it.type}")
                            Text("Device #: ${it.deviceNumber}")
                            Text("Client #: ${it.clientNumber}")
                            Text("Ring Duration: ${it.ringDuration} sec")
                            Text("Call Duration: ${it.callDuration} sec")
                        }
                    }
                }
            }
        }
    } else {
        PermissionRequestUI {
            callLogPermissionState.launchPermissionRequest()
            phoneStatePermissionState.launchPermissionRequest()
        }
    }
}

@Composable
fun PermissionRequestUI(onRequest: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("This app requires permissions to read call logs and your device number.")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRequest) {
            Text("Grant Permissions")
        }
    }
}
