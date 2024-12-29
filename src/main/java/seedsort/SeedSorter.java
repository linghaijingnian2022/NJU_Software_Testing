package seedsort;

import java.util.*;

// 定义一个Seed类来表示种子
class Seed {
    byte[] data;      // 种子的字节数据
    int coverage;     // 覆盖率
    long executionTime; // 执行时间（毫秒）
    int enqueueOrder;  // 入队顺序（用于按入队顺序排序）

    // 构造方法
    Seed(byte[] data, int coverage, long executionTime, int enqueueOrder) {
        this.data = data;
        this.coverage = coverage;
        this.executionTime = executionTime;
        this.enqueueOrder = enqueueOrder;
    }

    @Override
    public String toString() {
        return "Seed{data=" + Arrays.toString(data) + ", coverage=" + coverage + ", executionTime=" + executionTime + ", enqueueOrder=" + enqueueOrder + "}";
    }
}

// 种子排序组件
public class SeedSorter {

    // 基于覆盖率排序
    public List<Seed> sortByCoverage(List<Seed> seeds) {
        seeds.sort((s1, s2) -> Integer.compare(s2.coverage, s1.coverage)); // 按覆盖率降序排列
        return seeds;
    }

    // 基于执行时间排序
    public List<Seed> sortByExecutionTime(List<Seed> seeds) {
        seeds.sort(Comparator.comparingLong(s -> s.executionTime)); // 按执行时间升序排列
        return seeds;
    }

    // 按入队顺序选择（不进行排序，保持原顺序）
    public List<Seed> sortByEnqueueOrder(List<Seed> seeds) {
        return seeds; // 不做任何排序，直接返回
    }

    // 根据需要选择排序方式
    public List<Seed> sortSeeds(List<Seed> seeds, String sortMethod) {
        switch (sortMethod.toLowerCase()) {
            case "coverage":
                return sortByCoverage(seeds);
            case "execution_time":
                return sortByExecutionTime(seeds);
            case "enqueue_order":
                return sortByEnqueueOrder(seeds);
            default:
                throw new IllegalArgumentException("Invalid sort method: " + sortMethod);
        }
    }

    public static void main(String[] args) {
        SeedSorter sorter = new SeedSorter();

        // 模拟一些种子（byte[]），每个种子都有不同的覆盖率、执行时间和入队顺序
        List<Seed> seeds = Arrays.asList(
                new Seed(new byte[]{1, 2, 3, 4}, 10, 150, 1), // 入队顺序 1
                new Seed(new byte[]{5, 6, 7, 8}, 30, 100, 2), // 入队顺序 2
                new Seed(new byte[]{9, 10, 11, 12}, 20, 200, 3), // 入队顺序 3
                new Seed(new byte[]{13, 14, 15, 16}, 15, 50, 4)  // 入队顺序 4
        );

        // 输出原始种子
        System.out.println("Original seeds:");
        seeds.forEach(System.out::println);

        // 排序方式可以选择 "coverage", "execution_time", "enqueue_order"
        String sortMethod = "coverage"; // 改为需要的排序方式

        // 排序
        List<Seed> sortedSeeds = sorter.sortSeeds(seeds, sortMethod);

        // 输出排序后的种子
        System.out.println("\nSorted seeds by " + sortMethod + ":");
        sortedSeeds.forEach(System.out::println);
    }
}
