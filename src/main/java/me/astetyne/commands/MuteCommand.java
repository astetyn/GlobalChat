package me.astetyne.commands;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import me.astetyne.ChatPrefabrics;
import me.astetyne.Main;
import me.astetyne.Permissions;
import me.astetyne.playerdata.GPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class MuteCommand extends Command {

	public static Set<UUID> mutedPlayersUUIDs = new LinkedHashSet<>();
	
	public MuteCommand() {
		super("mute", Permissions.MUTE, "silence");
	}

	@Override
	public void execute(CommandSender commandSender, String[] args) {
	
		if(!(commandSender instanceof ProxiedPlayer)) {
			commandSender.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.EXECUTE_PLAYER_ONLY));
			return;
		}
		
		ProxiedPlayer pp = (ProxiedPlayer) commandSender;
		
		int tokenCount = args.length;
		
		if(tokenCount==0||tokenCount>2) {
			pp.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.WARNING + ChatColor.RED + "/mute <nick>"));
		}else if(tokenCount==1) {
	
			String playerName = args[0];
			
			if(ProxyServer.getInstance().getPlayer(playerName)==null) {
				pp.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.PLAYER_NOT_ONLINE));
				return;
			}
			
			ProxiedPlayer pp2 = ProxyServer.getInstance().getPlayer(playerName);
			GPlayer mutedPlayerData = Main.getPlayerData(pp2);
			
			if(mutedPlayerData.isMuted()) {
				pp.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.WARNING + ChatColor.GRAY + "Player is already muted. For unmute - /unmute <nick>"));
				return;
			}
			
			mutedPlayerData.setMuted(true);
			mutedPlayersUUIDs.add(mutedPlayerData.getProxiedPlayer().getUniqueId());
			
			pp.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.SILENCE + ChatColor.GRAY + "Player is silenced. His messages will not be shown."));
			mutedPlayerData.getProxiedPlayer().sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.SILENCE +
					ChatColor.GRAY + "You are silenced. Your messages will not be shown."));
			
			Main.logMessage("[Mute] User "+pp.getName()+ " muted user "+playerName);
			
		}
	}
	
	public static boolean isNumeric(String strNum) {
	    try {
	        @SuppressWarnings("unused")
			double d = Double.parseDouble(strNum);
	    } catch (NumberFormatException | NullPointerException nfe) {
	        return false;
	    }
	    return true;
	}
}
