package main;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import main.commands.AdminChatCommand;
import main.commands.EventInvitationCommand;
import main.commands.IgnoreCommand;
import main.commands.KickCommand;
import main.commands.MuteAllCommand;
import main.commands.MuteCommand;
import main.commands.NickCommand;
import main.commands.PrivateMessageBackCommand;
import main.commands.PrivateMessageCommand;
import main.commands.ReloadCommand;
import main.commands.SetcolorCommand;
import main.commands.SocialSpyCommand;
import main.commands.UnmuteCommand;
import main.config.GlobalChatConfiguration;
import main.eventinvitation.EventJoinCommand;
import main.message.GlobalMessage;
import main.playerdata.LoaderRunnable;
import main.playerdata.LuckPermsManager;
import main.playerdata.GPlayer;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginDescription;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class Main extends Plugin implements Listener {
	
	public static final Logger LOG = BungeeCord.getInstance().getLogger();
	public static List<GPlayer> gPlayers = new ArrayList<GPlayer>();
	public static Plugin plugin;
	
	@Override
    public void onEnable() {
		
		Plugin luckPerms = getProxy().getPluginManager().getPlugin("LuckPerms");
		
		if(luckPerms==null) {
			LOG.warning("-----------------------------");
			LOG.warning("GlobalChat requires LuckPerms installed.");
			LOG.warning("Plugin will NOT WORK please install LuckPerms v5 and restart the server.");
			LOG.warning("-----------------------------");
			return;
		}
		
		plugin = this;
		GlobalChatConfiguration.load(this);
		
        BungeeCord.getInstance().getScheduler().schedule(this, new LoaderRunnable(), 5, 5,TimeUnit.SECONDS);
        
        getProxy().getPluginManager().registerListener(this, this); 
        
        getProxy().getPluginManager().registerCommand(this, new AdminChatCommand());
        getProxy().getPluginManager().registerCommand(this, new KickCommand());
        getProxy().getPluginManager().registerCommand(this, new MuteCommand());
        getProxy().getPluginManager().registerCommand(this, new MuteAllCommand());
        getProxy().getPluginManager().registerCommand(this, new PrivateMessageBackCommand());
        getProxy().getPluginManager().registerCommand(this, new PrivateMessageCommand());
        getProxy().getPluginManager().registerCommand(this, new SetcolorCommand());
        getProxy().getPluginManager().registerCommand(this, new SocialSpyCommand());
        getProxy().getPluginManager().registerCommand(this, new IgnoreCommand());
        getProxy().getPluginManager().registerCommand(this, new UnmuteCommand());
        getProxy().getPluginManager().registerCommand(this, new EventInvitationCommand());
        getProxy().getPluginManager().registerCommand(this, new EventJoinCommand());
        getProxy().getPluginManager().registerCommand(this, new NickCommand());
        getProxy().getPluginManager().registerCommand(this, new ReloadCommand());
        
    }
	
	@Override
	public void onDisable() {
		
		for(GPlayer gp : gPlayers) {
			LuckPermsManager.saveAllDataFromPlayer(gp);
		}
		
	}

	@EventHandler
	public void onChat(ChatEvent e) {
		
		if(e.isCancelled()) {
			return;
		}

		if(!(e.getSender() instanceof ProxiedPlayer)) {
			return;
		}
		
		String line = e.getMessage();
		
		if(line.startsWith("/")) {
			return;
		}
		
		ProxiedPlayer pp = (ProxiedPlayer) e.getSender();
		GPlayer pd = getPlayerData(pp);
		
		if(pd==null) {
			return;
		}
		
		GlobalMessage gs = new GlobalMessage(pd, line);
		gs.wantsToSendMessage();

		e.setCancelled(true);
	}
	
	public static GPlayer getPlayerData(ProxiedPlayer pp) {
		
		for(GPlayer gp : gPlayers) {
			if(gp.getProxiedPlayer().equals(pp)) {
				return gp;
			}
		}
		return null;
	}

	@EventHandler
	public void onPostJoin(PostLoginEvent e) {
		
		GPlayer gPlayer = new GPlayer(e.getPlayer());
		gPlayers.add(gPlayer);
		
		BungeeCord.getInstance().getScheduler().schedule(this,  new Runnable() {
	        @Override
	        public void run() {
	        	if(!e.getPlayer().isConnected()) {
	        		return;
	        	}
	        	gPlayer.setJoinedServer(true);
	        	LuckPermsManager.loadAllDataToPlayer(gPlayer);
        		
        		for(ProxiedPlayer p : ProxyServer.getInstance().getPlayers()){
        			p.sendMessage(TextComponent.fromLegacyText(ChatColor.DARK_GRAY+"["+ChatColor.GRAY+"WoF"+ChatColor.DARK_GRAY+"] "+ChatColor.WHITE+e.getPlayer().getName()+ChatColor.GRAY+" sa pripojil/a do hry!"));
        		}

        		if(!GlobalChatConfiguration.config.getBoolean("showVersionMessage")) {
        			return;
        		}

        		PluginDescription pd = plugin.getDescription();
	        	String version = pd.getVersion();
        		TextComponent globalChatVersionInfo = new TextComponent();
        		globalChatVersionInfo.addExtra(ChatColor.GRAY+"-----------------------------------------\n");
        		globalChatVersionInfo.addExtra(ChatColor.GREEN+"            Running "+ChatColor.WHITE+"GlobalChat "+ChatColor.GREEN+ChatColor.BOLD+version+"\n");
        		globalChatVersionInfo.addExtra(ChatColor.GRAY+"-----------------------------------------");
        		e.getPlayer().sendMessage(globalChatVersionInfo);
	        }
	    }, 2,TimeUnit.SECONDS);
	}

	@EventHandler
	public void onLeave(PlayerDisconnectEvent e) {

		GPlayer gPlayer = getPlayerData(e.getPlayer());
		gPlayers.remove(gPlayer);
		
		if(!gPlayer.isJoinedServer()) {
			return;
		}
		
		for(ProxiedPlayer p : ProxyServer.getInstance().getPlayers()){
	         p.sendMessage(TextComponent.fromLegacyText(ChatColor.DARK_GRAY+"["+ChatColor.GRAY+"WoF"+ChatColor.DARK_GRAY+"] "+ChatColor.WHITE+e.getPlayer()+ChatColor.GRAY+" sa odpojil/a z hry."));
	    }
		LuckPermsManager.saveAllDataFromPlayer(gPlayer);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
    public void onTabComplete(TabCompleteEvent e) {
		
        String[] args = e.getCursor().split(" ");
        final String checked = (args.length > 0 ? args[args.length - 1] : e.getCursor()).toLowerCase();
        
		for (ProxiedPlayer player : BungeeCord.getInstance().getPlayers()) {
            if (player.getName().toLowerCase().startsWith(checked)) {
                e.getSuggestions().add(player.getName());
            }
        }
		
    }
	
	public static void logMessage(String msg) {
		LOG.info(ChatColor.WHITE+"["+ChatColor.GREEN+"GCH"+ChatColor.WHITE+"] "+ChatColor.RESET+msg);
	}
}
