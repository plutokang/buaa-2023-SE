package llvm.value;

import llvm.type.Type;

public class User extends Value{
    public User(String name, Type type) {
        super(name, type);
    }
}
