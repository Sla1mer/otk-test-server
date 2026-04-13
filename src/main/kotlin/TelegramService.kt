package ru.interlinkstudio

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*

class TelegramService {

    private val sshHost = System.getenv("RELAY_SSH_HOST") ?: error("RELAY_SSH_HOST не задан")
    private val sshUser = System.getenv("RELAY_SSH_USER") ?: "root"
    private val scriptPath = System.getenv("RELAY_SCRIPT_PATH") ?: "/opt/tg-relay/send_telegram.sh"

    fun sendNotification(
        name: String,
        communicationAddress: String,
        phoneNumber: String,
        message: String?
    ) {
        val command = listOf(
            "ssh",
            "$sshUser@$sshHost",
            scriptPath,
            name,
            communicationAddress,
            phoneNumber,
            message ?: ""
        )

        try {
            val process = ProcessBuilder(command)
                .redirectErrorStream(true)
                .start()

            val output = process.inputStream.bufferedReader().readText()
            val exitCode = process.waitFor()

            println("Telegram relay exitCode=$exitCode")
            println("Telegram relay output=$output")

            if (exitCode != 0) {
                throw RuntimeException("SSH relay failed with exit code $exitCode")
            }

        } catch (e: Exception) {
            println("Telegram relay error: ${e.message}")
            throw e
        }
    }
}
