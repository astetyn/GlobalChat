package main.commands;

import java.util.TimerTask;

import main.ChatPrefabrics;
import main.Main;
import main.playerdata.PlayerData;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class UnmuteCommand extends Command {

	public UnmuteCommand() {
		super("unmute", "globalchat.mute", "unsilence");
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
		
		PlayerData tempUnmutedPlayerData = null;
		
		for(PlayerData pdd : Main.playerDataList) {
			if(pdd.getProxiedPlayer().getName().equals(playerName)) {
				tempUnmutedPlayerData = pdd;
			}
		}
		
		final PlayerData unmutedPlayerData = tempUnmutedPlayerData;
		
		if(unmutedPlayerData.isMuted()) {
	
			unmutedPlayerData.setMuted(false);
			TimerTask ts = unmutedPlayerData.getTempMuteTask();
			if(ts!=null) {
				ts.cancel();
			}
			pp.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.SILENCE + ChatColor.GRAY + "Player was unsilenced. His messages will be shown now."));
			unmutedPlayerData.getProxiedPlayer().sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.SILENCE +
					ChatColor.GRAY + "You have been unsilenced. Your messages will be shown now."));
			
			Main.LOG.info("User "+pp.getName()+ " unmuted user "+playerName);
		}else {
			pp.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.WARNING + ChatColor.RED + "Player is not silenced!. For silence, please use - /mute <nick>"));
		}
	}
}
