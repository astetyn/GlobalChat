package main.commands;

import java.util.Timer;
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

public class MuteCommand extends Command {

	public MuteCommand() {
		super("mute", "globalchat.mute", "silence");
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
			pp.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.WARNING + ChatColor.RED + "/mute <nick> [duration in secs]"));
		}else if(tokenCount==1) {
	
			String playerName = args[0];
			
			if(ProxyServer.getInstance().getPlayer(playerName)==null) {
				pp.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.PLAYER_NOT_ONLINE));
				return;
			}
			
			ProxiedPlayer pp2 = ProxyServer.getInstance().getPlayer(playerName);
			PlayerData mutedPlayerData = Main.getPlayerData(pp2);
			
			if(mutedPlayerData.isMuted()) {
				pp.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.WARNING + ChatColor.GRAY + "Player is already muted. For unmute type /unmute <nick>"));
				return;
			}
			
			mutedPlayerData.setMuted(true);
			pp.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.SILENCE + ChatColor.GRAY + "Player is now silenced. His messages will not be shown."));
			mutedPlayerData.getProxiedPlayer().sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.SILENCE +
					ChatColor.GRAY + "You have been silenced. Your messages will not be shown."));
			
			Main.LOG.info("User "+pp.getName()+ " muted user "+playerName);
			
		}else if(tokenCount==2) {
			
			String playerName = args[0];
			
			if(ProxyServer.getInstance().getPlayer(playerName)==null) {
				pp.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.PLAYER_NOT_ONLINE));
				return;
			}
			
			PlayerData tempMutedPlayerData = null;
			
			for(PlayerData pdd : Main.playerDataList) {
				if(pdd.getProxiedPlayer().getName().equals(playerName)) {
					tempMutedPlayerData = pdd;
				}
			}
			
			final PlayerData mutedPlayerData = tempMutedPlayerData;
			
			String timeStr = args[1];
			if(!isNumeric(timeStr)) {
				pp.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.WARNING + ChatColor.RED + "/mute <nick> [duration in secs]"));
				return;
			}
			double time = Double.parseDouble(timeStr);
			
			if(mutedPlayerData.isMuted()) {
				pp.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.WARNING + ChatColor.GRAY + "Player is already muted. For unmute type /unmute <nick>"));
				return;
			}
			
			mutedPlayerData.setMuteTimestamp(System.currentTimeMillis());
			mutedPlayerData.setMuteDuration(time);
			mutedPlayerData.setMuted(true);
			pp.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.SILENCE + ChatColor.GRAY + "Player was silenced for "+time+" seconds."));
			mutedPlayerData.getProxiedPlayer().sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.SILENCE +
					ChatColor.GRAY + "You have been silenced for "+time+" seconds. Your messages will not be shown."));

			Main.LOG.info("User "+pp.getName()+ " muted user "+playerName+" for "+time+" seconds.");
			
			Timer timer = new Timer();
			TimerTask ts = new TimerTask() {

				@Override
				public void run() {
					mutedPlayerData.setMuted(false);
					mutedPlayerData.getProxiedPlayer().sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.SILENCE +
							ChatColor.GRAY + "You are unsilenced now. Silence time passed."));
					Main.LOG.info("User "+playerName+" unmuted due to time reach.");
				}
			};
			timer.schedule(ts, (int)(time*1000));
			mutedPlayerData.setTempMuteTask(ts);
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
