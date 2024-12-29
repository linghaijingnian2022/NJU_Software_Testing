package evaluation;
import org.jacoco.core.analysis.Analyzer;
import org.jacoco.core.analysis.ICoverageNode;
import org.jacoco.core.analysis.ICounter;
import org.jacoco.core.data.ExecutionData;
import org.jacoco.core.data.SessionInfo;
import org.jacoco.core.data.ExecutionDataStore;
import org.jacoco.core.analysis.CoverageBuilder;
import org.jacoco.core.tools.ExecFileLoader;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class CoverageEvaluator {

    private final String execFilePath; // JaCoCo exec 文件路径
    private final String classFilePath; // 被测试的类文件路径

    public CoverageEvaluator(String execFilePath, String classFilePath) {
        this.execFilePath = execFilePath;
        this.classFilePath = classFilePath;
    }

    // 读取执行数据
    public void analyzeCoverage() throws IOException {
        ExecFileLoader execFileLoader = new ExecFileLoader();
        execFileLoader.load(new File(execFilePath));

        // 获取会话信息和执行数据
        List<SessionInfo> sessionInfos = execFileLoader.getSessionInfoStore().getInfos();
        ExecutionDataStore executionDataStore = execFileLoader.getExecutionDataStore();

        // 创建覆盖率分析器
        CoverageBuilder coverageBuilder = new CoverageBuilder();
        Analyzer analyzer = new Analyzer(executionDataStore, coverageBuilder);

        // 分析指定的类文件
        File classFile = new File(classFilePath);
        analyzer.analyzeAll(classFile);

        // 准备数据
        XYSeries coverageSeries = new XYSeries("Coverage");
        int step = 0; // 步长，模拟不同时间点的覆盖率
        for (ICoverageNode node : coverageBuilder.getClasses()) {
            ICounter instructionCounter = node.getInstructionCounter();
            int coveredInstructions = instructionCounter.getCoveredCount();
            int totalInstructions = instructionCounter.getTotalCount();
            double coverageRatio = (totalInstructions > 0) ? ((double) coveredInstructions / totalInstructions) * 100 : 0;

            coverageSeries.add(step++, coverageRatio);
        }

        // 创建图表
        XYSeriesCollection dataset = new XYSeriesCollection(coverageSeries);
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Coverage Curve",
                "Step",
                "Coverage (%)",
                dataset
        );

        // 显示图表
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Coverage Curve");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new ChartPanel(chart));
            frame.pack();
            frame.setVisible(true);
        });

        // 输出覆盖率结果
        printCoverageResults(coverageBuilder);
    }

    // 打印覆盖率结果
    private void printCoverageResults(CoverageBuilder coverageBuilder) {
        for (ICoverageNode node : coverageBuilder.getClasses()) {
            System.out.println("Class: " + node.getName());

            // 获取指令计数器
            ICounter instructionCounter = node.getInstructionCounter();
            int coveredInstructions = instructionCounter.getCoveredCount();
            int totalInstructions = instructionCounter.getTotalCount();

            // 计算覆盖率
            double coverageRatio = (totalInstructions > 0) ? ((double) coveredInstructions / totalInstructions) * 100 : 0;

            System.out.println("Covered Instructions: " + coveredInstructions);
            System.out.println("Total Instructions: " + totalInstructions);
            System.out.printf("Coverage: %.2f%%\n", coverageRatio);
            System.out.println();
        }
    }

    public static void main(String[] args) {
        try {
            String execFilePath = "D:/rjcs/NJU_Software_Testing/target/jacoco.exec"; // 替换为实际路径
            String classFilePath = "D:/rjcs/NJU_Software_Testing/target/classes/monitoring/FuzzingMonitor.class."; // 替换为实际路径
            CoverageEvaluator evaluator = new CoverageEvaluator(execFilePath, classFilePath);
            evaluator.analyzeCoverage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
