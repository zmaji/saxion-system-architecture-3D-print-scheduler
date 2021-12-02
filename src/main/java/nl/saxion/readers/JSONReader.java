package nl.saxion.readers;

import java.util.ArrayList;
import java.util.List;

abstract public class JSONReader<T> {
    final protected String source;
    protected List<T> readItems = new ArrayList<>();

    public JSONReader(String source) {
        this.source = source;
    }

    public abstract List<T> readPrintsFromFile() throws ReaderException;
}
