package me.astetyne.commands;

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

public class UnmuteCommand extends Command {

	public UnmuteCommand() {
		super("unmute", Permissions.MUTE, "unsilence");
	}

	@Override
	public void execute(CommandSender commandSender, String[] args) {
	
		if(!(commandSender instanceof ProxiedPlayer)) {
			commandSender.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.EXECUTE_PLAYER_ONLY));
			return;
		}
		
		ProxiedPlayer pp = (ProxiedPlayer) commandSender;
		
		int tokenCount = args.length;
		
		if(tokenCount!=1) {
			pp.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.WARNING + ChatColor.RED + "/unmute <nick>"));
			return;
		}
		
		String playerName = args[0];
		
		if(ProxyServer.getInstance().getPlayer(playerName)==null) {
			pp.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.PLAYER_NOT_ONLINE));
			return;
		}
		
		ProxiedPlayer pp2 = ProxyServer.getInstance().getPlayer(playerName);
		GPlayer unmutedPlayerData = Main.getPlayerData(pp2);
		
		if(unmutedPlayerData.isMuted()) {
	
			unmutedPlayerData.setMuted(false);
			MuteCommand.mutedPlayersUUIDs.remove(unmutedPlayerData.getProxiedPlayer().getUniqueId());

			pp.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.SILENCE + ChatColor.GRAY + "Player was unmuted. His messages will be shown."));
			unmutedPlayerData.getProxiedPlayer().sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.SILENCE +
					ChatColor.GRAY + "You are unsilenced. Your messages will be shown now."));
			
			Main.logMessage("[Mute] User "+pp.getName()+ " unmuted user "+playerName);
		}else {
			pp.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.WARNING + ChatColor.RED + "Player is not silenced. Please use - /mute <nick>"));
		}
	}
}
