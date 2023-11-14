// CompUnit → {Decl} {FuncDef} MainFuncDef
// 传参为Decl的ArrayList FuncDef的ArrayList 和 MainFuncDef的main
package Node;

import java.util.ArrayList;

public class CompUnit {
    public ArrayList<Decl> DeclArray = new ArrayList<>();
    public ArrayList<FuncDef> FuncArray = new ArrayList<>();
    public MainFuncDef main ;
    public CompUnit(ArrayList<Decl> DeclArray, ArrayList<FuncDef> FuncArray, MainFuncDef main)
    {
        this.DeclArray = DeclArray;
        this.FuncArray = FuncArray;
        this.main = main;
    }
}
