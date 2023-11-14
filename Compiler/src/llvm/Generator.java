package llvm;

import Node.*;
import Node.Number;
import llvm.type.Pointer;
import llvm.value.*;
import llvm.value.Instructions.CalcuInstruction;
import llvm.value.Instructions.RetInstruction;

import javax.print.attribute.standard.PDLOverrideSupported;
import java.util.ArrayList;


public class Generator {
    public SymbolTable rootTable = null;
    static public SymbolTable curTable = null;
    public BasicBlock curBasicBlock = null;
    static public FunctionValue curFunction = null;
    public Factory factory = new Factory();

    public Generator(CompUnit compUnit) {
        initTable();
        addLibraryFunction();
        passASTTree(compUnit);
        completeReturn();
    }
    private void completeReturn()
    {
        Module module = Module.getModule();
        for(int i = 0;i < module.functionList.size();i++)
        {
            if(module.functionList.get(i).isVoid){
                for(BasicBlock basicblock : module.functionList.get(i).basicBlockList)
                {
                    int len = basicblock.instructionList.size() - 1;
                    if(!(basicblock.instructionList.get(len) instanceof RetInstruction))
                        factory.buildRetInstruction(null,true,basicblock);
                }
            }
        }
    }
    public void addLibraryFunction() {
        FunctionValue functionValue1 = factory.buildFunction("getint", false, true);
        FunctionValue functionValue2 = factory.buildFunction("putint", true, true);
        VarValue varValue = factory.buildVar(null, false, false, null);
        functionValue1.addToParams(varValue);
        FunctionValue functionValue3 = factory.buildFunction("putch", true, true);
        varValue = factory.buildVar(null, false, false, null);
        functionValue3.addToParams(varValue);
        Module module = Module.getModule();
        module.addToGlobalFunction(functionValue1);
        module.addToGlobalFunction(functionValue2);
        module.addToGlobalFunction(functionValue3);
        module.addToFunctionList(functionValue1);
        module.addToFunctionList(functionValue2);
        module.addToFunctionList(functionValue3);
    }

    private void initTable() {
        this.rootTable = new SymbolTable(null);
        this.curTable = this.rootTable;
    }

    public void buildTable() {
        SymbolTable newTable = new SymbolTable(curTable);
        curTable.addToSonList(newTable);
        curTable = newTable;
        factory.refreshRegisterNum();

    }

    private void addToModuleGlobalVar() {
        Module module = Module.getModule();
        for (Value value : rootTable.varMap.values()) {
            module.addToGlobalVar((VarValue) value);
        }
    }

    private void passASTTree(CompUnit compUnit) {
        for (int i = 0; i < compUnit.DeclArray.size(); i++)
            visitDecl(compUnit.DeclArray.get(i));
        addToModuleGlobalVar();
        for (int i = 0; i < compUnit.FuncArray.size(); i++)
            visitFunction(compUnit.FuncArray.get(i));
        visiteMainFunc(compUnit.main);
    }

    private Value getVar(String name) {
        SymbolTable temp = curTable;
        while (temp != null) {
            Value var = temp.varMap.get(name);
            if (var != null)
                return var;
            temp = temp.parentTable;
        }
        return null;
    }

    private Value getFunction(String name) {
        Module module = Module.getModule();
        for (int i = 0; i < module.functionList.size(); i++) {
            if (module.functionList.get(i).name.equals(name))
                return module.functionList.get(i);
        }
        return null;
    }

    private void visitFunction(FuncDef function) {
        buildTable();
        FunctionValue functionValue = factory.buildFunction(function.Ident.value, function.functype.type.value.equals("void"), false);
        this.curFunction = functionValue;
        if (function.funcFParams != null)
            visitFParams(function.funcFParams);
        BasicBlock basicBlock = factory.buildBasicBlock();
        this.curBasicBlock = basicBlock;
        curFunction.addToBasicBlockList(basicBlock);
        for (Value value : curFunction.paramsList) {
            VarValue addr = factory.buildVar(value.name, false, false, curTable);
            factory.buildAllocInstructions(addr, curBasicBlock);
            factory.buildStoreInstruction(value, addr, curBasicBlock);
            curTable.addTovarHashMap(addr.name, addr);
        }
        visitBlock(function.block);
    }

    private void visiteMainFunc(MainFuncDef mainFuncDef) {
        buildTable();
        FunctionValue mainFunc = factory.buildFunction("main", false, false);
        this.curFunction = mainFunc;
        BasicBlock basicBlock = factory.buildBasicBlock();
        this.curBasicBlock = basicBlock;
        curFunction.addToBasicBlockList(basicBlock);
        visitBlock(mainFuncDef.block);
    }

    private void visitBlock(Block block) {
        for (int i = 0; i < block.BlockItemArray.size(); i++)
            visitBlockItem(block.BlockItemArray.get(i));
        curTable = curTable.parentTable;
    }

