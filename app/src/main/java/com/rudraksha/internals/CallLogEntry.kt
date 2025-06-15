package com.rudraksha.internals

data class CallLogEntry(
    val date: String,
    val time: String,
    val type: String,
    val deviceNumber: String,
    val clientNumber: String,
    val ringDuration: Int,  // only for incoming
    val callDuration: Int
)
