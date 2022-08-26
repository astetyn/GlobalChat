package me.astetyne.commands;

import me.astetyne.ChatPrefabrics;
import me.astetyne.Main;
import me.astetyne.Permissions;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class KickCommand extends Command {

	public KickCommand() {
		super("gkick",Permissions.KICK);
	}

	@Override
	public void execute(CommandSender commandSender, String[] args) {
		
		if(!(commandSender instanceof ProxiedPlayer)) {
			commandSender.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.EXECUTE_PLAYER_ONLY));
			return;
		}
		
		ProxiedPlayer pp = (ProxiedPlayer) commandSender;
		
		int tokenCount = args.length;
		
		if(tokenCount==0) {
			pp.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.WARNING + ChatColor.RED + "/gkick <nick> [reason]"));
			return;
		}else if(tokenCount>0) {
			
			String name = args[0];
			
			if(ProxyServer.getInstance().getPlayer(name)==null) {
				pp.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.PLAYER_NOT_ONLINE));
				return;
			}
			ProxiedPlayer p = ProxyServer.getInstance().getPlayer(name);
			
			if(tokenCount>1) {
				String reason = "";
				for(int i = 1;i<tokenCount;i++) {
					reason += args[i]+" ";
				}
				p.disconnect(TextComponent.fromLegacyText(reason));
				Main.logMessage("[Kick] User "+pp.getName()+" kicked "+name+" from server. Reason: "+reason);
			}else {
				String reason = "Bol/a si vyhodený/á zo servera.";
				p.disconnect(TextComponent.fromLegacyText(reason));
				Main.logMessage("[Kick] User "+pp.getName()+" kicked "+name+" from server. Reason: "+reason);
			}
		}
		
	}

}
