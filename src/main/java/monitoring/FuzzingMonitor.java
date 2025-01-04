package monitoring;

import seedsort.Seed;

import java.util.ArrayList;
import java.util.List;

public class FuzzingMonitor {

    private List<Seed> specialSeeds; // 保存特殊种子
    private long startTime; // 测试开始时间
    private long executionCount; // 执行次数
    private long totalExecutionTime; // 总执行时间

    public FuzzingMonitor() {
        this.specialSeeds = new ArrayList<>();
        this.executionCount = 0;
        this.totalExecutionTime = 0;
    }

    // 开始监控
    public void startMonitoring() {
        this.startTime = System.currentTimeMillis();
    }

    // 记录一次执行结果
    public void recordExecution(byte[] seedData, int coverage, long executionTime, int enqueueOrder, boolean isSpecial) {
        this.executionCount++;
        this.totalExecutionTime += executionTime;

        // 创建 Seed 对象
        Seed seed = new Seed(seedData, coverage, executionTime, enqueueOrder);

        if (isSpecial) {
            // 保存特殊种子
            specialSeeds.add(seed);
        }
    }

    public double getCoverage() {
        // 实际覆盖率计算逻辑应根据具体情况实现
        return (double) executionCount/100;
    }

    // 获取执行速度
    public double getExecutionSpeed() {
        return (double) executionCount / (System.currentTimeMillis() - startTime) * 1000; // 每秒执行次数
    }

    // 打印监控结果
    public void printResults() {
        System.out.println("Execution Count: " + executionCount);
        System.out.println("Total Execution Time: " + totalExecutionTime + " ms");
        System.out.println("Coverage: " + getCoverage());
        System.out.println("Execution Speed: " + getExecutionSpeed() + " executions/second");

        // 打印特殊种子
        System.out.println("Special Seeds:");
        for (Seed seed : specialSeeds) {
            System.out.println(seed);
        }
    }
}
