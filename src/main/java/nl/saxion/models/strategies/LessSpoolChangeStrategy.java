package nl.saxion.models.strategies;

public class LessSpoolChangeStrategy implements PrintStrategy {
    @Override
    public void calculatePrintTime() {
        System.out.println("Calculated time with the use of less spools strategy: 100");
    }

    @Override
    public void calculateTotalCost() {
        System.out.println("Calculated cost with the use of less spools strategy: 100");
    }

    @Override
    public String toString() {
        return "Less Spool Changes";
    }
}
