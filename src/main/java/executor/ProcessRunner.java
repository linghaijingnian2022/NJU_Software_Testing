package executor;
import java.io.IOException;

public class ProcessRunner {
    public void runFuzzTarget(String targetPath, byte[] input) throws IOException {
        ProcessBuilder pb = new ProcessBuilder(targetPath);
        pb.redirectInput(ProcessBuilder.Redirect.PIPE);
        Process process = pb.start();
        process.getOutputStream().write(input);
        process.getOutputStream().close();
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
