package executor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ProcessRunner {
    public static List<String> runProcess(String command) throws IOException {
        Process process = Runtime.getRuntime().exec(command);

        // 用于存储标准输出和标准错误信息的列表
        List<String> output = new ArrayList<>();

        // 读取标准输出流
        InputStream isOut = process.getInputStream();
        BufferedReader readerOut = new BufferedReader(new InputStreamReader(isOut));
        String lineOut;
        while ((lineOut = readerOut.readLine())!= null) {
            output.add("标准输出: " + lineOut);
        }

        // 读取标准错误流
        InputStream isErr = process.getErrorStream();
        BufferedReader readerErr = new BufferedReader(new InputStreamReader(isErr));
        String lineErr;
        while ((lineErr = readerErr.readLine())!= null) {
            output.add("标准错误: " + lineErr);
        }

        // 等待进程执行完毕并获取退出值
        int exitValue;
        try {
            exitValue = process.waitFor();
            output.add("退出值: " + exitValue);
        } catch (InterruptedException e) {
            // 处理中断异常，这里简单记录并重新设置中断状态
            output.add("进程等待被中断: " + e.getMessage());
            Thread.currentThread().interrupt();
            exitValue = -1;
        }

        return output;
    }
}
