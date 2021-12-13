package net.peacefulcraft.borough;

import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;
import net.peacefulcraft.borough.commands.ClaimCommand;
import net.peacefulcraft.borough.commands.UnclaimCommand;
import net.peacefulcraft.borough.config.MainConfiguration;
import net.peacefulcraft.borough.listeners.ChunkCacheEventListeners;
import net.peacefulcraft.borough.listeners.PlayerCacheEventListeners;
import net.peacefulcraft.borough.listeners.PlayerMovementListener;
import net.peacefulcraft.borough.storage.BoroughClaimStore;
import net.peacefulcraft.borough.storage.SQLQueries;
import net.peacefulcraft.borough.storage.UUIDCache;

public class Borough extends JavaPlugin {

	public static final String messagingPrefix = ChatColor.GREEN + "[" + ChatColor.BLUE + "PCN" + ChatColor.GREEN + "]"
			+ ChatColor.RESET;

	private static Borough _this;
		public static Borough _this() { return _this; }

	private static MainConfiguration configuration;
		public static MainConfiguration getConfiguration() { return configuration; }

	private static BoroughClaimStore claimStore;
		public static BoroughClaimStore getClaimStore() { return claimStore; }

	private static UUIDCache uuidCache;
		public static UUIDCache getUUIDCache() { return uuidCache; }
	/**
	 * Called when Bukkit server enables the plguin
	 * For improved reload behavior, use this as if it was the class constructor
	 */
	public void onEnable() {
		this._this = this;
		// Save default config if one does not exist. Then load the configuration into
		// memory
		configuration = new MainConfiguration();

		SQLQueries.setup();

		uuidCache = new UUIDCache();
		claimStore = new BoroughClaimStore();

		this.setupCommands();
		this.setupEventListeners();
	}

	public void logDebug(String message) {
		if (configuration.isDebugEnabled()) {
			this.getServer().getLogger().log(Level.INFO, message);
		}
	}

	public void logNotice(String message) {
		this.getServer().getLogger().log(Level.INFO, message);
	}

	public void logWarning(String message) {
		this.getServer().getLogger().log(Level.WARNING, message);
	}

	public void logSevere(String message) {
		this.getServer().getLogger().log(Level.SEVERE, message);
	}

	/**
	 * Called whenever Bukkit server disableds the plugin
	 * For improved reload behavior, try to reset the plugin to it's initaial state
	 * here.
	 */
	public void onDisable() {
		this.getServer().getScheduler().cancelTasks(this);
		SQLQueries.teardown();
	}

	private void setupCommands() {
		ClaimCommand claimCommand = new ClaimCommand();
		this.getCommand("claim").setExecutor(claimCommand);
		this.getCommand("claim").setTabCompleter(claimCommand);
		this.getCommand("unclaim").setExecutor(new UnclaimCommand());		
	}

	private void setupEventListeners() {
		this.getServer().getPluginManager().registerEvents(new ChunkCacheEventListeners(), this);
		this.getServer().getPluginManager().registerEvents(new PlayerCacheEventListeners(), this);
		this.getServer().getPluginManager().registerEvents(new PlayerMovementListener(), this);
	}
}