package dev.arbjerg.ukulele.command

import dev.arbjerg.ukulele.features.HelpContext
import dev.arbjerg.ukulele.jda.Command
import dev.arbjerg.ukulele.jda.CommandContext
import org.springframework.stereotype.Component
import dev.arbjerg.ukulele.jda.PrivateMessageContext


@Component
class ResumeCommand : Command ("resume") {
    override suspend fun CommandContext.invoke() {
        if (!player.isPaused) return reply("Player is already playing.")

        player.resume()
        reply("Playback has been resumed.")
    }

    override suspend fun PrivateMessageContext.invoke() {
        reply("this command in PM isn't supported yet")
    }

    override fun HelpContext.provideHelp() {
        addUsage("")
        addDescription("Resumes the playback.")
    }
}