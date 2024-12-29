import seedsort.Seed;
import seedsort.SeedSorter;
import energy.EnergyScheduler;

import java.util.List;
import java.util.ArrayList;

public class FuzzTest {

    // 创建测试方法来模拟种子排序和能量调度
    public static void testSortingAndEnergyScheduling() {
        // 创建一些示例种子
        List<Seed> seeds = new ArrayList<>();
        seeds.add(new Seed(new byte[]{0, 1, 2}, 100, 200, 1));  // 100覆盖率，200执行时间，入队顺序1
        seeds.add(new Seed(new byte[]{3, 4, 5}, 150, 300, 2));  // 150覆盖率，300执行时间，入队顺序2
        seeds.add(new Seed(new byte[]{6, 7, 8}, 120, 250, 3));  // 120覆盖率，250执行时间，入队顺序3

        // 打印原始种子列表
        System.out.println("Before Sorting and Scheduling:");
        seeds.forEach(System.out::println);

        // 按照能量排序
        SeedSorter.sortSeeds(seeds, "energy");
        System.out.println("\nAfter Sorting by Energy:");
        seeds.forEach(System.out::println);

        // 按照覆盖率排序
        SeedSorter.sortSeeds(seeds, "coverage");
        System.out.println("\nAfter Sorting by Coverage:");
        seeds.forEach(System.out::println);

        // 按照执行时间排序
        SeedSorter.sortSeeds(seeds, "executionTime");
        System.out.println("\nAfter Sorting by Execution Time:");
        seeds.forEach(System.out::println);

        // 按照入队顺序排序
        SeedSorter.sortSeeds(seeds, "enqueueOrder");
        System.out.println("\nAfter Sorting by Enqueue Order:");
        seeds.forEach(System.out::println);
    }

    // 主函数，调用测试方法
    public static void main(String[] args) {
        testSortingAndEnergyScheduling();
    }
}

