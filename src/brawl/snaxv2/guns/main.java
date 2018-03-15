package brawl.snaxv2.guns;

import java.lang.reflect.Field;
import java.util.Properties;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import net.minecraft.server.v1_8_R3.PacketPlayOutAnimation;
public class main extends JavaPlugin implements Listener {
	
	public static gun[] guns = new gun[512];
	
	public class gun {
		public boolean is = false;
		public int itemType = 600;
		public double damage;
		public String bulletType = "default";
		public String mode = "default";
		public int bullets = 1;
		public int tickModif1 = 1;
		public int tickModif2 = 1;
		public int tickModif3 = 1;
		public int reload = 80;
		public int ammoType = 1;
		public int scopeLevel = 0;
		public double accuracy = 0;
		public double accuracyScoped = 0;
		public int clipSize = 4;
		public double speed= 4;
		public String name = "idk";
		public double kb = 0;
		public double recoil = 0;
		public String sound = "auto";
		
	}
	public static String directory = "";
	
    @Override
    public void onEnable() {
    	directory = getDataFolder().getAbsolutePath(); 
    	Bukkit.broadcastMessage(directory);
    	Bukkit.getServer().getPluginManager().registerEvents(this, this);
    	propertiesHelper prop = new propertiesHelper();
    	for (int i = 0; i<512; i++) {
    		Properties properties = prop.loadParams("/" + String.valueOf(i) + ".properties");
    		if (!properties.isEmpty()) {
    			gun newgun = new gun();
    			newgun.is = true;
    			newgun.itemType = i;
    			newgun.damage = Double.valueOf(properties.getProperty("damage", "1"));	
    			newgun.bulletType = properties.getProperty("bulletType", "default");
    			newgun.mode = properties.getProperty("mode", "defualt");
    			newgun.bullets = Integer.valueOf(properties.getProperty("bullets", "1"));
    			newgun.tickModif1 = Integer.valueOf(properties.getProperty("tickModif1", "1"));
    			newgun.tickModif2 = Integer.valueOf(properties.getProperty("tickModif2", "1"));
    			newgun.tickModif3 = Integer.valueOf(properties.getProperty("tickModif3", "1"));		
    			newgun.reload = Integer.valueOf(properties.getProperty("reload", "80"));
    			newgun.ammoType = Integer.valueOf(properties.getProperty("ammoType", "1"));	
    			newgun.scopeLevel = Integer.valueOf(properties.getProperty("scopeLevel", "1"));
    			newgun.clipSize = Integer.valueOf(properties.getProperty("clipSize", "1"));
    			newgun.accuracy = Double.valueOf(properties.getProperty("accuracy", "1"));
    			newgun.accuracyScoped = Double.valueOf(properties.getProperty("accuracyScoped", "1"));
    			newgun.speed = Double.valueOf(properties.getProperty("speed", "4"));
    			newgun.kb = Double.valueOf(properties.getProperty("kb", "0"));
    			newgun.recoil = Double.valueOf(properties.getProperty("recoil", "0"));
    			newgun.name = properties.getProperty("name", "4");
    			newgun.sound = properties.getProperty("sound", "auto");
    			
    			guns[i] = newgun;
    		}else {
    			gun newgun = new gun();
    			newgun.is = false;
    			guns[i] = newgun;
    		}
    	}
    	
        new BukkitRunnable() {
            
            @Override
            public void run() {
            	
            	//update scope
            	for (Player player : Bukkit.getOnlinePlayers()) {
            		scopeStuff scope = new scopeStuff();
            		scope.updateScope(player);
            	}
            	
            	//update gun timers
            	for (int i = 0; i<512; i++) {
            		if (guns[i].is) {
            			for (Player player : Bukkit.getOnlinePlayers()) {
            				timerStuff timer = new timerStuff();
            				timer.updateTimer(player, i);
            			}
            		}
            	}
            	
            	//damage entities accordingly and neglect snowball gravity
            	for (World world : Bukkit.getWorlds()) {
            		for (Entity ent : world.getEntities()) {
            			if (ent instanceof Item) {
            				if (ent.getTicksLived()>400) {
            					ent.remove();
            				}
            			}
            			if (ent instanceof Snowball) {
            				if (ent.getTicksLived()<10) {
            					ent.setVelocity(ent.getVelocity().add(new Vector(0,0.027,0)));
            				}
            			}
            			
            			
            			//the delay stuff
		            	for (Player p : Bukkit.getOnlinePlayers()) {
			           		metaData met = new metaData();
				           	met.setMetadata(p, "delay", (int)met.getMetadata(p,"delay") -1);
				        }
		            	
            			//deal damage from guns as it is not done in the entitydamageevents as it would  cause problems xD
            			metaData met = new metaData();
            			if (ent.hasMetadata("damagedue") && (double)met.getMetadata(ent, "damagedue") > 0) {
            				if (ent instanceof Damageable) {
            					double damage = (double) met.getMetadata(ent, "damagedue");
            					if (ent instanceof Player) {
            						Player p = (Player) ent;
	            					damageReduction red = new damageReduction();
	            					damage = damage-damage*red.getDamageReduced(p);
            					}
            					if (damage>=((Damageable) ent).getHealth()) {
            						((Damageable) ent).damage(1999);
                					met.setMetadata(ent, "damagedue", 0D);
            					}else {
            						((Damageable) ent).setHealth(((Damageable) ent).getHealth()-damage);
            						met.setMetadata(ent, "damagedue", 0D);
            						//do the damage animations cuz they are cancelled when the entitydamageevent is cancelled
            					    for (Player player : Bukkit.getOnlinePlayers()) {        					    	
            					        PacketPlayOutAnimation packet = new PacketPlayOutAnimation();

            					        try {
            					            Field idField = packet.getClass().getDeclaredField("a");
            					            Field animationField = packet.getClass().getDeclaredField("b");

            					            idField.setAccessible(true);
            					            animationField.setAccessible(true);

            					            idField.set(packet, ent.getEntityId());
            					            animationField.set(packet, 1);
            					        } catch (Exception e) {
            					        }
            					        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
            					        
            					    }
            					}
            				}
            			}
            		}
            	}
            	//update gun displaynames
            	for (Player player : Bukkit.getOnlinePlayers()) {
            		nameStuff name = new nameStuff();
            		name.updateAllGunNames(player);
            	}
            	
            	//handle the right click holding thing shit
            	for (Player player : Bukkit.getOnlinePlayers()) {
            		metaData meta = new metaData();
            		if ((int)meta.getMetadata(player, "lastclick") > 4) {
            			meta.setMetadata(player, "holding", 0);
            		}
            		meta.setMetadata(player, "lastclick", (int)meta.getMetadata(player, "lastclick")+1);
            	}
            	
            	//check if holding rightclick with a gun, and if is, then attempt to shoot
            	for (Player player : Bukkit.getOnlinePlayers()) {
            		metaData meta = new metaData();
            		if ((int)meta.getMetadata(player, "holding") == 1) {
            			shootingStuff fire = new shootingStuff ();
            			fire.shoot(player);
            		}
            	}
            }
            
        }.runTaskTimer(this, 1, 1);
    }
    