    private void visitBlockItem(BlockItem item) {
        if (item.decl != null)
            visitDecl(item.decl);
        else
            visitStmt(item.stmt);

    }

    private void visitDecl(Decl decl) {
        if (decl.varDecl != null)
            visitVarDecl(decl.varDecl);
        else
            visitConstDecl(decl.constDecl);
    }

    private void visitVarDecl(VarDecl varDecl) {
        for (VarDef varDef : varDecl.varDefs)
            visitVarDef(varDef, varDecl.btype);
    }

    private void visitVarDef(VarDef varDef, BType btype) {
        if (varDef.leftBraceArray.isEmpty()) {
            VarValue var = factory.buildVar(varDef.Ident.value, false, curFunction == null, curTable);
            curTable.addTovarHashMap(varDef.Ident.value, var);
            factory.buildAllocInstructions(var, curBasicBlock);
            if (varDef.initVal != null) {
                Value value = visitInitVal(varDef.initVal);
                factory.buildStoreInstruction(value, var, curBasicBlock);
            }
        } else {
            // TODO: 2023/11/5  for Array
        }
    }

    private Value visitInitVal(InitVal initVal) {
        if (initVal.leftBrace == null)
            return visitExp(initVal.exp);
        else {
            // TODO: 2023/11/4 for Array
            return null;
        }
    }

    private Value visitExp(Exp exp) {
        return visitAddExp(exp.addExp);
    }

    private Value visitAddExp(AddExp addExp) {
        if (addExp.signals.isEmpty())
            return visitMulExp(addExp.mulExps.get(0));
        else {
            Value valuea = visitMulExp(addExp.mulExps.get(0));
            for (int i = 0; i < addExp.mulExps.size() - 1; i++) {
                Value valueb = visitMulExp(addExp.mulExps.get(i + 1));
                VarValue result = factory.buildVar(null, false, false, curTable);
                CalcuInstruction calcuInstruction = factory.buildCalcuInstruction(valuea, valueb, result, addExp.signals.get(i).value, curBasicBlock);
                valuea = calcuInstruction.result;
            }
            return valuea;
        }

    }

    private Value visitMulExp(MulExp mulExp) {
        if (mulExp.signals.isEmpty())
            return visitUnaryExp(mulExp.unaryExps.get(0));
        else {
            Value valuea = visitUnaryExp(mulExp.unaryExps.get(0));
            for (int i = 0; i < mulExp.unaryExps.size() - 1; i++) {
                Value valueb = visitUnaryExp(mulExp.unaryExps.get(i + 1));
                Value result = factory.buildVar(null, false, false, curTable);
                CalcuInstruction calcuInstruction = factory.buildCalcuInstruction(valuea, valueb, result, mulExp.signals.get(i).value, curBasicBlock);
                valuea = calcuInstruction.result;
            }
            return valuea;
        }

    }

    private Value visitUnaryExp(UnaryExp unaryExp) {
        //一个变量或者一个数字
        if (unaryExp.primaryExp != null) {
            return visitPrimaryExp(unaryExp.primaryExp);
        }
        //函数
        else if (unaryExp.Ident != null) {
            ArrayList<Value> paramList = new ArrayList<>();
            if (unaryExp.funcRParams != null) {
                for (int i = 0; i < unaryExp.funcRParams.expArray.size(); i++) {
                    paramList.add(visitExp(unaryExp.funcRParams.expArray.get(i)));
                }
            }
            Value function = getFunction(unaryExp.Ident.value);
            if (((FunctionValue) function).isVoid) {
                factory.buildCallInstruction(function, paramList, null, curBasicBlock);
                return null;
            } else {
                Value var = factory.buildVar(null, false, false, curTable);
                factory.buildCallInstruction(function, paramList, var, curBasicBlock);
                return var;
            }

        }
        //有符号的函数，数字或者变量
        else if (unaryExp.unaryOp != null) {
            ConstInt constInt = factory.buildConstInt(0);
            Value value = visitUnaryExp(unaryExp.unaryExp);
            VarValue result = factory.buildVar(null, false, false, curTable);
            factory.buildCalcuInstruction(constInt, value, result, unaryExp.unaryOp.Op.value, curBasicBlock);
            return result;
            // TODO: 2023/11/4
        }
        return null;
    }

    private Value visitPrimaryExp(PrimaryExp primaryExp) {
        if (primaryExp.number != null)
            return visitNumber(primaryExp.number);
        else if (primaryExp.lVal != null) {
            Value var = visitLval(primaryExp.lVal);
            if (var.type instanceof Pointer) {
                Value temp = factory.buildVar(null, false, false, curTable);
                factory.buildLoadInstruction(var, temp, curBasicBlock);
                var = temp;
            }
            return var;
        }
        else if(primaryExp.exp != null)
            return visitExp(primaryExp.exp);
        else return null;
    }

