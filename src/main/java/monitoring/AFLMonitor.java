package monitoring;


import org.jacoco.agent.AgentJar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public class AFLMonitor {
    private CoverageCollector coverageCollector;
    private ErrorLogger errorLogger;
    private ResultAnalyzer resultAnalyzer;

    public AFLMonitor() {
        this.coverageCollector = new CoverageCollector("target/jacoco-agent.jar","jacoco.exec");
        this.errorLogger = new ErrorLogger();
        this.resultAnalyzer = new ResultAnalyzer();
    }

    public void monitor(Process process) {
        try {
            // 监控执行状态，设置超时
            boolean finished = process.waitFor(5, TimeUnit.SECONDS);
            if (!finished) {
                process.destroy(); // 超时后终止进程
                System.out.println("Process timed out and was terminated.");
            } else {
                // 收集覆盖率信息
                coverageCollector.collectCoverage();
                // 捕获错误信息
                errorLogger.logErrors(process);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 分析结果
        resultAnalyzer.analyzeResults();
    }
}


class ErrorLogger {
    public void logErrors(Process process) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // 记录错误信息
                System.err.println("Error: " + line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

