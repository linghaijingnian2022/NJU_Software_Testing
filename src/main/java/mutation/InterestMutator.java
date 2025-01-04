package mutation;

import seedsort.Seed;

import java.util.Random;

public class InterestMutator implements Mutator {
    private final Random random = new Random();

    @Override
    public Seed mutate(Seed seed) {
        byte[] mutated = seed.data.clone();  // 克隆原始输入，防止修改原始数据

        if (mutated.length > 0) {
            // 以某种概率选择变异方式
            int mutationChoice = random.nextInt(5);  // 这里有五种变异操作

            switch (mutationChoice) {
                case 0:
                    // 随机替换一个字节
                    int index = random.nextInt(mutated.length);  // 选择一个随机索引
                    mutated[index] = (byte) random.nextInt(256);  // 用一个随机字节替换
                    break;

                case 1:
                    // 将所有字节加一个随机偏移量
                    byte offset = (byte) random.nextInt(10);  // 随机偏移量在0~9之间
                    for (int i = 0; i < mutated.length; i++) {
                        mutated[i] = (byte) (mutated[i] + offset);  // 每个字节加上偏移量
                    }
                    break;

                case 2:
                    // 翻转数组中的所有字节
                    for (int i = 0; i < mutated.length; i++) {
                        mutated[i] = (byte) ~mutated[i];  // 对字节取反
                    }
                    break;

                case 3:
                    // 随机交换数组中的两个字节
                    int index1 = random.nextInt(mutated.length);
                    int index2 = random.nextInt(mutated.length);
                    byte temp = mutated[index1];
                    mutated[index1] = mutated[index2];
                    mutated[index2] = temp;
                    break;

                case 4:
                    // 将数组中的一个字节设置为固定值（例如：0x7F）
                    mutated[random.nextInt(mutated.length)] = (byte) 0x7F;
                    break;

                default:
                    break;
            }
        }

        return new Seed(mutated, seed.coverage, seed.executionTime, seed.enqueueOrder);
    }
}
