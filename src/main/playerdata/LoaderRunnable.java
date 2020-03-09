package main.playerdata;

import main.Main;

public class LoaderRunnable implements Runnable {
	
	@Override
	public void run() {
		
		for(PlayerData pd : Main.playerDataList) {
			pd.syncPrefix();
		}
		
	}
}
