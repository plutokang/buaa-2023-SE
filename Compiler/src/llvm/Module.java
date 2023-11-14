package llvm;

import llvm.type.VarType;
import llvm.value.FunctionValue;
import llvm.value.VarValue;

import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class Module {
    private static Module instance;
    ArrayList<VarValue> globalVarList = new ArrayList<>();
    ArrayList<FunctionValue> functionList = new ArrayList<>();
    ArrayList<FunctionValue> globalFuncList = new ArrayList<>();
    FunctionValue mainFunction = null;
    public BufferedWriter writer = null;
    private String filePath = "llvm_ir.txt";

    private Module() {
        buildWriter();
    }

    public static Module getModule() {
        // 只有当instance为null时才创建实例
        if (instance == null) {
            // 类锁确保在多线程环境中只创建一个实例
            synchronized (Module.class) {
                // 再次检查以确保instance在等待锁的时间里没有被创建
                if (instance == null) {
                    instance = new Module();
                }
            }
        }
        return instance;
    }

    public void addToGlobalVar(VarValue varValue) {
        this.globalVarList.add(varValue);
    }

    public void addToFunctionList(FunctionValue function) {
        this.functionList.add(function);
    }

    public void addMainFunc(FunctionValue functionValue) {
        this.mainFunction = functionValue;
    }
    public void addToGlobalFunction(FunctionValue functionValue)
    {
        this.globalFuncList.add(functionValue);
    }

    public void print() {
        for(FunctionValue functionValue : globalFuncList)
            functionValue.printGlobalToConsole();
        for (VarValue value : globalVarList)
            value.printToGlobalConsole();
        for (FunctionValue functionValue : functionList)
            functionValue.print();
        mainFunction.print();
    }

    public void printToFile() {
        for(FunctionValue functionValue : globalFuncList)
            functionValue.printGlobalToFIle();
        for (VarValue value : globalVarList)
            value.printToGlobalFile();
        for (FunctionValue functionValue : functionList)
            functionValue.printToFile();
        mainFunction.printToFile();
    }

    private void buildWriter() {
        try {
            writer = new BufferedWriter(new FileWriter(this.filePath));
        } catch (IOException e) {
            e.printStackTrace();
            // 可以考虑再抛出异常或者处理这个异常，取决于你的错误处理策略
        }
    }

    public void printForChile(String string) { // 方法名改为更合适的名字
        try {
            writer.write(string);
            writer.flush(); // 确保字符串立即写入文件而不是缓存在内存中
        } catch (IOException e) {
            // 根据你的需要，这里可以捕获并处理异常，或者重新抛出
            throw new RuntimeException(e);
        }
    }

    public void closeWriter() {
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
//    public void testPrint()
//    {
//        String filePath = "output.txt";
//        String content = " %0";
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
//            writer.write(content);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


}
