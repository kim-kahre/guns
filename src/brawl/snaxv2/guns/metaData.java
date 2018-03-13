package brawl.snaxv2.guns;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.metadata.Metadatable;
import org.bukkit.plugin.Plugin;

public class metaData {
	public void setMetadata(Metadatable object, String key, Object value) {
		  object.setMetadata(key, new FixedMetadataValue(Bukkit.getPluginManager().getPlugin("brawlGuns"),value));
		}

		public Object getMetadata(Metadatable object, String key) {
		  List<MetadataValue> values = object.getMetadata(key);  
		  for (MetadataValue value : values) {
		     if (value.getOwningPlugin() == Bukkit.getPluginManager().getPlugin("brawlGuns")) {
		        return value.value();
		     }
		  }
		  return null;
		}
}
