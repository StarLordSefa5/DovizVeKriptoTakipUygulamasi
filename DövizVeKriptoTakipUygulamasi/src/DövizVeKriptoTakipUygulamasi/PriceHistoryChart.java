package DövizVeKriptoTakipUygulamasi;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PriceHistoryChart extends JFrame{
	public PriceHistoryChart(String coinId, List<double[]> priceData) {
        setTitle(coinId.toUpperCase() + " - Son 24 Saatlik Fiyat Grafiği");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (double[] point : priceData) {
            String time = Utils.formatTimestamp(point[0]);
            double price = point[1];
            dataset.addValue(price, "Fiyat", time);
        }

        JFreeChart lineChart = ChartFactory.createLineChart(
                coinId.toUpperCase() + " Fiyat Grafiği",
                "Zaman",
                "Fiyat (USD)",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false);

        ChartPanel chartPanel = new ChartPanel(lineChart);
        setContentPane(chartPanel);
    }
}


