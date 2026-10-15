package com.parentguard.util

enum class Role(val value: String) {
    PARENT("parent"),
    CHILD("child");

    companion object {
        fun from(value: String?): Role = values().firstOrNull { it.value == value } ?: CHILD
    }
}
