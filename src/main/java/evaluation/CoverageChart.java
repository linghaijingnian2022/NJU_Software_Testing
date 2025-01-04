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
import java.util.ArrayList;
import java.util.List;

public class CoverageChart extends JFrame {
    public CoverageChart(List<List<CoverageData>> coverageDataLists, List<String> names) {
        setTitle("Code Coverage Over Time");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        XYSeriesCollection dataset = new XYSeriesCollection();

        // 遍历每组覆盖率数据
        for (int j = 0; j < coverageDataLists.size(); j++) {
            List<CoverageData> coverageDataList = coverageDataLists.get(j);
            String name = names.get(j);
            XYSeries series = new XYSeries(name);

            // 添加覆盖率数据并进行线性插值
            for (int i = 0; i < coverageDataList.size() - 1; i++) {
                CoverageData start = coverageDataList.get(i);
                CoverageData end = coverageDataList.get(i + 1);

                // 添加起始点
                series.add(Double.parseDouble(start.getTime()), start.getCoverage());

                // 线性插值，添加中间点
                double midVersion = (Double.parseDouble(start.getTime()) +
                        Double.parseDouble(end.getTime())) / 2;
                double midCoverage = (start.getCoverage() + end.getCoverage()) / 2;
                series.add(midVersion, midCoverage);
            }


            // 将系列添加到数据集中
            dataset.addSeries(series);
        }

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Code Coverage Over Time",
                "time (h)",
                "Coverage",
                dataset
        );

        // 获取图表的绘图区域
        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(true, false);

        // 设置线条的宽度和颜色
        for (int i = 0; i < names.size(); i++) {
            renderer.setSeriesStroke(i, new BasicStroke(2.0f)); // 设置线条宽度
            renderer.setSeriesPaint(i, getColor(i)); // 设置线条颜色
        }

        // 应用渲染器
        plot.setRenderer(renderer);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        setContentPane(chartPanel);
    }




    // 根据索引返回不同的颜色
    private Color getColor(int index) {
        Color[] colors = {Color.BLUE, Color.MAGENTA, Color.GREEN, Color.RED, Color.ORANGE, Color.BLACK, Color.CYAN, Color.GRAY, Color.PINK, Color.YELLOW, Color.white};
        return colors[index % colors.length];
    }

    public static void main(String[] args) {
        CoverageEvaluator.CoverageDataCollector collector = new CoverageEvaluator.CoverageDataCollector();
        List<List<CoverageData>> coverageData = collector.collectCoverageData();
        List<String> names = collector.collectName();
        for (int i = 0; i < coverageData.size(); i++) {
            List<List<CoverageData>> CD= new ArrayList<>();
            CD.add(coverageData.get(i));
            List<String> N = new ArrayList<>();
            N.add(names.get(i));
            SwingUtilities.invokeLater(() -> {
                CoverageChart chart = new CoverageChart(CD, N);
                chart.setVisible(true);
            });
        }
        SwingUtilities.invokeLater(() -> {
            CoverageChart chart = new CoverageChart(coverageData, names);
            chart.setVisible(true);
        });
    }
}