package mutation;
import seedsort.Seed;

import java.util.Random;

public class BitflipMutator implements Mutator {
    private final Random random = new Random();

    @Override
    public Seed mutate(Seed seed) {
        byte[] mutated = seed.data.clone();
        // 随机选择一个字节进行位翻转
        int index = random.nextInt(mutated.length);
        mutated[index] = (byte) ~mutated[index];
        return new Seed(mutated, seed.coverage, seed.executionTime, seed.enqueueOrder);
    }
}
