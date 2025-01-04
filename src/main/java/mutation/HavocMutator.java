package mutation;

import seedsort.Seed;

import java.util.Random;

public class HavocMutator implements Mutator {
    private final Random random = new Random();

    @Override
    public Seed mutate(Seed seed) {
        byte[] mutated = seed.data.clone();
        int numMutations = random.nextInt(32); // 随机选择0 - 31次变异操作
        for (int i = 0; i < numMutations; i++) {
            int operation = random.nextInt(3); // 0:替换, 1:删除, 2:插入
            switch (operation) {
                case 0: // 替换
                    if (mutated.length > 0) {
                        int index = random.nextInt(mutated.length);
                        mutated[index] = (byte) random.nextInt(256);
                    }
                    break;
                case 1: // 删除
                    if (mutated.length > 1) {
                        int startIndex = random.nextInt(mutated.length - 1);
                        int endIndex = Math.min(startIndex + random.nextInt(mutated.length - startIndex), mutated.length);
                        byte[] newBytes = new byte[mutated.length - (endIndex - startIndex)];
                        System.arraycopy(mutated, 0, newBytes, 0, startIndex);
                        System.arraycopy(mutated, endIndex, newBytes, startIndex, mutated.length - endIndex);
                        mutated = newBytes;
                    }
                    break;
                case 2: // 插入
                    byte[] newByte = new byte[1];
                    newByte[0] = (byte) random.nextInt(256);
                    byte[] newBytes = new byte[mutated.length + 1];
                    int insertIndex = random.nextInt(mutated.length + 1);
                    System.arraycopy(mutated, 0, newBytes, 0, insertIndex);
                    System.arraycopy(newByte, 0, newBytes, insertIndex, 1);
                    System.arraycopy(mutated, insertIndex, newBytes, insertIndex + 1, mutated.length - insertIndex);
                    mutated = newBytes;
                    break;
            }
        }
        return new Seed(mutated, seed.coverage, seed.executionTime, seed.enqueueOrder);
    }
}
