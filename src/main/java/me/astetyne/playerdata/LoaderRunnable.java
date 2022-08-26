package me.astetyne.playerdata;

import me.astetyne.Main;

public class LoaderRunnable implements Runnable {
	
	@Override
	public void run() {
		
		for(GPlayer gPlayer : Main.gPlayers) {
			LuckPermsManager.syncPrefixAndSufix(gPlayer);
		}
		
	}
}
