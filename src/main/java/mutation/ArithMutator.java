package mutation;

import seedsort.Seed;

import java.util.Random;

public class ArithMutator implements Mutator {
    private final Random random = new Random();

    @Override
    public Seed mutate(Seed seed) {
        byte[] mutated = seed.data.clone();
        // 随机选择一个位置进行算术变异
        int index = random.nextInt(mutated.length);
        // 随机选择一种算术操作（0:加, 1:减, 2:乘, 3:除）
        int operation = random.nextInt(4);
        switch (operation) {
            case 0:
                // 加法
                mutated[index] += 1;
                break;
            case 1:
                // 确保减法操作不会导致负数（这里简单处理，可根据实际情况优化）
                if (mutated[index] > 0) {
                    mutated[index] -= 1;
                }
                break;
            case 2:
                // 乘法（简单乘以2，可根据实际情况调整）
                mutated[index] *= 2;
                break;
            case 3:
                // 确保除法操作不会导致除零错误（这里简单处理，可根据实际情况优化）
                if (mutated[index]!= 0) {
                    mutated[index] /= 2;
                }
                break;
        }
        return new Seed(mutated, seed.coverage, seed.executionTime, seed.enqueueOrder);
    }
}