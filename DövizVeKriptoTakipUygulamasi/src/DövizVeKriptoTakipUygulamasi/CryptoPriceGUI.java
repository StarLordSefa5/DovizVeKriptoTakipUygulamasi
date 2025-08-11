package DövizVeKriptoTakipUygulamasi;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;

import org.json.JSONObject;

public class CryptoPriceGUI extends JFrame {
	
	 private JTable table;
	    private String[][] data;
	    private String[] columnNames = {"Para Birimi", "Fiyat (USD)"};

	    public CryptoPriceGUI() {
	    	
	    	 try {
	    	        DatabaseManager.createTables();  // Tabloları burada oluştur
	    	    } catch (Exception e) {
	    	        e.printStackTrace();
	    	    }
	        setTitle("Kripto Fiyat Takip Paneli");
	        setSize(600, 400);
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        setLocationRelativeTo(null);

	        data = new String[][] {
	        	 {"Bitcoin", "Yükleniyor..."},
	        	    {"Ethereum", "Yükleniyor..."},
	        	    {"Ripple", "Yükleniyor..."},
	        	    {"Litecoin", "Yükleniyor..."},
	        	    {"Cardano", "Yükleniyor..."}
	        };

	        table = new JTable(data, columnNames);
	        add(new JScrollPane(table), BorderLayout.CENTER);

	        JPanel buttonPanel = new JPanel(new FlowLayout());

	        JButton favButton = new JButton("Favorilere Ekle");
	        JButton alarmButton = new JButton("Alarm Ekle");
	        JButton chartButton = new JButton("Grafiği Göster");

	        buttonPanel.add(favButton);
	        buttonPanel.add(alarmButton);
	        buttonPanel.add(chartButton);

	        add(buttonPanel, BorderLayout.SOUTH);

	        favButton.addActionListener(e -> addFavorite());
	        alarmButton.addActionListener(e -> addAlarm());
	        chartButton.addActionListener(e -> showChart());

	        updatePrices();

	        Timer timer = new Timer(30000, e -> updatePrices());
	        timer.start();
	    

	    
	  
	    }
	    private void showChart() {
	        int selectedRow = table.getSelectedRow();
	        if (selectedRow == -1) {
	            JOptionPane.showMessageDialog(this, "Lütfen bir para birimi seçin.");
	            return;
	        }

	        String name = (String) table.getValueAt(selectedRow, 0);

	        final String coinId;
	        switch(name.toLowerCase()) {
	            case "bitcoin": coinId = "bitcoin"; break;
	            case "ethereum": coinId = "ethereum"; break;
	            case "ripple": coinId = "ripple"; break;
	            case "litecoin": coinId = "litecoin"; break;
	            case "cardano": coinId = "cardano"; break;
	            default:
	                JOptionPane.showMessageDialog(this, "Grafik gösterilemiyor: bilinmeyen para birimi.");
	                return;
	        }

	       
	        // API çağrısını arka planda yapalım ki UI donmasın
	        SwingWorker<Void, Void> worker = new SwingWorker<>() {
	            List<double[]> priceHistory;

	            @Override
	            protected Void doInBackground() throws Exception {
	                priceHistory = CoinGeckoPriceHistory.getPriceHistory(coinId, 1);
	                return null;
	            }

	            @Override
	            protected void done() {
	                if (priceHistory != null && !priceHistory.isEmpty()) {
	                    PriceHistoryChart chart = new PriceHistoryChart(coinId, priceHistory);
	                    chart.setVisible(true);
	                } else {
	                    JOptionPane.showMessageDialog(null, "Fiyat verisi alınamadı.");
	                }
	            }
	        };
	        worker.execute();
	    }

	    

	    // Favori ekleme
	    private void addFavorite() {
	        int selectedRow = table.getSelectedRow();
	        if (selectedRow == -1) {
	            JOptionPane.showMessageDialog(this, "Lütfen bir para birimi seçin.");
	            return;
	        }
	        String name = (String) table.getValueAt(selectedRow, 0);
	        String symbol = name.equalsIgnoreCase("Bitcoin") ? "BTC" : "ETH";

	        try (Connection conn = DatabaseManager.connect()) {
	            String sql = "INSERT INTO favorites (currency_name, currency_symbol, type) VALUES (?, ?, ?)";
	            PreparedStatement pstmt = conn.prepareStatement(sql);
	            pstmt.setString(1, name);
	            pstmt.setString(2, symbol);
	            pstmt.setString(3, "crypto");
	            pstmt.executeUpdate();
	            JOptionPane.showMessageDialog(this, name + " favorilere eklendi.");
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	        
	        
	    }
	    

	    // Alarm ekleme
	    private void addAlarm() {
	        int selectedRow = table.getSelectedRow();
	        if (selectedRow == -1) {
	            JOptionPane.showMessageDialog(this, "Lütfen bir para birimi seçin.");
	            return;
	        }
	        String name = (String) table.getValueAt(selectedRow, 0);
	        String symbol = name.equalsIgnoreCase("Bitcoin") ? "BTC" : "ETH";

	        String priceStr = JOptionPane.showInputDialog(this, "Hedef fiyat girin:");
	        if (priceStr == null || priceStr.isEmpty()) return;

	        String[] options = {"Üstünde", "Altında"};
	        int choice = JOptionPane.showOptionDialog(this, "Fiyat hangi yönde olunca uyarı verilsin?",
	                "Alarm Yönü", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
	                null, options, options[0]);

	        String direction = (choice == 0) ? "up" : "down";

	        try (Connection conn = DatabaseManager.connect()) {
	            String sql = "INSERT INTO alerts (currency_symbol, target_price, direction) VALUES (?, ?, ?)";
	            PreparedStatement pstmt = conn.prepareStatement(sql);
	            pstmt.setString(1, symbol);
	            pstmt.setDouble(2, Double.parseDouble(priceStr));
	            pstmt.setString(3, direction);
	            pstmt.executeUpdate();
	            JOptionPane.showMessageDialog(this, "Alarm eklendi: " + name + " " + direction + " " + priceStr);
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	    }

	    // API'den fiyat çekme
	    private void updatePrices() {
	        try {
	            String apiUrl = "https://api.coingecko.com/api/v3/simple/price?ids=bitcoin,ethereum,ripple,litecoin,cardano&vs_currencies=usd";
	            URL url = new URL(apiUrl);
	            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	            connection.setRequestMethod("GET");

	            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	            StringBuilder response = new StringBuilder();
	            String inputLine;

	            while ((inputLine = in.readLine()) != null) {
	                response.append(inputLine);
	            }
	            in.close();

	            JSONObject jsonResponse = new JSONObject(response.toString());

	            // Fiyatları tabloya yaz
	            table.setValueAt("$" + jsonResponse.getJSONObject("bitcoin").getDouble("usd"), 0, 1);
	            table.setValueAt("$" + jsonResponse.getJSONObject("ethereum").getDouble("usd"), 1, 1);
	            table.setValueAt("$" + jsonResponse.getJSONObject("ripple").getDouble("usd"), 2, 1);
	            table.setValueAt("$" + jsonResponse.getJSONObject("litecoin").getDouble("usd"), 3, 1);
	            table.setValueAt("$" + jsonResponse.getJSONObject("cardano").getDouble("usd"), 4, 1);

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }


	    // Main
	    public static void main(String[] args) {
	        SwingUtilities.invokeLater(() -> {
	            new CryptoPriceGUI().setVisible(true);
	        });
	    }
}