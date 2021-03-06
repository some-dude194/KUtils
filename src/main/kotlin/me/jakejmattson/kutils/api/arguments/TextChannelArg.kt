package me.jakejmattson.kutils.api.arguments

import me.jakejmattson.kutils.api.dsl.arguments.*
import me.jakejmattson.kutils.api.dsl.command.CommandEvent
import me.jakejmattson.kutils.api.extensions.jda.tryRetrieveSnowflake
import me.jakejmattson.kutils.api.extensions.stdlib.trimToID
import net.dv8tion.jda.api.entities.TextChannel

open class TextChannelArg(override val name: String = "Text Channel", private val allowsGlobal: Boolean = false) : ArgumentType<TextChannel>() {
    companion object : TextChannelArg()

    override fun convert(arg: String, args: List<String>, event: CommandEvent<*>): ArgumentResult<TextChannel> {
        val channel = event.discord.jda.tryRetrieveSnowflake {
            it.getTextChannelById(arg.trimToID())
        } as TextChannel? ?: return ArgumentResult.Error("Couldn't retrieve $name from $arg.")

        if (!allowsGlobal && channel.guild.id != event.guild?.id)
            return ArgumentResult.Error("$name must be from this guild.")

        return ArgumentResult.Success(channel)
    }

    override fun generateExamples(event: CommandEvent<*>) = listOf(event.channel.id)
}