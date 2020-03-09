package main.commands;

import main.ChatPrefabrics;
import main.Main;
import main.playerdata.PlayerData;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class MuteAllCommand extends Command {

	public MuteAllCommand() {
		super("muteall", "globalchat.muteall", "silenceall");
	}

	@Override
	public void execute(CommandSender commandSender, String[] args) {
	
		if(!(commandSender instanceof ProxiedPlayer)) {
			commandSender.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.EXECUTE_PLAYER_ONLY));
			return;
		}
		
		ProxiedPlayer pp = (ProxiedPlayer) commandSender;
		PlayerData pd = Main.getPlayerData(pp);
		
		for(PlayerData pdd : Main.playerDataList) {
			if(pdd.equals(pd)) {
				continue;
			}
			pdd.setMuted(true);
			pdd.getProxiedPlayer().sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.SILENCE + "All players have been silenced. Your messages will not be shown."));
		}
		pp.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.SILENCE + "All players except you have been silenced."));
		Main.LOG.info("All players have been muted by user: "+pp.getName());
	}
}
