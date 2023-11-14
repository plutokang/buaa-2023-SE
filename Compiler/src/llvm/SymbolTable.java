package llvm;

import java.util.ArrayList;
import java.util.HashMap;
import llvm.value.*;
public class SymbolTable {
    ArrayList<SymbolTable> sonList = new ArrayList<>();
    SymbolTable parentTable = null;
    HashMap<String,Value> varMap = new HashMap<>();
    public int registeNum = 0;
    public SymbolTable(SymbolTable parentTable)
    {
        this.parentTable = parentTable;
    }
    public void addToSonList(SymbolTable son)
    {
        this.sonList.add(son);
    }
    public void addTovarHashMap(String name,Value var)
    {
        this.varMap.put(name,var);
    }
}
