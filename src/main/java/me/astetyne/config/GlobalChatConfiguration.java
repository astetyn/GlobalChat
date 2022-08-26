package me.astetyne.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.google.common.io.ByteStreams;

import me.astetyne.Main;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class GlobalChatConfiguration {

	public static Configuration config;
	
	public static void load(Plugin plugin) {
		
		try {
			if (!plugin.getDataFolder().exists()) {
	            plugin.getDataFolder().mkdir();
	        }
			
			File file = new File(plugin.getDataFolder(), "config.yml");
	
	        if (!file.exists()) {
	            file.createNewFile();
	            try (InputStream is = plugin.getResourceAsStream("config.yml");
	            OutputStream os = new FileOutputStream(file)) {
	    			ByteStreams.copy(is, os);
	       		}
	        }
	        
			config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
		} catch (IOException e) {
			e.printStackTrace();
			Main.LOG.warning("-----------------------------");
			Main.LOG.warning("Config in GlobalChat is corrupted.");
			Main.LOG.warning("Please repair the me.astetyne.config or delete me.astetyne.config file. Plugin will create new one.");
			Main.LOG.warning("-----------------------------");
		}
	}

}
