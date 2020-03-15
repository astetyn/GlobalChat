package main.commands;

import main.ChatPrefabrics;
import main.Main;
import main.playerdata.GPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class IgnoreCommand extends Command {

	public IgnoreCommand() {
		super("ignore");
	}

	@Override
	public void execute(CommandSender commandSender, String[] args) {
		
		if(!(commandSender instanceof ProxiedPlayer)) {
			commandSender.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.EXECUTE_PLAYER_ONLY));
			return;
		}
		
		ProxiedPlayer pp = (ProxiedPlayer) commandSender;
		GPlayer pd = Main.getPlayerData(pp);
		
		int tokenCount = args.length;
		
		if(tokenCount==0) {
			pp.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.WARNING + ChatColor.RED + "/ignore <nick>"));
			return;
		}else if(tokenCount>0) {
			
			String name = args[0];
			
			if(pd.getIgnoredPlayersNames().contains(name)) {
				pd.getIgnoredPlayersNames().remove(name);
				pp.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.SILENCE + ChatColor.GRAY + "You are no longer ignoring this player."));
			}else {
				pd.getIgnoredPlayersNames().add(name);
				pp.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.SILENCE + ChatColor.GRAY + "Messages from this player will be hidden for you."));
			}	
			
			Main.LOG.info("User "+pp.getName()+" used /ignore for "+name);
			
		}
		
	}

}