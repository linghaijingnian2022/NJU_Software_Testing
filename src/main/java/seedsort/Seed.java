package seedsort;

import java.util.Arrays;

public class Seed {
    public byte[] data;        // 种子的字节数据
    public int coverage;       // 覆盖率
    public long executionTime; // 执行时间（毫秒）
    public int enqueueOrder;   // 入队顺序（用于按入队顺序排序）
    public double energy;      // 能量值（由能量调度组件计算得出）

    // 构造方法
    public Seed(byte[] data, int coverage, long executionTime, int enqueueOrder) {
        this.data = data;
        this.coverage = coverage;
        this.executionTime = executionTime;
        this.enqueueOrder = enqueueOrder;
        this.energy = 0.0; // 初始能量为0
    }

    @Override
    public String toString() {
        return "Seed{data=" + Arrays.toString(data) + ", coverage=" + coverage +
                ", executionTime=" + executionTime + ", enqueueOrder=" + enqueueOrder +
                ", energy=" + energy + "}";
    }
}

