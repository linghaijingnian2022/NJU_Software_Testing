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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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


        // 输出覆盖率结果
        printCoverageResults(coverageBuilder);
    }

    public static class CoverageDataCollector {
        public List<CoverageData> collectCoverageData() {
            List<CoverageData> coverageDataList = new ArrayList<>();
            // 模拟不同版本的覆盖率数据
            String csvFile = "path/to/your/file.csv"; // 替换为你的CSV文件路径
            String line;
            String csvSplitBy = ","; // 根据实际情况修改分隔符

            try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
                while ((line = br.readLine()) != null) {
                    // 使用逗号分隔符分割每一行
                    String[] values = line.split(csvSplitBy);

                    coverageDataList.add(new CoverageData(values[0], Double.parseDouble(values[1])));

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            coverageDataList.add(new CoverageData("1.0", 60.0));
            coverageDataList.add(new CoverageData("1.1", 70.0));
            coverageDataList.add(new CoverageData("1.2", 80.0));
            coverageDataList.add(new CoverageData("1.3", 75.0));
            coverageDataList.add(new CoverageData("1.4", 90.0));
            return coverageDataList;
        }
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
