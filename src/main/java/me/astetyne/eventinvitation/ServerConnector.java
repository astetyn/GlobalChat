package me.astetyne.eventinvitation;

import java.util.concurrent.TimeUnit;

import me.astetyne.Main;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.scheduler.ScheduledTask;

public class ServerConnector {

	ScheduledTask task;
	
	public ServerConnector(ProxiedPlayer pp, Server server, String cmd) {
		
		if(!pp.getServer().getInfo().equals(server.getInfo())) {
			pp.connect(server.getInfo());
		}	
		
		task = ProxyServer.getInstance().getScheduler().schedule(Main.plugin, new Runnable() {
            @Override
            public void run() {
                if(pp.getServer().getInfo().equals(server.getInfo())) {
                	pp.chat(cmd);
                	task.cancel();
                	return;
                }
            }
        }, 2000, 1000, TimeUnit.MILLISECONDS);
	}
}
