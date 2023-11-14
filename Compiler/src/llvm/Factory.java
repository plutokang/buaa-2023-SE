package llvm;

import llvm.type.FunctionType;
import llvm.type.Pointer;
import llvm.value.*;
import llvm.value.Instructions.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Factory {
    int registerNum = 0;
    int blockNum = 1;

    public int allocBasicBlock() {
        int temp = this.blockNum;
        blockNum++;
        return temp;
    }

    public int allocRegister() {
        int temp = Generator.curFunction.registNum;
        Generator.curFunction.registNum++;
        return temp;
    }

    public void refreshRegisterNum() {
        this.registerNum = 0;
    }

    public void refreshBlockNum() {
        this.blockNum = 0;
    }

    public FunctionValue buildFunction(String name, boolean isVoid, boolean isLibrary) {
        FunctionType type = new FunctionType(isVoid);
        FunctionValue function = new FunctionValue(name, type, isVoid, isLibrary);
        Module module = Module.getModule();
        if (!name.equals("main"))
            module.addToFunctionList(function);
        else
            module.addMainFunc(function);
        return function;
    }

    public BasicBlock buildBasicBlock() {
        return new BasicBlock(allocRegister());
    }

    public VarValue buildVar(String name, boolean isConst, boolean isGlobal, SymbolTable curTable) {
        FunctionValue functionValue = Generator.curFunction;
        VarValue var = new VarValue(name, (functionValue == null) ? 0 : allocRegister(), isConst, isGlobal, 32);
        return var;
    }

    public ConstInt buildConstInt(int value) {
        return new ConstInt(value);
    }

    //instructions
    public void buildAllocInstructions(VarValue varValue, BasicBlock basicBlock) {
        varValue.type = new Pointer(varValue.width);
        AllocInstruction allocInstruction = new AllocInstruction(varValue);
        varValue.addToUserlist(allocInstruction);
        if (basicBlock != null)
            basicBlock.addToInstruction(allocInstruction);
    }

    public void buildStoreInstruction(Value fromValue, Value toValue, BasicBlock basicBlock) {
        StoreInstruction storeInstruction = new StoreInstruction(fromValue, toValue);
        if(!(((VarValue) toValue).isGlobal && ((VarValue) toValue).isChanged))
        {
            toValue.value = fromValue.value;
        }
        ((VarValue) toValue).isChanged = true;
        fromValue.addToUserlist(storeInstruction);
        toValue.addToUserlist(storeInstruction);
        if (basicBlock != null)
            basicBlock.addToInstruction(storeInstruction);
    }

    public void buildLoadInstruction(Value fromValue, Value toValue, BasicBlock basicBlock) {
        LoadInstruction loadInstruction = new LoadInstruction(fromValue, toValue);
//        toValue.value = fromValue.value;
        fromValue.addToUserlist(loadInstruction);
        toValue.addToUserlist(loadInstruction);
        if (basicBlock != null)
            basicBlock.addToInstruction(loadInstruction);
    }

    public CalcuInstruction buildCalcuInstruction(Value valuea, Value valueb, Value result, String signal, BasicBlock basicBlock) {
        CalcuInstruction calcuInstruction = new CalcuInstruction(valuea, valueb, signal, result);
        valuea.addToUserlist(calcuInstruction);
        valueb.addToUserlist(calcuInstruction);
        if (basicBlock != null)
            basicBlock.addToInstruction(calcuInstruction);
        return calcuInstruction;
    }

    public void buildRetInstruction(Value value, boolean isVoid, BasicBlock basicBlock) {
        RetInstruction retInstruction;
        if (isVoid)
            retInstruction = new RetInstruction();
        else {
            retInstruction = new RetInstruction(value);
            value.addToUserlist(retInstruction);
        }
        if (basicBlock != null)
            basicBlock.addToInstruction(retInstruction);
    }

    public void buildCallInstruction(Value function, ArrayList<Value> paramList, Value result, BasicBlock basicBlock) {
        CallInstruction callInstruction = new CallInstruction(function, paramList, result);
        if (!((FunctionValue) function).isVoid) {
            result.addToUserlist(callInstruction);
        }
        for (Value value : paramList) {
            value.addToUserlist(callInstruction);
        }
        if (basicBlock != null)
            basicBlock.addToInstruction(callInstruction);
    }
}
