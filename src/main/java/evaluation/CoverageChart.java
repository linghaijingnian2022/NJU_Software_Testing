package evaluation;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CoverageChart extends JFrame {
    public CoverageChart(List<CoverageData> coverageDataList) {
        setTitle("Code Coverage Over Time");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        XYSeries series = new XYSeries("Coverage");
        // 添加覆盖率数据并进行线性插值
        for (int i = 0; i < coverageDataList.size() - 1; i++) {
            CoverageData start = coverageDataList.get(i);
            CoverageData end = coverageDataList.get(i + 1);

            // 添加起始点
            series.add(Double.parseDouble(start.getVersion().substring(1)), start.getCoverage());

            // 线性插值，添加中间点
            double midVersion = (Double.parseDouble(start.getVersion().substring(1)) +
                    Double.parseDouble(end.getVersion().substring(1))) / 2;
            double midCoverage = (start.getCoverage() + end.getCoverage()) / 2;
            series.add(midVersion, midCoverage);
        }

        // 添加最后一个点
        series.add(Double.parseDouble(coverageDataList.get(coverageDataList.size() - 1).getVersion().substring(1)),
                coverageDataList.get(coverageDataList.size() - 1).getCoverage());

        XYSeriesCollection dataset = new XYSeriesCollection(series);
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Code Coverage Over Time",
                "Version",
                "Coverage (%)",
                dataset
        );

        // 获取图表的绘图区域
        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(true, false);

        // 设置线条的宽度和颜色
        renderer.setSeriesStroke(0, new BasicStroke(2.0f)); // 设置线条宽度
        renderer.setSeriesPaint(0, Color.BLUE); // 设置线条颜色

        // 应用渲染器
        plot.setRenderer(renderer);


        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        setContentPane(chartPanel);
    }

    public static void main(String[] args) {
        CoverageEvaluator.CoverageDataCollector collector = new CoverageEvaluator.CoverageDataCollector();
        List<CoverageData> coverageData = collector.collectCoverageData();
        SwingUtilities.invokeLater(() -> {
            CoverageChart chart = new CoverageChart(coverageData);
            chart.setVisible(true);
        });
    }
}
