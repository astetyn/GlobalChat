package main.commands;

import main.Main;
import main.Permissions;
import main.config.GlobalChatConfiguration;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class ReloadCommand extends Command {

	public ReloadCommand() {
		super("gcreload",Permissions.RELOAD,"globalchatreload");
	}

	@Override
	public void execute(CommandSender arg0, String[] arg1) {
		
		GlobalChatConfiguration.load(Main.plugin);
		arg0.sendMessage(new TextComponent("Config reloaded!"));
		
	}

}
