package com.nhan.trainticketapp.model


data class Train(
    val description: String = "",
    val destination: String = "",
    val duration: Int = 0,
    val origin: String = "",
    val schedules: List<Schedule> = listOf(),
    val stations: List<String> = listOf(),
    val train_id: Int = 0
)
