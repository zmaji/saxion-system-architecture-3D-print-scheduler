package nl.saxion.models.printers;

import nl.saxion.models.prints.FilamentType;
import nl.saxion.models.prints.PrintTask;

public class HousedMultiColor extends MultiColor {

    public HousedMultiColor(int id, String printerName, String manufacturer, int maxX, int maxY, int maxZ, int maxColors) {
        super(id, printerName, manufacturer, maxX, maxY, maxZ, maxColors);
    }

    @Override
    public boolean printerCompatibleWithTask(PrintTask printTask) {
        return printTask.getFilamentType() == FilamentType.ABS && printTask.getColors().size() <= this.getMaxColors();
    }
}
