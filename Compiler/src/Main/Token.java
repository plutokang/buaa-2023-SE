package Main;
public class Token {
    public int lineNum;
    public String value;
    public String type;
    public Token(int lineNum,String value,String type)
    {
        this.lineNum = lineNum;
        this.value = value;
        this.type = type;
    }
}
