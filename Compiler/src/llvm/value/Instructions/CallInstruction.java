package llvm.value.Instructions;

import llvm.Module;
import llvm.type.FunctionType;
import llvm.type.Type;
import llvm.type.VoidType;
import llvm.value.FunctionValue;
import llvm.value.Value;

import java.util.ArrayList;

public class CallInstruction extends Instruction {
    ArrayList<Value> paramList = new ArrayList<>();
    Value function = null;
    Value result = null;

    public CallInstruction(Value function, ArrayList<Value> paramList, Value result) {
        super(null, new VoidType());
        this.function = function;
        this.paramList = paramList;
        this.result = result;
    }

    public void print() {
        if (((FunctionValue) function).isVoid) {
            System.out.print("call void @" + function.name + "(");
            for (int i = 0; i < paramList.size(); i++) {
                System.out.print("i32 ");
                paramList.get(i).print();
                if (i != paramList.size() - 1)
                    System.out.print(",");
            }
            System.out.print(")\n");

        } else {
            result.print();
            System.out.print(" = call i32 @" + function.name + "(");
            for (int i = 0; i < paramList.size(); i++) {
                System.out.print("i32 ");
                paramList.get(i).print();
                if (i != paramList.size() - 1)
                    System.out.print(",");
            }
            System.out.print(")\n");
        }
    }
    public void printToFile()
    {
        Module module = Module.getModule();
        if (((FunctionValue) function).isVoid) {
            module.printForChile("call void @" + function.name + "(");
            for (int i = 0; i < paramList.size(); i++) {
                module.printForChile("i32 ");
                paramList.get(i).printToFile();
                if (i != paramList.size() - 1)
                    module.printForChile(",");
            }
            module.printForChile(")\n");

        } else {
            result.printToFile();
            module.printForChile(" = call i32 @" + function.name + "(");
            for (int i = 0; i < paramList.size(); i++) {
                module.printForChile("i32 ");
                paramList.get(i).printToFile();
                if (i != paramList.size() - 1)
                    module.printForChile(",");
            }
            module.printForChile(")\n");
        }
    }
}
