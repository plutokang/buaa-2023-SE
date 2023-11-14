package llvm.value.Instructions;

import llvm.type.Type;
import llvm.value.User;
import llvm.value.Value;

public class Instruction extends User {

    public Instruction(String name, Type type) {
        super(name, type);
    }
}
