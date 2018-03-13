package brawl.snaxv2.guns;

import org.bukkit.entity.Player;

public class timerStuff {
	public void updateTimer(Player player, int id){
		metaData meta = new metaData();
		meta.setMetadata(player, String.valueOf(id) + "timer", (int)(meta.getMetadata(player, String.valueOf(id) +  "timer"))-1);
		if ((int)meta.getMetadata(player, String.valueOf(id) +  "timer")<0 && (int)meta.getMetadata(player, String.valueOf(id) +  "inclip")==0) {
			meta.setMetadata(player, String.valueOf(id) + "inclip", brawl.snaxv2.guns.main.guns[id].clipSize);
		}
	}
	
}
