package energy;

import seedsort.Seed;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;

public class EnergyScheduler {

    // 权重，可以根据实际需求进行调整
    private static final double COVERAGE_WEIGHT = 0.7;
    private static final double EXECUTION_TIME_WEIGHT = 0.3;

    // 计算种子的能量，依据覆盖率和执行时间进行加权
    public static double calculateEnergy(Seed seed) {
        return (seed.coverage * COVERAGE_WEIGHT) + (seed.executionTime * EXECUTION_TIME_WEIGHT);
    }

    // 更新每个种子的能量
    public static void updateEnergy(List<Seed> seeds) {
        for (Seed seed : seeds) {
            seed.energy = calculateEnergy(seed);
        }
    }

    // 根据种子的能量值进行排序
    public static void sortSeedsByEnergy(List<Seed> seeds) {
        // 更新所有种子的能量
        updateEnergy(seeds);

        // 根据能量值排序，能量高的排在前面
        Collections.sort(seeds, new Comparator<Seed>() {
            @Override
            public int compare(Seed s1, Seed s2) {
                // 能量高的排前面
                return Double.compare(s2.energy, s1.energy);
            }
        });
    }
}



