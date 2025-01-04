package mutation;
import java.util.Random;

public class BitflipMutator implements Mutator {
    private final Random random = new Random();

    @Override
    public byte[] mutate(byte[] input) {
        byte[] mutated = input.clone();
        // 随机选择一个字节进行位翻转
        int index = random.nextInt(input.length);
        mutated[index] = (byte) ~input[index];
        return mutated;
    }
}
