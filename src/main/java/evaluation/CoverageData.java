package evaluation;

public class CoverageData {
    private String time;
    private double coverage;

    public CoverageData(String version, double coverage) {
        this.time = version;
        this.coverage = coverage;
    }

    public String getTime() {
        return time;
    }

    public double getCoverage() {
        return coverage;
    }
}
