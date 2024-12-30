package executor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestExecutor {

    private final List<Long> executionTimes = new ArrayList<>();
    private int executionCount = 0;

    public void executeTest(File target, String[] args) {
        try {
            long startTime = System.currentTimeMillis();
            ProcessBuilder pb = new ProcessBuilder();
            pb.command().add(target.getAbsolutePath());
            for (String arg : args) {
                pb.command().add(arg);
            }
            Process process = pb.start();
            process.waitFor();
            long endTime = System.currentTimeMillis();
            executionTimes.add(endTime - startTime);
            executionCount++;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public List<Long> getExecutionTimes() {
        return executionTimes;
    }

    public int getExecutionCount() {
        return executionCount;
    }
}