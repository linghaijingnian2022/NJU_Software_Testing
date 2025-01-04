package seedsort;

import energy.EnergyScheduler;
import java.util.List;
import java.util.ArrayList;

public class SeedSorter {

    // 按入队顺序进行排序
    public static void sortByEnqueueOrder(List<Seed> seeds) {
        seeds.sort((s1, s2) -> Integer.compare(s1.enqueueOrder, s2.enqueueOrder));
    }

    //排序种子


    // 基于覆盖率排序
    public static void sortByCoverage(List<Seed> seeds) {
        seeds.sort((s1, s2) -> Integer.compare(s2.coverage, s1.coverage)); // 降序
    }

    // 基于执行时间排序
    public static void sortByExecutionTime(List<Seed> seeds) {
        seeds.sort((s1, s2) -> Long.compare(s1.executionTime, s2.executionTime)); // 升序
    }

    // 使用能量调度组件对种子进行排序（根据能量值）
    public static void sortByEnergy(List<Seed> seeds) {
        EnergyScheduler.sortSeedsByEnergy(seeds);
    }

    // 统一的排序方法：可以根据需求选择不同的排序方式
    public static void sortSeeds(List<Seed> seeds, String sortMethod) {
        switch (sortMethod) {
            case "coverage":
                sortByCoverage(seeds);
                break;
            case "executionTime":
                sortByExecutionTime(seeds);
                break;
            case "enqueueOrder":
                sortByEnqueueOrder(seeds);
                break;
            case "energy":
                sortByEnergy(seeds);
                break;
            default:
                throw new IllegalArgumentException("Unknown sort method: " + sortMethod);
        }
    }
}
