package mutation;

import java.util.Random;

public class InterestMutator implements Mutator {
    private final Random random = new Random();

    @Override
    public byte[] mutate(byte[] input) {
        byte[] mutated = input.clone();
        // 选择特定的有趣值进行替换
        if (input.length > 0) {
            // 例如将第一个字节替换为0
            mutated[0] = 0;
        }
        return mutated;
    }
}