    @Override
    public void onDisable() {
    	
    }
	@EventHandler
	public void playerJoinEvent (PlayerJoinEvent e){
		Player player = e.getPlayer();
		metaData meta = new metaData();
		//set the necessary metadatas for scoping, damage tracking, equipping delays etc.
		meta.setMetadata(player, "scoped", 0);
		meta.setMetadata(player, "lastclick", 0);
		meta.setMetadata(player, "holding", 0);
		meta.setMetadata(player, "lastDamager", player.getName());
		meta.setMetadata(player, "delay", 0);
		meta.setMetadata(player, "handItemId", player.getItemInHand().getTypeId());
		//add the metadatas for each gun to the player
		for (int i = 0; i<512; i++) {
			meta.setMetadata(player, String.valueOf(i)+"timer", 0);
			meta.setMetadata(player, String.valueOf(i)+"inclip", guns[i].clipSize);
		}
		
	}
	
	@EventHandler
	public void bullethitssomeguy(EntityDamageByEntityEvent event){
		metaData meta = new metaData();
		canbeDamaged can = new canbeDamaged();
		if (!(event.getEntity() instanceof Player) || (event.getEntity() instanceof Player && can.getState(event.getEntity()) == true)) {
			if (event.getDamager() instanceof Snowball) {
				//cancel so there is no damage timer shit etc and do the damage animations etc in the bukkitrunnable @onenable
				event.setCancelled(true);
				meta.setMetadata(event.getEntity(), "lastDamager", ((Player)((Snowball)event.getDamager()).getShooter()).getName());
				double damage = (double) meta.getMetadata(event.getDamager(), "damage");
				if (event.getDamager().getLocation().getY()-1.45>event.getEntity().getLocation().getY()) {
					damage = damage*2;
				}
				//calculate knockback cuz its a complex artform jk jk
				double knockBackAmplifier = (double) meta.getMetadata(event.getDamager(), "kb");
				Vector kb = new Vector(event.getDamager().getVelocity().getX(), 0, event.getDamager().getVelocity().getZ()).normalize();
				kb = new Vector (kb.getX(), 1.2, kb.getZ());
				event.getEntity().setVelocity(kb.multiply(knockBackAmplifier));
				if (event.getEntity().hasMetadata("damagedue")) {
					meta.setMetadata(event.getEntity(), "damagedue", damage+(double)meta.getMetadata(event.getEntity(), "damagedue"));
				}else {
					meta.setMetadata(event.getEntity(), "damagedue", damage);
				}
			}
		}
	}
	 
