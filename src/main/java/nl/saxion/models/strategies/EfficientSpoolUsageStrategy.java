package nl.saxion.models.strategies;

public class EfficientSpoolUsageStrategy implements PrintStrategy {
    @Override
    public void calculatePrintTime() {
        System.out.println("Calculated time with the use of efficient spool strategy: 75");
    }

    @Override
    public void calculateTotalCost() {
        System.out.println("Calculated cost with the use of efficient spool strategy: 50");
    }

    @Override
    public String toString() {
        return "Efficient Spool Usage";
    }
}
