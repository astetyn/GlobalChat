package main.commands;

import main.ChatPrefabrics;
import main.Main;
import main.Permissions;
import main.playerdata.GPlayer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class MuteAllCommand extends Command {

	public MuteAllCommand() {
		super("muteall", Permissions.MUTE_ALL, "silenceall");
	}

	@Override
	public void execute(CommandSender commandSender, String[] args) {
	
		if(!(commandSender instanceof ProxiedPlayer)) {
			commandSender.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.EXECUTE_PLAYER_ONLY));
			return;
		}
		
		ProxiedPlayer pp = (ProxiedPlayer) commandSender;
		GPlayer pd = Main.getPlayerData(pp);
		
		for(GPlayer pdd : Main.gPlayers) {
			if(pdd.equals(pd)) {
				continue;
			}
			pdd.setMuted(true);
			MuteCommand.mutedPlayersUUIDs.add(pdd.getProxiedPlayer().getUniqueId());
			pdd.getProxiedPlayer().sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.SILENCE + "All players have been silenced. Your messages will not be shown."));
		}
		pp.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.SILENCE + "All players except you have been silenced."));
		Main.logMessage("[Mute] All players have been muted by user: "+pp.getName());
	}
}
