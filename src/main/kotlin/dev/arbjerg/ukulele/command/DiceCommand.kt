package dev.arbjerg.ukulele.command

import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import dev.arbjerg.ukulele.audio.Player
import dev.arbjerg.ukulele.audio.PlayerRegistry
import dev.arbjerg.ukulele.features.HelpContext
import dev.arbjerg.ukulele.jda.Command
import dev.arbjerg.ukulele.jda.CommandContext
import dev.arbjerg.ukulele.jda.PrivateMessageContext
import dev.arbjerg.ukulele.utils.TextUtils
import org.springframework.stereotype.Component

import java.util.Random

@Component
class DiceCommand() : Command("dice", "d") {
    private val random = Random()
    override suspend fun CommandContext.invoke() {
        val (numDice, numFaces) = parseArgument(argumentText)
        reply("${message.author.name} Request [${numDice}d$numFaces]: ${rollDices(numDice, numFaces).joinToString(separator = ", ")}")
    }
    override suspend fun PrivateMessageContext.invoke() {
        val (numDice, numFaces) = parseArgument(argumentText)
        reply("${message.author.name} Request [${numDice}d$numFaces]: ${rollDices(numDice, numFaces).joinToString(separator = ", ")}")
    }


    private fun parseArgument(argumentText: String): Pair<Int, Int> {
        val input = argumentText.split("d")
        if (input.size != 2) {
            throw IllegalArgumentException("Invalid argument format. Expected format: [Number of dices]d[Number of faces]")
        }

        val numDice = input[0].toIntOrNull()
            ?: throw IllegalArgumentException("Invalid number of dices")
        val numFaces = input[1].toIntOrNull()
            ?: throw IllegalArgumentException("Invalid number of faces")

        if (numDice < 1 || numFaces < 2) {
            throw IllegalArgumentException("Number of dices must be at least 1 and number of faces must be at least 2")
        }

        if (numDice > 50 || numFaces > 200) {
            throw IllegalArgumentException("dice max 50 and faces max 200")
        }

        return Pair(numDice, numFaces)
    }

    private fun rollDices(numDice: Int, numFaces: Int): List<Int> {
        return (1..numDice).map { random.nextInt(numFaces) + 1 }.sortedDescending()
    }

    override fun HelpContext.provideHelp() {
        addUsage("[Number of dices]d[Number of faces]")
        addDescription("roll the dice")
    }
}

