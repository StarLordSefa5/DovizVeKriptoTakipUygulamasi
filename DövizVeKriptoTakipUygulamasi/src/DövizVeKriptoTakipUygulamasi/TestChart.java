package DövizVeKriptoTakipUygulamasi;

import java.util.List;

import javax.swing.SwingUtilities;

public class TestChart {
	public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            List<double[]> priceHistory = CoinGeckoPriceHistory.getPriceHistory("bitcoin", 1);
            PriceHistoryChart chart = new PriceHistoryChart("bitcoin", priceHistory);
            chart.setVisible(true);
        });
    }
}
