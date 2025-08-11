package DövizVeKriptoTakipUygulamasi;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class SimpleChartExample extends JFrame {
	
	public SimpleChartExample() {
        setTitle("JFreeChart Örnek Grafik");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Örnek veri: Saat bazında fiyatlar
        dataset.addValue(100, "Fiyat", "10:00");
        dataset.addValue(105, "Fiyat", "11:00");
        dataset.addValue(102, "Fiyat", "12:00");
        dataset.addValue(110, "Fiyat", "13:00");
        dataset.addValue(115, "Fiyat", "14:00");

        // Çizgi grafik oluştur
        JFreeChart lineChart = ChartFactory.createLineChart(
                "Kripto Fiyat Grafiği",
                "Zaman",
                "Fiyat (USD)",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        ChartPanel chartPanel = new ChartPanel(lineChart);
        setContentPane(chartPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SimpleChartExample example = new SimpleChartExample();
            example.setVisible(true);
        });
    }

}