    private Value visitLval(LVal lVal) {
        if (lVal.expArray.isEmpty()) {
            Value var = getVar(lVal.Ident.value);
            return var;
        } else {
            // TODO: 2023/11/5 forArray
            return null;
        }
    }

    private Value visitNumber(Number number) {
        Value value = factory.buildConstInt(Integer.parseInt(number.intConst.value));
        return value;
    }

    private void visitConstDecl(ConstDecl constDecl) {
        for (ConstDef constDef : constDecl.constDefsList)
            visitConstDef(constDef);

    }

    private void visitConstDef(ConstDef constDef) {
        if (constDef.leftBraces.isEmpty()) {
            VarValue var = factory.buildVar(constDef.Ident.value, true, (curFunction == null), curTable);
            curTable.addTovarHashMap(constDef.Ident.value, var);
            factory.buildAllocInstructions(var, curBasicBlock);
            Value value = visitConstInitVal(constDef.constInitVal);
            factory.buildStoreInstruction(value, var, curBasicBlock);
        } else {
            //todo
        }
    }

    private Value visitConstInitVal(ConstInitVal constInitVal) {
        if (constInitVal.constExp != null)
            return visitConstExp(constInitVal.constExp);
        else {
            // TODO: 2023/11/5  for array
            return null;
        }
    }

    private Value visitConstExp(ConstExp constExp) {
        return visitAddExp(constExp.addExp);
    }

    private void visitStmt(Stmt stmt) {
        if (stmt.lVal != null && stmt.exp != null) {
            Value lval = visitLval(stmt.lVal);
            Value exp = visitExp(stmt.exp);
            factory.buildStoreInstruction(exp, lval, curBasicBlock);
        } else if (stmt.ifToken != null) {
            // TODO: 2023/11/5 for if
            return;
        } else if (stmt.forToken != null) {
            // TODO: 2023/11/5 for for
            return;
        } else if (stmt.returnToken != null) {
            if (stmt.exp == null)
                factory.buildRetInstruction(null, true, curBasicBlock);
            else {
                Value var = visitExp(stmt.exp);
                factory.buildRetInstruction(var, false, curBasicBlock);

            }
        } else if (stmt.exp != null)
            visitExp(stmt.exp);
        else if (stmt.getInt != null) {
            Value var = visitLval(stmt.lVal);
            Value value = factory.buildVar(null, false, false, curTable);
            Value function = getFunction("getint");
            factory.buildCallInstruction(function, new ArrayList<>(), value, curBasicBlock);
            factory.buildStoreInstruction(value, var, curBasicBlock);
        } else if (stmt.printfToken != null) {
            String str = stmt.formatString.value;

            int pos = 0, paramNum = 0;
            while (pos < str.length()) {
                if (str.charAt(pos) == '"') {
                    pos++;
                    continue;
                } else if (str.charAt(pos) == '%') {
                    pos++;
                    Value value = visitExp(stmt.expArray.get(paramNum));
                    paramNum++;
                    pos++;
                    Value functionValue = getFunction("putint");
                    ArrayList<Value> values = new ArrayList<>();
                    values.add(value);
                    factory.buildCallInstruction(functionValue, values, null, curBasicBlock);
                } else if (str.charAt(pos) == '\\') {
                    pos++;
                    if (str.charAt(pos) == 'n') {
                        pos++;
                        Value value = factory.buildConstInt(10);
                        Value functionValue = getFunction("putch");
                        ArrayList<Value> values = new ArrayList<>();
                        values.add(value);
                        factory.buildCallInstruction(functionValue, values, null, curBasicBlock);
                    }
                } else {
                    int asciiCode = str.charAt(pos);
                    Value value = factory.buildConstInt(asciiCode);
                    Value functionValue = getFunction("putch");
                    ArrayList<Value> values = new ArrayList<>();
                    values.add(value);
                    factory.buildCallInstruction(functionValue, values, null, curBasicBlock);
                    pos++;
                }
            }
        } else if (stmt.block != null) {
            buildTable();
            visitBlock(stmt.block);
        }

    }

    private void visitFParams(FuncFParams funcFParams) {
        for (int i = 0; i < funcFParams.funcFParamArray.size(); i++)
            visitFParam(funcFParams.funcFParamArray.get(i));
    }

    private void visitFParam(FuncFParam funcFParam) {
        if (funcFParam.firstLeftBrace == null) {
            VarValue var = factory.buildVar(funcFParam.Ident.value, true, false, curTable);
            curTable.addTovarHashMap(funcFParam.Ident.value, var);
            curFunction.addToParams(var);
            this.curTable.addTovarHashMap(funcFParam.Ident.value, var);
        } else {
            //todo
        }
    }
}
