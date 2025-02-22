package dev.arbjerg.ukulele.command

import dev.arbjerg.ukulele.features.HelpContext
import dev.arbjerg.ukulele.jda.Command
import dev.arbjerg.ukulele.jda.CommandContext
import org.springframework.stereotype.Component
import dev.arbjerg.ukulele.jda.PrivateMessageContext


@Component
class RepeatCommand : Command ("repeat", "loop", "r") {
    override suspend fun CommandContext.invoke() {
        player.isRepeating = !player.isRepeating
        if (player.isRepeating) {reply("Repeating is now enabled.")}
        else{reply("Repeating is now disabled.")}
    }

    override suspend fun PrivateMessageContext.invoke() {
        reply("this command in PM isn't supported yet")
    }

    override fun HelpContext.provideHelp() {
        addUsage("")
        addDescription("Toggles the repeat of the queue.")
    }
}
