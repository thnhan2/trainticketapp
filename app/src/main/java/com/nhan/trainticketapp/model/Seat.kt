package com.nhan.trainticketapp.model


data class Seat(var position: List<Boolean>, val train_id: Int)
{
    constructor() : this(emptyList(), 0)
}
