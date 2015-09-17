
package me.Game_Crytus.EventsRunGC.Events;

import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;

public class Loadout {
	private String name;
	private String id;
	private String permission;
	private int priority;
	
	private Inventory inventory;
	private EntityEquipment equipment;
	
	
	public void setName (String name) {
		this.name = name;
	}
	
	public void setId (String id) {
		this.id = id;
	}
	
	public void setPermission (String permission) {
		this.permission = permission;
	}
	
	public void setPriority (int priority) {
		this.priority = priority;
	}
	
	public void setInventory (Inventory inventory) {
		this.inventory = inventory;
	}
	
	public void setEquipment (EntityEquipment equipment) {
		this.equipment = equipment;
	}
	
	public String getName () {
		return this.name;
	}
	
	public String getId () {
		return this.id;
	}
	
	public String getPermission () {
		return this.permission;
	}
	
	public int getPriority () {
		return this.priority;
	}
	
	public Inventory getInventory () {
		return this.inventory;
	}
		
	public EntityEquipment getEquipment () {
		return this.equipment;
	}
}

















