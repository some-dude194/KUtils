package me.jakejmattson.kutils.api.arguments

import me.jakejmattson.kutils.api.dsl.arguments.*
import me.jakejmattson.kutils.api.dsl.command.CommandEvent
import me.jakejmattson.kutils.api.extensions.jda.tryRetrieveSnowflake
import me.jakejmattson.kutils.api.extensions.stdlib.trimToID
import net.dv8tion.jda.api.entities.User

open class UserArg(override val name: String = "User", private val allowsBot: Boolean = false) : ArgumentType<User>() {
    companion object : UserArg()

    override fun convert(arg: String, args: List<String>, event: CommandEvent<*>): ArgumentResult<User> {
        val user = event.discord.jda.tryRetrieveSnowflake {
            it.retrieveUserById(arg.trimToID()).complete()
        } as User? ?: return ArgumentResult.Error("Couldn't retrieve $name from $arg.")

        if (!allowsBot && user.isBot)
            return ArgumentResult.Error("$name cannot be a bot.")

        return ArgumentResult.Success(user)
    }

    override fun generateExamples(event: CommandEvent<*>) = listOf(event.author.id)
}
