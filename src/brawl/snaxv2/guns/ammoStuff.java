package brawl.snaxv2.guns;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ammoStuff {
	public int itemAmount (Player player, int id) {
		int count = 0;
		for (ItemStack is : player.getInventory()) {
			if (is != null && is.getTypeId() == id) {
				count+=is.getAmount();
			}
		}
		return count;
	}
	public void consumeItem(Player player, int id) {
		for (ItemStack is : player.getInventory()) {
			if (is != null && is.getTypeId() == id) {
				if (is.getAmount()==1) {
					is.setAmount(0);
					return;
				}else {
					is.setAmount(is.getAmount()-1);
					return;
				}
			}
		}
	}
}
