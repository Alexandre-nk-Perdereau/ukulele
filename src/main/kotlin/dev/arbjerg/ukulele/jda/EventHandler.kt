package dev.arbjerg.ukulele.jda

import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.StatusChangeEvent
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class EventHandler(private val commandManager: CommandManager) : ListenerAdapter() {

    private val log: Logger = LoggerFactory.getLogger(EventHandler::class.java)

    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        if (event.isWebhookMessage || event.author.isBot) return

        val guild = event.guild
        val member = event.member
        val message = event.message
        val content = message.contentRaw
        val questionRoleName = "Inquisiteur"

        if (member?.roles?.find { it.name == questionRoleName } != null) {
            if (content.contains("?")) {
                val response = if (member.effectiveName != null) {
                    "Non ${member.effectiveName}"
                } else {
                    "Non ${member.user.name}"
                }
                message.channel.sendMessage(response).queue()
            }
        }
        commandManager.onMessage(event.guild, event.channel, event.member!!, event.message)
    }

    override fun onPrivateMessageReceived(event: PrivateMessageReceivedEvent) {
        if (event.author.isBot) return
        commandManager.onPrivateMessage(event.author, event.message)
    }

    override fun onStatusChange(event: StatusChangeEvent) {
        log.info("{}: {} -> {}", event.entity.shardInfo, event.oldStatus, event.newStatus)
    }

    override fun onGuildVoiceUpdate(event: GuildVoiceUpdateEvent) {
        if (event.entity.user == null || event.entity.user!!.isBot) return // ignore bots and null users
        val guild = event.guild
        val channel = event.channelLeft ?: event.channelJoined // get the channel that was left or joined
        if (channel == null) return // ignore if channel is null
        val humanCount = channel.members.filter { !it.user.isBot }.count() // count humans in channel
        if (humanCount == 0) {
            log.info("No humans left in voice channel $channel")
            val audioManager = guild.audioManager // get the audio manager for the guild
            if (audioManager.isConnected && audioManager.connectedChannel == channel) {
                audioManager.closeAudioConnection() // leave the channel if bot is connected
                log.info("Left voice channel $channel")
            }
        }
    }

}