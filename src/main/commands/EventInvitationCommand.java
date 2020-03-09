package main.commands;

import main.ChatPrefabrics;
import main.eventinvitation.EventInvitation;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class EventInvitationCommand extends Command {

	public EventInvitationCommand() {
		super("event", "globalchat.event", "makeevent");
	}

	@Override
	public void execute(CommandSender commandSender, String[] args) {
		
		if(!(commandSender instanceof ProxiedPlayer)) {
			commandSender.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.EXECUTE_PLAYER_ONLY));
			return;
		}
		
		ProxiedPlayer pp = (ProxiedPlayer) commandSender;
		
		int tokenCount = args.length;
		
		if(tokenCount<2) {
			pp.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.WARNING + ChatColor.RED + "/event <name> <cmd...>"));
			return;
		}else {
			String[] cmdParts = new String[args.length-1];
			for(int i = 1;i<args.length;i++) {
				cmdParts[i-1] = args[i];
			}
			new EventInvitation(pp, pp.getServer(), args[0], cmdParts);
		}
		
	}

	
	
}
