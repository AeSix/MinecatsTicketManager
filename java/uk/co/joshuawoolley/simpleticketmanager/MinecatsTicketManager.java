/* originally:
package uk.co.joshuawoolley.simpleticketmanager;
*/
package com.aesix.minecatsticketmanager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
/* changing for my OCD/sanity
import uk.co.joshuawoolley.simpleticketmanager.command.CommandHandler;
import uk.co.joshuawoolley.simpleticketmanager.database.MySQL;
import uk.co.joshuawoolley.simpleticketmanager.database.Queries;
import uk.co.joshuawoolley.simpleticketmanager.database.SQLite;
import uk.co.joshuawoolley.simpleticketmanager.listeners.PlayerJoin;
import uk.co.joshuawoolley.simpleticketmanager.ticketsystem.TicketManager;
*/
import com.aesix.minecatsticketmanager.command.CommandHandler;
import com.aesix.minecatsticketmanager.database.MySQL;
import com.aesix.minecatsticketmanager.database.Queries;
import com.aesix.minecatsticketmanager.database.SQLite;
import com.aesix.minecatsticketmanager.listeners.PlayerJoin;
import com.aesix.minecatsticketmanager.ticketsystem.TicketManager;
/**
 * @author Josh Woolley
 */
public class MinecatsTicketManager extends JavaPlugin {
	
	public HashMap<String, String> messageData = new HashMap<String, String>();
	private Connection connection = null;
	private Messages messages;
	private TicketManager manager;

	/**
	 * Enable method
	 */
	public void onEnable() {
		getLogger().info("Minecats Ticket Manager is starting up. This may take a while!");

		this.saveDefaultConfig();
		
		messages = new Messages(this);
		messageData = messages.getMessageData();

		if(this.getConfig().getBoolean("mysql")){
			String hostname = this.getConfig().getString("hostname");
			String port = this.getConfig().getString("port");
			String database = this.getConfig().getString("database");
			String username = this.getConfig().getString("username");
			String password = this.getConfig().getString("password");
			MySQL MySQL = new MySQL(this, hostname, port, database, username, password);
			try {
				connection = MySQL.openConnection();
				createMySQLTables();	
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			SQLite sqlite = new SQLite(this, "/tickets.db");
			try {
				connection = sqlite.openConnection();
				createSQLiteTables();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		manager = new TicketManager(this, new Queries(connection), this.getConfig().getInt("maxtickets"));
		try {
			manager.loadInTickets();
		} catch (SQLException e) {
			getLogger().severe("There was a error loading in the tickets from the database!");
			e.printStackTrace();
		}
		this.getCommand("report").setExecutor(new CommandHandler(this, manager));
		this.getCommand("submit").setExecutor(new CommandHandler(this, manager));
		this.getCommand("ticket").setExecutor(new CommandHandler(this, manager));
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(new PlayerJoin(this, manager), this);
		manager.startTask();
		
		getLogger().info("Minecats Ticket Managers has been enabled!");
	}
	
	/**
	 * Disable method
	 */
	public void onDisable() {
		manager.onDisableUpdate();
		try{
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		getLogger().info("Minecats Ticket Manager has been disabled");
	}
	
	/**
	 * Update messages hashmap with newest changes
	 */
	public void reloadMessages() {
		messages.loadMessages();
	}
	
	private void createMySQLTables() {
		Queries query = new Queries(connection);
		boolean created = query.createMySQLTable();
		if(!created) {
		getLogger().info("Error while creating MySQL database table. Do you have the correct database details in the config?");
			Bukkit.getPluginManager().disablePlugin(this);
		}
	}
	
	private void createSQLiteTables() {
		Queries query = new Queries(connection);
		boolean created = query.createSQLiteTable();
		if(!created) {
		getLogger().info("Error while creating SQLite database table. Please report this to the plugin developer");
			Bukkit.getPluginManager().disablePlugin(this);
		}
	}
}
