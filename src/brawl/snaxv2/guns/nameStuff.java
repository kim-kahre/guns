package brawl.snaxv2.guns;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class nameStuff {
	public void updateAllGunNames(Player player) {
		for (ItemStack is : player.getInventory()) {
			if (is != null) {
				if (brawl.snaxv2.guns.main.guns[is.getTypeId()].is) {
					ItemMeta meta = is.getItemMeta();
					metaData data = new metaData();
					ammoStuff ammo = new ammoStuff();
					int ammoinclip =  (int) data.getMetadata(player, String.valueOf(is.getTypeId())+"inclip");
					if (ammoinclip >0) {
						if (ammo.itemAmount(player, brawl.snaxv2.guns.main.guns[is.getTypeId()].ammoType)<ammoinclip) {
							ammoinclip = ammo.itemAmount(player, brawl.snaxv2.guns.main.guns[is.getTypeId()].ammoType);
						}	
						meta.setDisplayName(brawl.snaxv2.guns.main.guns[is.getTypeId()].name + " <"+ ammoinclip+"||"+String.valueOf(ammo.itemAmount(player, brawl.snaxv2.guns.main.guns[is.getTypeId()].ammoType)-ammoinclip)+">");
						is.setItemMeta(meta);
					}else {
						meta.setDisplayName(brawl.snaxv2.guns.main.guns[is.getTypeId()].name + " <RELOADING>");
						is.setItemMeta(meta);
					}
				}
			}
		}
	}
}
