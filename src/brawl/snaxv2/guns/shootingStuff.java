package brawl.snaxv2.guns;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.util.Vector;

public class shootingStuff {
	public void shoot(Player player) {
		metaData meta = new metaData();
		ammoStuff ammo = new ammoStuff();
		//check if can shoot: holding a gun, the gun has reloaded, it can shoot, it has bullets, the equipment delay isnt active etc.
		if ((int)meta.getMetadata(player, "delay") < 0 && player.getItemInHand() != null && (int)meta.getMetadata(player, player.getItemInHand().getTypeId()+ "inclip")>0 && (int)meta.getMetadata(player, player.getItemInHand().getTypeId() + "timer") <= 0 && ammo.itemAmount(player, brawl.snaxv2.guns.main.guns[player.getItemInHand().getTypeId()].ammoType)>0) {
			//iterate as many times as the gun should shoot, and do the shooting xD
        	
			//check item in hand has changed and apply delay if so
    		if (brawl.snaxv2.guns.main.guns[(int)meta.getMetadata(player, "handItemId")].is && (int)meta.getMetadata(player, "handItemId")!=player.getItemInHand().getTypeId() && brawl.snaxv2.guns.main.guns[(int)meta.getMetadata(player, "handItemId")].tickModif1 > 3) {
    			meta.setMetadata(player, "delay", 6);
    		}
    		meta.setMetadata(player, "handItemId", player.getItemInHand().getTypeId());
    		meta.setMetadata(player, "delay", (int)meta.getMetadata(player,"delay") -1);
    		
			for (int i = 0; i<brawl.snaxv2.guns.main.guns[player.getItemInHand().getTypeId()].bullets; i++) {
				Snowball bullet = player.getWorld().spawn(player.getEyeLocation(), Snowball.class);
				bullet.setShooter(player);
				bullet.setVelocity(player.getLocation().getDirection().multiply(brawl.snaxv2.guns.main.guns[player.getItemInHand().getTypeId()].speed));
				Random rand = new Random();
				Vector accuracy = new Vector(rand.nextFloat()-0.5,rand.nextFloat()-0.5,rand.nextFloat()-0.5);
				if ((int)meta.getMetadata(player, "scoped") == 1) {
					accuracy = accuracy.multiply(brawl.snaxv2.guns.main.guns[player.getItemInHand().getTypeId()].accuracyScoped);
				}else {
					accuracy = accuracy.multiply(brawl.snaxv2.guns.main.guns[player.getItemInHand().getTypeId()].accuracy);
				}
				bullet.setVelocity(bullet.getVelocity().add(accuracy));
				meta.setMetadata(bullet, "kb", brawl.snaxv2.guns.main.guns[player.getItemInHand().getTypeId()].kb);
				meta.setMetadata(bullet, "damage" , brawl.snaxv2.guns.main.guns[player.getItemInHand().getTypeId()].damage);
				meta.setMetadata(bullet, "type" , brawl.snaxv2.guns.main.guns[player.getItemInHand().getTypeId()].bulletType);
				Vector temp = bullet.getVelocity().multiply(-brawl.snaxv2.guns.main.guns[player.getItemInHand().getTypeId()].recoil);
				player.setVelocity(new Vector(temp.getX(), temp.getY()+player.getVelocity().getY(), temp.getZ()));
			}
			//remove ammo from inv, update timers, ammo in the guns clip and play the soudns
			int inclip = (int)meta.getMetadata(player, String.valueOf(player.getItemInHand().getTypeId())+"inclip");
			ammo.consumeItem(player, brawl.snaxv2.guns.main.guns[player.getItemInHand().getTypeId()].ammoType);
			if (inclip == 1) {
				meta.setMetadata(player, String.valueOf(player.getItemInHand().getTypeId())+"timer", brawl.snaxv2.guns.main.guns[player.getItemInHand().getTypeId()].reload);
				meta.setMetadata(player, String.valueOf(player.getItemInHand().getTypeId())+"inclip", (int)meta.getMetadata(player, String.valueOf(player.getItemInHand().getTypeId())+"inclip")-1);
			}else {
				meta.setMetadata(player, String.valueOf(player.getItemInHand().getTypeId())+"timer", brawl.snaxv2.guns.main.guns[player.getItemInHand().getTypeId()].tickModif1);
				meta.setMetadata(player, String.valueOf(player.getItemInHand().getTypeId())+"inclip", (int)meta.getMetadata(player, String.valueOf(player.getItemInHand().getTypeId())+"inclip")-1);
			}
			if (brawl.snaxv2.guns.main.guns[player.getItemInHand().getTypeId()].sound.equals("shotgun")) {
				player.getWorld().playSound(player.getLocation(), Sound.GHAST_FIREBALL, 10, 1);
			}
			if (brawl.snaxv2.guns.main.guns[player.getItemInHand().getTypeId()].sound.equals("sniper")) {
				player.getWorld().playSound(player.getLocation(), Sound.IRONGOLEM_HIT, 10, 1);
				player.getWorld().playSound(player.getLocation(), Sound.WITHER_SHOOT, 10, 1);
			}
			if (brawl.snaxv2.guns.main.guns[player.getItemInHand().getTypeId()].sound.equals("auto")) {
				player.getWorld().playSound(player.getLocation(), Sound.NOTE_SNARE_DRUM, 10, 1);
				player.getWorld().playSound(player.getLocation(), Sound.ITEM_BREAK, 10, 1);
				player.getWorld().playSound(player.getLocation(), Sound.GHAST_FIREBALL, 10, 1);
			}
		}
	}
}
