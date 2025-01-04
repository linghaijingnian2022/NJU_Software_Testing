package executor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import seedsort.Seed;

public class TestExecutor {
    private final Seed seed;
    private final ProcessRunner processRunner;

    public TestExecutor(Seed seed, ProcessRunner processRunner) {
        this.seed = seed;
        this.processRunner = processRunner;
    }

    public void executeFuzzTarget(String targetPath, String seedPath) throws IOException {
        byte[] seedData = readSeed(seedPath);
        processRunner.runFuzzTarget(targetPath, seedData);
    }

    private byte[] readSeed(String seedPath) throws IOException {
        File file = new File(seedPath);
        byte[] buffer = new byte[(int) file.length()];
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(buffer);
        }
        return buffer;
    }
}
