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
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        public List<List<CoverageData>> collectCoverageData() {
            List<List<CoverageData>> coverageDataList = new ArrayList<>();
            // 指定文件夹路径
            String folderPath = "\\src\\main\\csvs"; // 替换为您的文件夹路径
            Path path = Paths.get(Paths.get("").toAbsolutePath() + folderPath);
            // 检查文件夹是否存在
            if (Files.exists(path) && Files.isDirectory(path)) {
                try (DirectoryStream<Path> stream = Files.newDirectoryStream(path, "*.csv")) {
                    for (Path entry : stream) {
                        List<CoverageData> coverageDataList2 = new ArrayList<>();
                        // 逐行读取文件内容
                        Files.lines(entry).forEach(line -> {
                            // 将每一行按逗号分隔
                            String[] fields = line.split(",");
                            if (fields[0].matches("^-?\\d+(\\.\\d+)?$")) {
                                coverageDataList2.add(new CoverageData(fields[0], Double.parseDouble(fields[1])));
                            }

                        });
                        coverageDataList.add(coverageDataList2);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("指定的路径不存在或不是一个文件夹。");
            }
            return coverageDataList;
        }

        public List<String> collectName() {
            List<String> nameList = new ArrayList<>();
            // 指定文件夹路径
            String folderPath = "\\src\\main\\csvs"; // 替换为您的文件夹路径
            Path path = Paths.get(Paths.get("").toAbsolutePath() + folderPath);
            // 检查文件夹是否存在
            if (Files.exists(path) && Files.isDirectory(path)) {
                try (DirectoryStream<Path> stream = Files.newDirectoryStream(path, "*.csv")) {
                    for (Path entry : stream) {
                        nameList.add(entry.getFileName().toString().substring(0, entry.getFileName().toString().lastIndexOf(".")));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("指定的路径不存在或不是一个文件夹。");
            }
            return nameList;
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
