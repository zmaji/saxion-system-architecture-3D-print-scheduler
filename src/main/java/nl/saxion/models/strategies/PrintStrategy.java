package nl.saxion.models.strategies;

public interface PrintStrategy {
    public void calculatePrintTime();
    public void calculateTotalCost();
    public String toString();
}
