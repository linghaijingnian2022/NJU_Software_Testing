package evaluation;

public class CoverageData {
    private String time;
    private double coverage;

    public CoverageData(String time, double coverage) {
        this.time = time;
        this.coverage = coverage;
    }

    public String getTime() {
        return time;
    }

    public double getCoverage() {
        return coverage;
    }
}