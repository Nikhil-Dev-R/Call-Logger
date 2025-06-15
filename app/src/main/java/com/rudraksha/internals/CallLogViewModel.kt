package com.rudraksha.internals

import android.Manifest
import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.os.Build
import android.provider.CallLog
import android.telephony.SubscriptionManager
import androidx.annotation.RequiresPermission
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class CallLogViewModel(app: Application) : AndroidViewModel(app) {

    private val _logs = MutableStateFlow<List<CallLogEntry>>(emptyList())
    val logs: StateFlow<List<CallLogEntry>> = _logs

    @RequiresPermission(allOf = [Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_PHONE_STATE])
    fun loadCallLogs(contentResolver: ContentResolver) {
        viewModelScope.launch {
            val logList = mutableListOf<CallLogEntry>()
            val deviceNumber = getDevicePhoneNumber(getApplication())

            val cursor = contentResolver.query(
                CallLog.Calls.CONTENT_URI,
                null,
                null,
                null,
                CallLog.Calls.DATE + " DESC"
            )

            cursor?.use {
                val numberIndex = it.getColumnIndex(CallLog.Calls.NUMBER)
                val typeIndex = it.getColumnIndex(CallLog.Calls.TYPE)
                val dateIndex = it.getColumnIndex(CallLog.Calls.DATE)
                val durationIndex = it.getColumnIndex(CallLog.Calls.DURATION)

                while (it.moveToNext()) {
                    val timestamp = it.getLong(dateIndex)
                    val callDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(timestamp))
                    val callTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date(timestamp))
                    val type = when (it.getInt(typeIndex)) {
                        CallLog.Calls.INCOMING_TYPE -> "Incoming"
                        CallLog.Calls.OUTGOING_TYPE -> "Outgoing"
                        CallLog.Calls.MISSED_TYPE -> "Missed"
                        else -> "Other"
                    }

                    val duration = it.getInt(durationIndex)
                    val clientNumber = it.getString(numberIndex)
                    val ringDuration = if (type == "Incoming" && duration > 0) (1..5).random() else 0

                    logList.add(
                        CallLogEntry(
                            date = callDate,
                            time = callTime,
                            type = type,
                            deviceNumber = deviceNumber,
                            clientNumber = clientNumber,
                            ringDuration = ringDuration,
                            callDuration = duration
                        )
                    )
                }
            }

            _logs.value = logList
        }
    }

    fun sendData() {
        viewModelScope.launch {
            val logs = _logs.value
            for (log in logs) {
                sendCallLogToServer(log)
            }
        }
    }

    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    private fun getDevicePhoneNumber(context: Context): String {
        return try {
            val subscriptionManager = context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as? SubscriptionManager
                ?: return "Service not available"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val infoList = subscriptionManager.activeSubscriptionInfoList
                if (!infoList.isNullOrEmpty()) {
                    infoList[0].number ?: "Unknown"
                } else "No SIM"
            } else "Unsupported"
        } catch (e: SecurityException) {
            "Permission denied"
        } catch (e: Exception) {
            "Error: ${e.localizedMessage}"
        }
    }
}
