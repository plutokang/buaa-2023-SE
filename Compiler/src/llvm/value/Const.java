package llvm.value;

import llvm.type.Type;

public class Const extends Value{
    public Const(String name, Type type) {
        super(name, type);
    }
}
