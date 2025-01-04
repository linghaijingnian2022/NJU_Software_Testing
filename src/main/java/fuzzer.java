import executor.ProcessRunner;
import executor.TestExecutor;
import energy.EnergyScheduler;
import evaluation.CoverageEvaluator;
import evaluation.CoverageChart;
import monitoring.FuzzingMonitor;
import mutation.ArithMutator;
import mutation.BitflipMutator;
import mutation.HavocMutator;
import mutation.InterestMutator;
import mutation.Mutator;
import mutation.SpliceMutator;
import seedsort.Seed;
import seedsort.SeedSorter;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class fuzzer {
    public static void main(String[] args) {
        String targetPath = null;
        String initialSeedPath = null;
        String sortStrategy = "enqueueOrder";  // 默认调度策略
        String mutationStrategy = "arith";  // 默认变异策略

        for (int i = 0; i < args.length; i++) {
            if ("-i".equals(args[i])) {
                if (i + 1 < args.length) {
                    targetPath = args[i + 1];
                } else {
                    System.err.println("Missing value for -p option");
                    return;
                }
            } else if ("-o".equals(args[i])) {
                if (i + 1 < args.length) {
                    initialSeedPath = args[i + 1];
                } else {
                    System.err.println("Missing value for -s option");
                    return;
                }
            } else if ("-s".equals(args[i])) {
                if (i + 1 < args.length) {
                    sortStrategy = args[i + 1];
                } else {
                    System.err.println("Missing value for -m option");
                    return;
                }
            } else if ("-m".equals(args[i])) {
                if (i + 1 < args.length) {
                    mutationStrategy = args[i + 1];
                } else {
                    System.err.println("Missing value for -e option");
                    return;
                }
            }
        }

        if (targetPath == null || initialSeedPath == null) {
            System.err.println("Usage: java fuzzer [-i <target_program_path>] [-o <initial_seed_path>]");
            return;
        }

        // 初始化组件
        ProcessRunner processRunner = new ProcessRunner();
        FuzzingMonitor monitor = new FuzzingMonitor();
        List<Seed> seeds = new ArrayList<>();

        // 开始监控
        monitor.startMonitoring();

        // 执行模糊测试循环
        for (int i = 0; i < 100; i++) {
            // 根据选择的调度策略进行种子排序
            if ("energy".equals(sortStrategy)) {
                EnergyScheduler.sortSeedsByEnergy(seeds);
            } else if ("enqueueOrder".equals(sortStrategy)) {
                SeedSorter.sortByEnqueueOrder(seeds);
            } else if ("coverage".equals(sortStrategy)) {
                SeedSorter.sortByCoverage(seeds);
            } else if ("executionTime".equals(sortStrategy)) {
                SeedSorter.sortByExecutionTime(seeds);
            } else {
                System.err.println("Invalid seed-sort strategy");
                return;
            }

            // 选择种子
            Seed selectedSeed = seeds.get(0);

            // 执行测试目标
            TestExecutor testExecutor = new TestExecutor(selectedSeed, processRunner);
            try {
                testExecutor.executeFuzzTarget(targetPath, initialSeedPath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 记录执行结果
            // 假设这里可以获取到覆盖率和执行时间等信息，这里只是示例值
            int coverage = 50;
            long executionTime = 1000;
            boolean isSpecial = false;
            monitor.recordExecution(selectedSeed.data, coverage, executionTime, i, isSpecial);

            // 根据选择的变异策略进行种子变异
            Mutator mutator;
            if ("arith".equals(mutationStrategy)) {
                mutator = new ArithMutator();
            } else if ("bitflip".equals(mutationStrategy)) {
                mutator = new BitflipMutator();
            } else if ("havoc".equals(mutationStrategy)) {
                mutator = new HavocMutator();
            } else if ("interest".equals(mutationStrategy)) {
                mutator = new InterestMutator();
            } else if ("splice".equals(mutationStrategy)) {
                mutator = new SpliceMutator(new ArrayList<byte[]>());
            } else {
                System.err.println("Invalid mutation strategy");
                return;
            }
            Seed mutatedSeed = mutator.mutate(selectedSeed);
            seeds.add(mutatedSeed);
        }

        // 打印监控结果
        monitor.printResults();

        // 评估覆盖率并生成图表
        CoverageEvaluator evaluator = new CoverageEvaluator("your_exec_file_path", "your_class_file_path");
        try {
            evaluator.analyzeCoverage();
        } catch (IOException e) {
            e.printStackTrace();
        }
        CoverageEvaluator.CoverageDataCollector collector = new CoverageEvaluator.CoverageDataCollector();
        List<List<evaluation.CoverageData>> coverageDataList = collector.collectCoverageData();
        List<String> chartNames = collector.collectName();
        SwingUtilities.invokeLater(() -> {
            CoverageChart chart = new CoverageChart(coverageDataList, chartNames);
            chart.setVisible(true);
        });
    }
}