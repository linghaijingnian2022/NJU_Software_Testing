package energy;
//能量调度组件


import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class EnergyScheduler {

//    // 启发式能量调度方法，依据覆盖率和执行时间调整能量
//    public static void adjustEnergy(Seed seed) {
//        // 基于覆盖率调整能量，覆盖率越高，能量越大
//        double coverageFactor = seed.coverage / 100.0;
//
//        // 基于执行时间调整能量，执行时间越短，能量越高（假设执行时间短表示更有效）
//        double executionTimeFactor = 1.0 / (seed.executionTime + 1);
//
//        // 最终能量计算：覆盖率 * 执行时间的倒数，确保两者都影响最终能量
//        seed.energy = coverageFactor * 10 + executionTimeFactor * 5;
//    }
}

