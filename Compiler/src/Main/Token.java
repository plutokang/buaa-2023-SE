package Main;
public class Token {
    public int lineNum;
    String value;
    String type;
    public Token(int lineNum,String value,String type)
    {
        this.lineNum = lineNum;
        this.value = value;
        this.type = type;
    }
}
