package evaluation;

public class CoverageData {
    private String version;
    private double coverage;

    public CoverageData(String version, double coverage) {
        this.version = version;
        this.coverage = coverage;
    }

    public String getVersion() {
        return version;
    }

    public double getCoverage() {
        return coverage;
    }
}
