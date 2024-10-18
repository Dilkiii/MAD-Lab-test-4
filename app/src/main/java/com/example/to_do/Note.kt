package com.example.to_do

data class Note(
    val id: Int,
    val title: String,
    val content: String,
    val priority: Int,
    val deadline: Long
    )
