import java.util.Map;
import java.util.HashMap;

public class SymbolTable {
    private Map<String, SymbolData> symbols = new HashMap<>();

    static class SymbolData {
        String dataType;
        int firstOccurrenceLineNumber;
        int totalFrequency = 0;
    }

    public SymbolTable() {}

    public void InsertIdentifier(String nameOfIdentifier, int lineNumber) {
        if (symbols.containsKey(nameOfIdentifier)) {
            symbols.get(nameOfIdentifier).totalFrequency++;
        }
        else {
            symbols.put(nameOfIdentifier, new SymbolData());
            symbols.get(nameOfIdentifier).firstOccurrenceLineNumber = lineNumber;
            symbols.get(nameOfIdentifier).totalFrequency = 1;
        }
    }

    public void PrintAllIdentifiers() {
        for (Map.Entry<String, SymbolData> entry : symbols.entrySet()) {
            System.out.print("Identifier: " + entry.getKey());
            System.out.print("\nFirst Occurred at Line: " + entry.getValue().firstOccurrenceLineNumber);
            System.out.print("\nTotal Frequency: " + entry.getValue().totalFrequency);
            System.out.println();
        }
    }
}