    @EventHandler
    public void onItemDrop (PlayerDropItemEvent e) {
    	//make sure players dont drop guns that arent reloaded and reload if they arent :P
        Player p = e.getPlayer();
        Item drop = e.getItemDrop();
        metaData meta = new metaData();
        if (guns[drop.getItemStack().getTypeId()].is) {
        	if (!((int)meta.getMetadata(p, String.valueOf(drop.getItemStack().getTypeId())+"inclip")==guns[drop.getItemStack().getTypeId()].clipSize)) {
        		e.setCancelled(true);
				meta.setMetadata(p, String.valueOf(drop.getItemStack().getTypeId())+"timer", guns[drop.getItemStack().getTypeId()].reload);
				meta.setMetadata(p, String.valueOf(drop.getItemStack().getTypeId())+"inclip", 0);
        	}
        }
       
    }
    
	@EventHandler
	public void playerInteractEvent (PlayerInteractEvent e){
		Player player = e.getPlayer();
		
		//scoping stuff
		if (e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
			int id = e.getPlayer().getItemInHand().getTypeId();
			if (guns[id].is) {
				metaData meta = new metaData();
				if ((int)meta.getMetadata(player, "scoped") == 1) {
					meta.setMetadata(player, "scoped", 0);
				}else {
					meta.setMetadata(player, "scoped", 1);
				}
			}
		}
		
		//right click (register the clicks here and handle the shooting in the runnable)
		if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if (e.getPlayer().getItemInHand() != null && guns[e.getPlayer().getItemInHand().getTypeId()].is) {
				metaData meta = new metaData();
				shootingStuff fire = new shootingStuff();
				fire.shoot(e.getPlayer());
				if ((int)meta.getMetadata(e.getPlayer(), "holding")==0) {
					if ((int)meta.getMetadata(e.getPlayer(), "lastclick")>4) {
						meta.setMetadata(e.getPlayer(), "lastclick", 0);
					}
					else {
						meta.setMetadata(e.getPlayer(), "lastclick", 0);
						meta.setMetadata(e.getPlayer(), "holding", 1);
					}
				}else {
					meta.setMetadata(e.getPlayer(), "lastclick", 0);
					meta.setMetadata(e.getPlayer(), "holding", 1);
				}
			}
		}
	}
    
}
