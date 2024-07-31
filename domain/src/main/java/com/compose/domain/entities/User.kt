package com.compose.domain.entities
import java.io.Serializable

data class User(
    val userId:String="",
    val username: String = "",
    val email :String="",
    val password: String = "",
    var profilePictureUrl: String = ""
):Serializable