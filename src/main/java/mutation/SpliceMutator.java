package mutation;

import seedsort.Seed;

import java.util.ArrayList;
import java.util.List;

public class SpliceMutator implements Mutator {
    private final List<byte[]> seedPool;

    public SpliceMutator(List<byte[]> seedPool) {
        this.seedPool = seedPool;
    }

    @Override
    public Seed mutate(Seed seed) {
        byte[] mutated = seed.data.clone();
        if (!seedPool.isEmpty()) {
            byte[] otherSeed = seedPool.get((int) (Math.random() * seedPool.size()));
            byte[] newBytes = new byte[mutated.length + otherSeed.length];
            System.arraycopy(mutated, 0, newBytes, 0, mutated.length);
            System.arraycopy(otherSeed, 0, newBytes, mutated.length, otherSeed.length);
            mutated = newBytes;
        }
        return new Seed(mutated, seed.coverage, seed.executionTime, seed.enqueueOrder);
    }
}
