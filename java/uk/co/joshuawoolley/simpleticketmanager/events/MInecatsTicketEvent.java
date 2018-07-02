package com.aesix.minecatsticketmanager.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import com.aesix.minecatsticketmanager.Ticket;

public class MinecatsTicketEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    
    private final Ticket ticket;
    private final String action;

    /**
     *
     * @param action
     * @param ticket
     */
    public SimpleTicketEvent(final String action, final Ticket ticket) {
        this.ticket = ticket;
        this.action = action;
    }

    public Ticket getTicket() {
        return this.ticket;
    }
    
    public String getAction() {
        return action;
    }

    /**
     *
     * @return
     */
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    /**
     *
     * @return
     */
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
