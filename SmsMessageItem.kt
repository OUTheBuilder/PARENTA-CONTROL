package com.parentguard.data

data class SmsMessageItem(
    val sender: String,
    val body: String,
    val timestampMillis: Long
)
