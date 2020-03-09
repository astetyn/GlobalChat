package main.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.google.common.io.ByteStreams;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class GlobalChatConfiguration {

	Plugin plugin;
	Configuration config;
	
	public GlobalChatConfiguration(Plugin plugin) {
		this.plugin = plugin;
		try {
			load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void load() throws IOException {
		
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
	}
	
	public void save() throws IOException {
		ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, new File(plugin.getDataFolder(), "config.yml"));
	}
	
	public Object getValue(String key) {
		Object value = config.get(key);
		return value;
	}
}
