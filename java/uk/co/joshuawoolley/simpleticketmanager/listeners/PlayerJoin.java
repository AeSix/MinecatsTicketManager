package com.aesix.minecatsticketmanager.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.aesix.minecatsticketmanager.SimpleTicketManager;
import com.aesix.minecatsticketmanager.ticketsystem.TicketManager;

/**
 * @author Josh Woolley
 */
public class PlayerJoin implements Listener{
	
	private MinecatsTicketManager plugin;
	private TicketManager manager;
	
	private String tag = "";
	
	/**
	 * PlayerJoin Constructor
	 * 
	 * @param instance
	 * 			SimpleTicketManager instance
	 * @param manager
	 * 			TicketManager instance
	 */
	public PlayerJoin (MinecatsTicketManager instance, TicketManager manager) {
		plugin = instance;
		this.manager = manager;
		tag = ChatColor.translateAlternateColorCodes('&',plugin.messageData.get("tag"));
	}
	
	/**
	 * PlayerJoin Event
	 * 
	 * @param event
	 * 			PlayerJoinEvent
	 */
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		if (p.hasPermission("ticket.use")) {
			if(plugin.getConfig().getBoolean("adminmessages")){
				int open = manager.getOpenTicketsSize();
				if (open > 0) {
					p.sendMessage(tag + ChatColor.translateAlternateColorCodes('&', translateAmount(plugin.messageData.get("loginNotice"), open)));
				}
			}
		}
	}
	
	private String translateAmount(String message, int amount) {
		if (message.contains("%amount")) {
			String newMessage = message.replace("%amount", "" + amount);
			return newMessage;
		} else {
			return message;
		}
	}

}
