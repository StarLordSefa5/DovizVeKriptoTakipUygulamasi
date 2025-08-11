package DövizVeKriptoTakipUygulamasi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
	
	  private static final String DB_URL = "jdbc:sqlite:crypto_tracker.db";

	    // Veritabanı bağlantısı oluşturma
	    public static Connection connect() {
	        Connection conn = null;
	        try {
	            conn = DriverManager.getConnection(DB_URL);
	            System.out.println("SQLite bağlantısı başarılı.");
	        } catch (SQLException e) {
	            System.out.println("Bağlantı hatası: " + e.getMessage());
	        }
	        
	       
	        return conn;
	    }
           
	    
	    
	    // Tabloları oluşturma
	    public static void createTables() {
	        String createFavorites = "CREATE TABLE IF NOT EXISTS favorites (" +
	                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
	                "currency_name TEXT NOT NULL," +
	                "currency_symbol TEXT NOT NULL," +
	                "type TEXT NOT NULL" +
	                ");";

	        String createAlerts = "CREATE TABLE IF NOT EXISTS alerts (" +
	                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
	                "currency_symbol TEXT NOT NULL," +
	                "target_price REAL NOT NULL," +
	                "direction TEXT NOT NULL" +
	                ");";

	        String createPriceHistory = "CREATE TABLE IF NOT EXISTS price_history (" +
	                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
	                "currency_symbol TEXT NOT NULL," +
	                "price REAL NOT NULL," +
	                "date TEXT NOT NULL" +
	                ");";

	        try (Connection conn = connect();
	             Statement stmt = conn.createStatement()) {
	            stmt.execute(createFavorites);
	            stmt.execute(createAlerts);
	            stmt.execute(createPriceHistory);
	            System.out.println("Tablolar başarıyla oluşturuldu.");
	        } catch (SQLException e) {
	            System.out.println("Tablo oluşturma hatası: " + e.getMessage());
	        }
	    }

	    public static void main(String[] args) {
	        createTables();
	    }

}
