package com.nhan.trainticketapp.model

data class UserModel(val name: String, val email: String, val phone: String, val point: Int) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "name" to name,
            "email" to email,
            "phone" to phone,
            "point" to point,
        )
    }
}