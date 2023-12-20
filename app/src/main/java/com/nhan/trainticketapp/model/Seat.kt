package com.nhan.trainticketapp.model


data class Seat(val position: List<Boolean>, val train_id: Int)
{
    constructor() : this(emptyList(), 0)
}
