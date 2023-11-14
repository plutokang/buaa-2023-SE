package llvm.value;

import llvm.type.Type;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Value {
    public String name;
    public Type type;
    public ArrayList<User> userList = new ArrayList<>();
    public int value = 0;

    public Value(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public void print() {

    }

    public void printToFile() {

    }

    public void addToUserlist(User user) {
        this.userList.add(user);
    }


}
