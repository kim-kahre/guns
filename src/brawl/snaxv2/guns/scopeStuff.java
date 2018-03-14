package brawl.snaxv2.guns;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class scopeStuff {
	public void updateScope(Player player) {
		if (!brawl.snaxv2.guns.main.guns[player.getItemInHand().getTypeId()].is) {
			metaData meta = new metaData();
			meta.setMetadata(player, "scoped", 0);
		}else {
			metaData meta = new metaData();
			if ((int)meta.getMetadata(player, "scoped") == 1) {
				player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1, brawl.snaxv2.guns.main.guns[player.getItemInHand().getTypeId()].scopeLevel));
			}
		}	
	}
}
