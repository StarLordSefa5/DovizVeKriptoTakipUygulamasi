package DövizVeKriptoTakipUygulamasi;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class CoinGeckoPriceHistory {

	public static List<double[]> getPriceHistory(String coinId, int days) {
        List<double[]> priceData = new ArrayList<>();
        try {
            String urlStr = "https://api.coingecko.com/api/v3/coins/" + coinId + "/market_chart?vs_currency=usd&days=" + days;
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder jsonText = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                jsonText.append(line);
            }
            in.close();

            JSONObject json = new JSONObject(jsonText.toString());
            JSONArray prices = json.getJSONArray("prices");

            // prices dizisi içinde her eleman [timestamp, price]
            for (int i = 0; i < prices.length(); i++) {
                JSONArray pricePoint = prices.getJSONArray(i);
                double timestamp = pricePoint.getDouble(0);
                double price = pricePoint.getDouble(1);
                priceData.add(new double[] { timestamp, price });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return priceData;
    }

    public static void main(String[] args) {
        List<double[]> prices = getPriceHistory("bitcoin", 1);
        for (double[] point : prices) {
            System.out.println("Timestamp: " + point[0] + ", Price: " + point[1]);
        }
    }
}
