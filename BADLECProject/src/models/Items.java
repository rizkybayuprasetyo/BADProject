package models;

public class Items {
	private String itemID;
	private String itemName;
	private int itemPrice;
	public Items(String itemID, String itemName, int itemPrice) {
		this.itemID = itemID;
		this.itemName = itemName;
		this.itemPrice = itemPrice;
	}
	public String getItemID() {
		return itemID;
	}
	public void setItemID(String itemID) {
		this.itemID = itemID;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public int getItemPrice() {
		return itemPrice;
	}
	public void setItemPrice(int itemPrice) {
		this.itemPrice = itemPrice;
	}
	
	
	
}
