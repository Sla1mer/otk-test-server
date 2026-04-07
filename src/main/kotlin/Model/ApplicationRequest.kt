package ru.interlinkstudio.Model

import kotlinx.serialization.Serializable

@Serializable
data class ApplicationRequest(
    val name: String,
    val communicationAddress: String,
    val phoneNumber: String,
    val message: String? = null,
)
