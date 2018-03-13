package brawl.snaxv2.guns;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.bukkit.Bukkit;


public class propertiesHelper {
	public Properties loadParams(String fileName) {
	    Properties props = new Properties();
	    InputStream is = null;
	    try {
	        File f = new File(brawl.snaxv2.guns.main.directory+fileName);
	        is = new FileInputStream( f );
	    }
	    catch ( Exception e ) { is = null; }
	 
	    try {
	        if ( is == null ) {
	            is = getClass().getResourceAsStream("server.properties");
	        }
	        props.load( is );
	    }
	    catch ( Exception e ) { }
		return props;
	 

	}
}