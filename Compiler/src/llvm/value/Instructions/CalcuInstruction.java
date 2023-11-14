package llvm.value.Instructions;

import llvm.Module;
import llvm.type.Pointer;
import llvm.type.Type;
import llvm.type.VoidType;
import llvm.value.Value;
import llvm.value.VarValue;

public class CalcuInstruction extends Instruction {
    public String signal = null;
    public Value valueA = null;
    public Value valueB = null;
    public Value result = null;

    public CalcuInstruction(Value valueA, Value valueB, String signal, Value result) {
        super(null, new VoidType());
        this.valueA = valueA;
        this.valueB = valueB;
        this.signal = signal;
        this.result = getResult(result);
    }

    private Value getResult(Value result) {
        if (this.signal.equals("+"))
            result.value = this.valueA.value + this.valueB.value;
        else if (this.signal.equals("-"))
            result.value = this.valueA.value - this.valueB.value;
        else if (this.signal.equals("*"))
            result.value = this.valueA.value * this.valueB.value;
        else if (this.signal.equals("/"))
            if (this.valueB.value == 0)
                result.value = 0;
            else
                result.value = this.valueA.value / this.valueB.value;
        else if (this.signal.equals("%"))
            if (this.valueB.value == 0)
                result.value = 0;
            else
                result.value = this.valueA.value % this.valueB.value;
        return result;
    }

    private void printSignal(String signal) {
        this.result.print();
        System.out.print(" = " + signal + " i32");
        if (valueA.type instanceof Pointer)
            System.out.print("*");
        System.out.print(" ");
        this.valueA.print();
        System.out.print(",");
        if (valueB.type instanceof Pointer)
            System.out.print("i32* ");
        this.valueB.print();
        System.out.print("\n");
    }

    private void printSignalForStream(String signal) {
        Module module = Module.getModule();
        this.result.printToFile();
        module.printForChile(" = " + signal + " i32");
        module.printForChile(" ");
        this.valueA.printToFile();
        module.printForChile(",");
        this.valueB.printToFile();
        module.printForChile("\n");
    }

    @Override
    public void print() {
        if (this.signal.equals("+"))
            printSignal("add");
        else if (this.signal.equals("-"))
            printSignal("sub");
        else if (this.signal.equals("*"))
            printSignal("mul");
        else if (this.signal.equals("/"))
            printSignal("sdiv");
        else if (this.signal.equals("%"))
            printSignal("srem");
    }

    public void printToFile() {
        if (this.signal.equals("+"))
            printSignalForStream("add");
        else if (this.signal.equals("-"))
            printSignalForStream("sub");
        else if (this.signal.equals("*"))
            printSignalForStream("mul");
        else if (this.signal.equals("/"))
            printSignalForStream("sdiv");
        else if (this.signal.equals("%"))
            printSignalForStream("srem");
    }
}
