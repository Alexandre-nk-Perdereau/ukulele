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
class HexadiceCommand() : Command("hexadice", "hdice", "h") {
    private val random = Random()
    override suspend fun CommandContext.invoke() {
        val numDice = parseArgument(argumentText)
        var dicesToRoll = numDice
        val numFaces = 6
        val rolls = arrayListOf<List<Int>>()
        var success = 0
        while (dicesToRoll > 0) {
            val roll = rollDices(dicesToRoll, numFaces)
            rolls.add(roll)
            dicesToRoll = roll.count{it == 6}
            success += roll.count{ roll -> roll > 2 }
        }
        reply("${message.author.name} Request [h${numDice}]: ${rolls.joinToString(", ")} : $success success")
    }

    override suspend fun PrivateMessageContext.invoke() {
        var numDice = parseArgument(argumentText)
        var dicesToRoll = numDice
        val numFaces = 6
        val rolls = arrayListOf<List<Int>>()
        var success = 0
        while (dicesToRoll > 0) {
            val roll = rollDices(dicesToRoll, numFaces)
            rolls.add(roll)
            dicesToRoll = roll.count{it == 6}
            success += roll.count{ roll -> roll > 2 }
        }
        reply("${message.author.name} Request [h${numDice}]: ${rolls.joinToString(", ")} : $success success")
    }

    private fun parseArgument(argumentText: String): Int {
        val numDice = argumentText.toIntOrNull()
            ?: throw IllegalArgumentException("Invalid number of dices")
        if (numDice < 1) {
            throw IllegalArgumentException("Number of dices must be at least 1")
        }
        if (numDice > 50) {
            throw IllegalArgumentException("Number of dices must be at maximum 50")
        }
        return numDice
    }

    private fun rollDices(numDice: Int, numFaces: Int): List<Int> {
        return (1..numDice).map { random.nextInt(numFaces) + 1 }
    }

    override fun HelpContext.provideHelp() {
        addUsage("[Number of dices]")
        addDescription("roll the dices, do it again for the 6s, count the success")
    }
}

