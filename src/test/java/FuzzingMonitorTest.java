import monitoring.FuzzingMonitor;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class FuzzingMonitorTest {

    @Test
    public void testGetCoverage() {
        FuzzingMonitor monitor = new FuzzingMonitor();
        monitor.recordExecution(new byte[]{1, 2, 3}, 70, 100, 1, false);
        monitor.recordExecution(new byte[]{4, 5, 6}, 80, 200, 2, true);
        assertEquals(0.02, monitor.getCoverage(), 0.01); // 假设总测试用例为100
    }

    @Test
    public void testGetExecutionSpeed() {
        FuzzingMonitor monitor = new FuzzingMonitor();
        monitor.startMonitoring();
        monitor.recordExecution(new byte[]{1}, 70, 100, 1, false);
        monitor.recordExecution(new byte[]{2}, 80, 200, 2, false);
        double speed = monitor.getExecutionSpeed();
        assertTrue(speed > 0); // 验证执行速度大于0
    }
}
