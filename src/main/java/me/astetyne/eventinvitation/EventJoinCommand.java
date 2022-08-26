package me.astetyne.eventinvitation;

import me.astetyne.ChatPrefabrics;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class EventJoinCommand extends Command {

	public EventJoinCommand() {
		super("eventjoin");
	}

	@Override
	public void execute(CommandSender commandSender, String[] args) {
		
		if(!(commandSender instanceof ProxiedPlayer)) {
			commandSender.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.EXECUTE_PLAYER_ONLY));
			return;
		}
		EventInvitation.onEventJoin((ProxiedPlayer) commandSender);
	}

}
