package DövizVeKriptoTakipUygulamasi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class CoinGeckoAPITest {
	
	 public static void main(String[] args) {
	        try {
	            // API URL
	            String apiUrl = "https://api.coingecko.com/api/v3/simple/price?ids=bitcoin,ethereum,ripple,litecoin,cardano&vs_currencies=usd";

	            
	            // URL nesnesi oluştur
	            URL url = new URL(apiUrl);
	            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	            connection.setRequestMethod("GET");

	            // API yanıtını oku
	            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	            String inputLine;
	            StringBuilder response = new StringBuilder();
	            while ((inputLine = in.readLine()) != null) {
	                response.append(inputLine);
	            }
	            in.close();

	            // JSON parse
	            JSONObject jsonResponse = new JSONObject(response.toString());
	            double bitcoinPrice = jsonResponse.getJSONObject("bitcoin").getDouble("usd");
	            double ethereumPrice = jsonResponse.getJSONObject("ethereum").getDouble("usd");

	            // Sonuçları yazdır
	            System.out.println("Bitcoin Fiyatı: $" + bitcoinPrice);
	            System.out.println("Ethereum Fiyatı: $" + ethereumPrice);

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

}
