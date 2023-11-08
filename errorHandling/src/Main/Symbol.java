package Main;

import java.util.ArrayList;

public class Symbol {
    // 如果是变量，isConst是False，如果是常量isConst是True
    //dimension1表示第一维长度，dimension2表示第二维长度
    String name = null;
    int dimension = 0;
    boolean isConst = false;
    //如果是函数定义，如果是void类型函数，isVoid为true，paramsNum 为参数数量，paramsType是每个参数的维度
    boolean isVoid = false;
    int paramsNum = 0;
    ArrayList<Integer> paramsType = new ArrayList<>();
    boolean isFunc = false;
    public Symbol(String name,int dimension,boolean isConst)
    {
        this.name = name;
        this.dimension = dimension;
        this.isConst = isConst;
        this.isFunc = false;
    }
    public Symbol(String name, boolean isVoid,int paramsNum,ArrayList<Integer>paramsType)
    {
        this.name = name;
        this.isVoid = isVoid;
        this.paramsNum = paramsNum;
        this.paramsType = paramsType;
        this.isFunc = true;
    }

}
