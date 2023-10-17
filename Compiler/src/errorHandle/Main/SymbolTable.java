package Main;

import java.util.ArrayList;
import java.util.HashMap;

public class SymbolTable {
    SymbolTable parrentTable = null;
    ArrayList<SymbolTable>nextTableList = new ArrayList<>();
    HashMap<String,Symbol>symbolMap = new HashMap<>();
    public SymbolTable(SymbolTable curTable)
    {
        this.parrentTable = curTable;
    }
}
