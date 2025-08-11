package DövizVeKriptoTakipUygulamasi;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
	
	  public static String formatTimestamp(double timestampMillis) {
	        Date date = new Date((long) timestampMillis);
	        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
	        return sdf.format(date);
	    }

}
