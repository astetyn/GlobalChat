package me.astetyne.commands;

import me.astetyne.ChatPrefabrics;
import me.astetyne.Main;
import me.astetyne.Permissions;
import me.astetyne.playerdata.GPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class SocialSpyCommand extends Command {

	public SocialSpyCommand() {
		super("socialspy", Permissions.SOCIAL_SPY, "ss");
	}

	@Override
	public void execute(CommandSender commandSender, String[] args) {
		
		if(!(commandSender instanceof ProxiedPlayer)) {
			commandSender.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.EXECUTE_PLAYER_ONLY));
			return;
		}
		
		ProxiedPlayer pp = (ProxiedPlayer) commandSender;
		GPlayer pd = Main.getPlayerData(pp);

		if(pd.hasSocialSpy()) {
			pp.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.SOCIAL_SPY + ChatColor.RED + "deactivated."));
			pd.setSocialSpy(false);
		}else {
			pp.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.SOCIAL_SPY + ChatColor.GREEN + "activated."));
			pd.setSocialSpy(true);
		}
		
		return;
	}

}
