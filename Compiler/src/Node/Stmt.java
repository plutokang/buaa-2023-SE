//Stmt → LVal '=' Exp ';' // 每种类型的语句都要覆盖
//        | [Exp] ';' //有无Exp两种情况
//        | Block
//        | 'if' '(' Cond ')' Stmt [ 'else' Stmt ] // 1.有else 2.无else
//        | 'for' '(' [ForStmt] ';' [Cond] ';' [ForStmt] ')' Stmt // 1. 无缺省 2. 缺省第一个
//        ForStmt 3. 缺省Cond 4. 缺省第二个ForStmt
//        | 'break' ';' | 'continue' ';'
//        | 'return' [Exp] ';' // 1.有Exp 2.无Exp
//        | LVal '=' 'getint''('')'';'
//        | 'printf''('FormatString{','Exp}')'';'
package Node;

import Main.Token;

import java.util.ArrayList;

public class Stmt {
    public LVal lVal = null;
    public Token eq = null;
    public Exp exp = null;
    public Token SemiComma = null;
    public Block block = null;
    public Token ifToken = null;
    public Token leftBrace = null;
    public Cond cond = null;
    public Token rightBrace = null;
    public Stmt stmtIf = null;
    public Token elseToken = null;
    public Stmt stmtElse = null;
    public Token forToken = null;
    public ForStmt forStmtAfterFor = null;
    public Token afterForSemi = null;
    public Token afterCondSemi = null;
    public ForStmt forStmtAfterCond = null;
    public Stmt forStmt = null;
    public Token breakOrContinueToken = null;
    public Token returnToken = null;
    public Token getInt = null;
    public Token printfToken = null;
    public Token formatString = null;
    public ArrayList<Token> commaArray = null;
    public ArrayList<Exp> expArray = null;

    public Stmt(LVal lVal, Token eq, Exp exp, Token semiComma) //LVal '=' Exp ';'
    {
        this.lVal = lVal;
        this.eq = eq;
        this.exp = exp;
        this.SemiComma = semiComma;
    }

    public Stmt(Exp exp, Token semiComma) //[Exp] ';'
    {
        this.exp = exp;
        this.SemiComma = semiComma;
    }

    public Stmt(Block block) //Block
    {
        this.block = block;
    }

    public Stmt(Token ifToken, Token leftBrace, Token rightBrace, Cond cond, Stmt stmtIf, Token elseToken, Stmt stmtElse) // 'if' '(' Cond ')' Stmt [ 'else' Stmt ]
    {
        this.ifToken = ifToken;
        this.leftBrace = leftBrace;
        this.rightBrace = rightBrace;
        this.cond = cond;
        this.stmtIf = stmtIf;
        this.elseToken = elseToken;
        this.stmtElse = stmtElse;
    }

    public Stmt(Token forToken, Token leftBrace, Token rightBrace, ForStmt forStmtAfterFor, Token afterForSemi, Cond cond, Token afterCondSemi, ForStmt forStmtAfterCond, Stmt stmt)// 'for' '(' [ForStmt] ';' [Cond] ';' [ForStmt] ')' Stmt
    {
        this.forToken = forToken;
        this.leftBrace = leftBrace;
        this.rightBrace = rightBrace;
        this.forStmtAfterFor = forStmtAfterFor;
        this.afterForSemi = afterForSemi;
        this.cond = cond;
        this.afterCondSemi = afterCondSemi;
        this.forStmtAfterCond = forStmtAfterCond;
        this.forStmt = stmt;
    }

    public Stmt(Token breakOrContinueToken, Token semiComma) // break;|continue;
    {
        this.breakOrContinueToken = breakOrContinueToken;
        this.SemiComma = semiComma;
    }

    public Stmt(Token returnToken, Exp exp, Token semiComma) // 'return' [Exp] ';'
    {
        this.returnToken = returnToken;
        this.exp = exp;
        this.SemiComma = semiComma;
    }

    public Stmt(LVal lVal, Token eq, Token getInt, Token leftBrace, Token rightBrace, Token semiComma) // LVal '=' 'getint''('')'';'
    {
        this.lVal = lVal;
        this.eq = eq;
        this.getInt = getInt;
        this.leftBrace = leftBrace;
        this.rightBrace = rightBrace;
        this.SemiComma = semiComma;
    }

    public Stmt(Token printfToken, Token leftBrace, Token rightBrace, Token formatString, ArrayList<Token> commaArray, ArrayList<Exp> expArray, Token semiComma) //  'printf''('FormatString{','Exp}')'';'    {
    {
        this.printfToken = printfToken;
        this.leftBrace = leftBrace;
        this.rightBrace = rightBrace;
        this.formatString = formatString;
        this.commaArray = commaArray;
        this.expArray = expArray;
        this.SemiComma = semiComma;
    }

}
