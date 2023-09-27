//index使用的原则是，index处于当前使用的位置，使用之后next（）

package Main;

import Node.*;
import Node.Number;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Parser {
    ArrayList<Token> tokenList = new ArrayList<>();
    int index = 0;
    CompUnit compUnit = null;
    ArrayList<String> stringList = new ArrayList<>();

    public Parser(ArrayList<Token> tokenList) {

        this.tokenList = tokenList;
        compUnit = ComUnitBuild();
    }

    void next() {
        index++;
    }

    private Token getToken() {
        Token temp = tokenList.get(index);
        next();
        stringList.add(temp.type + " " + temp.value);
        return temp;
    }

    public void print() {
        String filePath = "output.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (int i = 0; i < this.stringList.size(); i++) {
                writer.write(this.stringList.get(i));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //CompUnit → {Decl} {FuncDef} MainFuncDef
    public CompUnit ComUnitBuild() {
        ArrayList<Decl> declList = new ArrayList<>();
        ArrayList<FuncDef> funcDefList = new ArrayList<>();
        MainFuncDef mainFun = null;
        while (index < tokenList.size()) {
            if (!tokenList.get(index + 1).type.equals("MAINTK") && !tokenList.get(index + 2).type.equals("LPARENT")) {
                Decl temp = DeclBuild();
                declList.add(temp);
            } else if (!tokenList.get(index + 1).type.equals("MAINTK") && tokenList.get(index + 2).type.equals("LPARENT")) {
                FuncDef temp = funcDefBuild();
                funcDefList.add(temp);
            } else if (tokenList.get(index + 1).type.equals("MAINTK")) {
                mainFun = MainFuncDefBuild();
            }
        }
        stringList.add("<CompUnit>");
        return new CompUnit(declList, funcDefList, mainFun);
    }

    // Decl → ConstDecl | VarDecl
    Decl DeclBuild() {
        if (tokenList.get(index).type.equals("CONSTTK")) {
            return new Decl(constDeclBuild());
        } else {
            return new Decl(varDeclBuild());
        }
    }


    //    ConstDecl → 'const' BType ConstDef { ',' ConstDef } ';'
    ConstDecl constDeclBuild() {
        Token constToken = getToken();
        BType btype = BTypeBuild();
        ArrayList<ConstDef> constDefList = new ArrayList<>();
        ArrayList<Token> commaList = new ArrayList<>();
        constDefList.add(constDefBuild());
        while (!tokenList.get(index).type.equals("SEMICN")) {
            if (tokenList.get(index).type.equals("COMMA")) {
                commaList.add(getToken());
            } else {
                constDefList.add(constDefBuild());
            }
        }
        Token semicToken = getToken();
        stringList.add("<ConstDecl>");
        return new ConstDecl(constToken, btype, constDefList, commaList, semicToken);
    }


    //  BType → 'int'
    BType BTypeBuild() {
        Token intToken = getToken();
        return new BType(intToken);
    }


    // ConstDef → Ident { '[' ConstExp ']' } '=' ConstInitVal
    ConstDef constDefBuild() {
        Token ident = getToken();
        ArrayList<Token> leftBraces = new ArrayList<>();
        ArrayList<Token> rightBraces = new ArrayList<>();
        ArrayList<ConstExp> constExps = new ArrayList<>();
        while (!tokenList.get(index).type.equals("ASSIGN")) {
            leftBraces.add(getToken());
            constExps.add(constExpBuild());
            rightBraces.add((getToken()));
        }
        Token assToken = getToken();
        ConstInitVal constInitVal = constInitValBuild();
        stringList.add("<ConstDef>");
        return new ConstDef(ident, constExps, assToken, constInitVal, leftBraces, rightBraces);
    }

    // ConstInitVal → ConstExp | '{' [ ConstInitVal { ',' ConstInitVal } ] '}'
    ConstInitVal constInitValBuild() {
        if (tokenList.get(index).type.equals("LBRACE")) {
            Token leftBrace = getToken();
            ArrayList<ConstInitVal> constInitVals = new ArrayList<>();
            ArrayList<Token> commaList = new ArrayList<>();
            if (!tokenList.get(index).type.equals("RBRACE")) constInitVals.add(constInitValBuild());
            while (!tokenList.get(index).type.equals("RBRACE")) {
                commaList.add(getToken());
                constInitVals.add(constInitValBuild());
            }
            Token rightBrace = getToken();
            stringList.add("<ConstInitVal>");
            return new ConstInitVal(leftBrace, constInitVals, rightBrace, commaList);
        } else {
            ConstExp constExp = constExpBuild();
            stringList.add("<ConstInitVal>");
            return new ConstInitVal(constExp);
        }
    }

    //VarDecl → BType VarDef { ',' VarDef } ';'
    VarDecl varDeclBuild() {
        BType btype = BTypeBuild();
        ArrayList<VarDef> varDefs = new ArrayList<>();
        ArrayList<Token> commas = new ArrayList<>();
        Token semiComma;
        varDefs.add(varDefBuild());
        while (tokenList.get(index).type.equals("COMMA")) {
            commas.add(getToken());
            varDefs.add(varDefBuild());
        }
        semiComma = getToken();
        stringList.add("<VarDecl>");
        return new VarDecl(btype, varDefs, commas, semiComma);
    }

    // VarDef → Ident { '[' ConstExp ']' } | Ident { '[' ConstExp ']' } '=' InitVal
    VarDef varDefBuild() {
        Token ident = getToken();
        ArrayList<Token> leftBraces = new ArrayList<>();
        ArrayList<Token> rightBraces = new ArrayList<>();
        ArrayList<ConstExp> constExps = new ArrayList<>();
        while (tokenList.get(index).type.equals("LBRACK")) {
            leftBraces.add(getToken());
            constExps.add(constExpBuild());
            rightBraces.add(getToken());
        }
        if (tokenList.get(index).type.equals("ASSIGN")) {
            Token ass = getToken();
            InitVal initVal = initValBuild();
            stringList.add("<VarDef>");
            return new VarDef(ident, leftBraces, constExps, rightBraces, ass, initVal);
        } else {
            stringList.add("<VarDef>");
            return new VarDef(ident, leftBraces, constExps, rightBraces);
        }
    }

    // InitVal → Exp | '{' [ InitVal { ',' InitVal } ] '}'
    InitVal initValBuild() {
        if (!tokenList.get(index).type.equals("LBRACE")) {
            Exp exp = expBuild();
            stringList.add("<InitVal>");
            return new InitVal(exp);
        } else {
            Token leftBrace = getToken();
            ArrayList<InitVal> initVals = new ArrayList<>();
            ArrayList<Token> commas = new ArrayList<>();
            if (!tokenList.get(index).type.equals("RBRACE")) {
                initVals.add(initValBuild());
            }
            while (!tokenList.get(index).type.equals("RBRACE")) {
                commas.add(getToken());
                initVals.add(initValBuild());
            }
            Token rightBrace = getToken();
            stringList.add("<InitVal>");
            return new InitVal(leftBrace, rightBrace, initVals, commas);
        }
    }

    //FuncDef → FuncType Ident '(' [FuncFParams] ')' Block
    FuncDef funcDefBuild() {
        FuncType type = funcTypeBuild();
        Token ident = getToken();
        Token leftBrace = getToken();
        FuncFParams funcFParams = null;
        if (!tokenList.get(index).type.equals("RPARENT")) {
            funcFParams = funcFParamsBuild();
        }
        Token rightBrace = getToken();
        Block block = blockBuild();
        stringList.add("<FuncDef>");
        return new FuncDef(type, ident, leftBrace, rightBrace, funcFParams, block);
    }

    //    MainFuncDef → 'int' 'main' '(' ')' Block
    MainFuncDef MainFuncDefBuild() {
        Token intToken = getToken();
        Token mainToken = getToken();
        Token leftBrace = getToken();
        Token rightBrace = getToken();
        Block block = blockBuild();
        stringList.add("<MainFuncDef>");
        return new MainFuncDef(intToken, mainToken, leftBrace, rightBrace, block);
    }

    //    FuncType → 'void' | 'int'
    FuncType funcTypeBuild() {
        Token ident = getToken();
        stringList.add("<FuncType>");
        return new FuncType(ident);
    }

    //FuncFParams → FuncFParam { ',' FuncFParam }
    FuncFParams funcFParamsBuild() {
        ArrayList<FuncFParam> funcFParams = new ArrayList<>();
        ArrayList<Token> commas = new ArrayList<>();
        funcFParams.add(funcFParamBuild());
        while (tokenList.get(index).type.equals("COMMA")) {
            commas.add(getToken());
            funcFParams.add(funcFParamBuild());
        }
        stringList.add("<FuncFParams>");
        return new FuncFParams(commas, funcFParams);
    }

    //FuncFParam → BType Ident ['[' ']' { '[' ConstExp ']' }]
    FuncFParam funcFParamBuild() {
        BType bType = BTypeBuild();
        Token ident = getToken();
        Token firstLeftBrace = null;
        Token firstRightBrace = null;
        Token secondLeftBrace = null;
        Token secondRightBrace = null;
        ConstExp constExp = null;
        if (tokenList.get(index).type.equals("LBRACK")) {
            firstLeftBrace = getToken();
            firstRightBrace = getToken();
            if (tokenList.get(index).type.equals("LBRACK")) {
                secondLeftBrace = getToken();
                constExp = constExpBuild();
                secondRightBrace = getToken();
            }
        }
        stringList.add("<FuncFParam>");
        return new FuncFParam(bType, ident, firstLeftBrace, firstRightBrace, secondLeftBrace, secondRightBrace, constExp);
    }

    //  Block → '{' { BlockItem } '}'
    Block blockBuild() {
        Token leftBrace = getToken();
        ArrayList<BlockItem> blockItems = new ArrayList<>();
        while (!tokenList.get(index).type.equals("RBRACE")) {
            blockItems.add(blockItemBuild());
        }
        Token rightBrace = getToken();
        stringList.add("<Block>");
        return new Block(leftBrace, rightBrace, blockItems);
    }

    // BlockItem → Decl | Stmt
    BlockItem blockItemBuild() {
        if (tokenList.get(index).type.equals("CONSTTK") || tokenList.get(index).type.equals("INTTK")) {
            return new BlockItem(DeclBuild());
        } else {
            return new BlockItem(stmtBuild());
        }
    }

    //Stmt
    Stmt stmtBuild() {
        //block
        if (tokenList.get(index).type.equals("LBRACE")) {
            Block block = blockBuild();
            stringList.add("<Stmt>");
            return new Stmt(block);
        }
        // 'if' '(' Cond ')' Stmt [ 'else' Stmt ]
        else if (tokenList.get(index).type.equals("IFTK")) {
            Token ifToken = getToken();
            Token leftBrace = getToken();
            Cond cond = condBuild();
            Token rightBrace = getToken();
            Stmt stmt1 = stmtBuild();
            Token elseToken = null;
            Stmt stmt2 = null;
            if (tokenList.get(index).type.equals("ELSETK")) {
                elseToken = getToken();
                stmt2 = stmtBuild();
            }
            stringList.add("<Stmt>");
            return new Stmt(ifToken, leftBrace, rightBrace, cond, stmt1, elseToken, stmt2);
        }
        //'for' '(' [ForStmt] ';' [Cond] ';' [ForStmt] ')' Stmt
        else if (tokenList.get(index).type.equals("FORTK")) {
            Token forToken = getToken();
            Token leftBrace = getToken();
            ForStmt forStmt1 = null;
            if (!tokenList.get(index).type.equals("SEMICN"))
                forStmt1 = forStmtBuild();
            Token semiToken1 = getToken();
            Cond cond = null;
            if (!tokenList.get(index).type.equals("SEMICN"))
                cond = condBuild();
            Token semiToken2 = getToken();
            ForStmt forStmt2 = null;
            if (!tokenList.get(index).type.equals("RPARENT"))
                forStmt2 = forStmtBuild();
            Token rightBrace = getToken();
            Stmt stmt = stmtBuild();
            stringList.add("<Stmt>");
            return new Stmt(forToken, leftBrace, rightBrace, forStmt1, semiToken1, cond, semiToken2, forStmt2, stmt);
        }
        //'break' ';' | 'continue' ';'
        else if (tokenList.get(index).type.equals("CONTINUETK") || tokenList.get(index).type.equals("BREAKTK")) {
            Token t = getToken();
            Token semi = getToken();
            stringList.add("<Stmt>");
            return new Stmt(t, semi);
        }
        //'return' [Exp] ';'
        else if (tokenList.get(index).type.equals("RETURNTK")) {
            Token returnToken = getToken();
            Exp exp = null;
            if (!tokenList.get(index).type.equals("SEMICN")) {
                exp = expBuild();
            }
            Token semi = getToken();
            stringList.add("<Stmt>");
            return new Stmt(returnToken, exp, semi);
        }
        //  'printf''('FormatString{','Exp}')'';'
        else if (tokenList.get(index).type.equals("PRINTFTK")) {
            Token printf = getToken();
            Token leftBrace = getToken();
            Token formatString = getToken();
            ArrayList<Token> commas = new ArrayList<>();
            ArrayList<Exp> exps = new ArrayList<>();
            while (!tokenList.get(index).type.equals("RPARENT")) {
                commas.add(getToken());
                exps.add(expBuild());
            }
            Token rightBrace = getToken();
            Token semi = getToken();
            stringList.add("<Stmt>");
            return new Stmt(printf, leftBrace, rightBrace, formatString, commas, exps, semi);
        } else {
            boolean flag1 = false;
            boolean flag2 = false;
            for (int i = 0; !tokenList.get(index + i).type.equals("SEMICN"); i++) {
                if (tokenList.get(index + i).type.equals("GETINTTK")) {
                    flag1 = true;
                }
                if (tokenList.get(index + i).type.equals("ASSIGN")) {
                    flag2 = true;
                }
            }
            // [Exp] ';'
            if (!flag2) {
                Exp exp = null;
                if (!tokenList.get(index).type.equals("SEMICN"))
                    exp = expBuild();
                Token semi = getToken();
                stringList.add("<Stmt>");
                return new Stmt(exp, semi);
            } else {
                // LVal '=' Exp ';'
                if (!flag1) {
                    LVal lVal = lValBuild();
                    Token ass = getToken();
                    Exp exp = expBuild();
                    Token semi = getToken();
                    stringList.add("<Stmt>");
                    return new Stmt(lVal, ass, exp, semi);
                }
                // LVal '=' 'getint''('')'';'
                else {
                    LVal lVal = lValBuild();
                    Token ass = getToken();
                    Token getIntToken = getToken();
                    Token leftBrace = getToken();
                    Token rightBrace = getToken();
                    Token semi = getToken();
                    stringList.add("<Stmt>");
                    return new Stmt(lVal, ass, getIntToken, leftBrace, rightBrace, semi);
                }

            }
        }
    }

    // ForStmt → LVal '=' Exp
    ForStmt forStmtBuild() {
        LVal lVal = lValBuild();
        Token assign = getToken();
        Exp exp = expBuild();
        stringList.add("<ForStmt>");
        return new ForStmt(lVal, assign, exp);
    }

    // Exp → AddExp
    Exp expBuild() {
        AddExp addExp = addExpBuild();
        stringList.add("<Exp>");
        return new Exp(addExp);
    }

    // Cond → LOrExp
    Cond condBuild() {
        LOrExp lOrExp = lOrExpBuild();
        stringList.add("<Cond>");
        return new Cond(lOrExp);
    }

    //LVal → Ident {'[' Exp ']'}
    LVal lValBuild() {
        Token ident = getToken();
        ArrayList<Token> leftBraces = new ArrayList<>();
        ArrayList<Token> rightBraces = new ArrayList<>();
        ArrayList<Exp> exps = new ArrayList<>();
        if (tokenList.get(index).type.equals("LBRACK")) {
            leftBraces.add(getToken());
            exps.add(expBuild());
            rightBraces.add(getToken());
        }
        if (tokenList.get(index).type.equals("LBRACK")) {
            leftBraces.add(getToken());
            exps.add(expBuild());
            rightBraces.add(getToken());
        }
        stringList.add("<LVal>");
        return new LVal(ident, leftBraces, rightBraces, exps);
    }

    // PrimaryExp → '(' Exp ')' | LVal | Number
    PrimaryExp primaryExpBuild() {
        if (tokenList.get(index).type.equals("LPARENT")) {
            Token leftBrace = getToken();
            Exp exp = expBuild();
            Token rightBrace = getToken();
            stringList.add("<PrimaryExp>");
            return new PrimaryExp(leftBrace, rightBrace, exp);
        } else if (tokenList.get(index).type.equals("INTCON")) {
            Number num = numberBuild();
            stringList.add("<PrimaryExp>");
            return new PrimaryExp(num);
        } else {
            LVal lVal = lValBuild();
            stringList.add("<PrimaryExp>");
            return new PrimaryExp(lVal);
        }
    }

    Number numberBuild() {
        Token num = getToken();
        stringList.add("<Number>");
        return new Number(num);
    }

    // UnaryExp → PrimaryExp | Ident '(' [FuncRParams] ')' | UnaryOp UnaryExp
    UnaryExp unaryExpBuild() {
        if (!(tokenList.get(index).type.equals("IDENFR") && tokenList.get(index + 1).type.equals("LPARENT")) && !(tokenList.get(index).type.equals("PLUS") || tokenList.get(index).type.equals("MINU") || tokenList.get(index).type.equals("NOT"))) {
            PrimaryExp primaryExp = primaryExpBuild();
            stringList.add("<UnaryExp>");
            return new UnaryExp(primaryExp);
        } else if (tokenList.get(index).type.equals("PLUS") || tokenList.get(index).type.equals("MINU") || tokenList.get(index).type.equals("NOT")) {
            UnaryOp op = unaryOpBuild();
            UnaryExp exp = unaryExpBuild();
            stringList.add("<UnaryExp>");
            return new UnaryExp(op, exp);
        } else {
            Token ident = getToken();
            Token leftBrace = getToken();
            FuncRParams funcRParams = null;
            if (!tokenList.get(index).type.equals("RPARENT")) {
                funcRParams = funcRParamsBuild();
            }
            Token rightBrace = getToken();
            stringList.add("<UnaryExp>");
            return new UnaryExp(ident, leftBrace, rightBrace, funcRParams);
        }
    }

    //UnaryOp → '+' | '−' | '!'
    UnaryOp unaryOpBuild() {
        Token ident = getToken();
        stringList.add("<UnaryOp>");
        return new UnaryOp(ident);
    }

    // FuncRParams → Exp { ',' Exp }
    FuncRParams funcRParamsBuild() {
        ArrayList<Token> commas = new ArrayList<>();
        ArrayList<Exp> exps = new ArrayList<>();
        exps.add(expBuild());
        while (tokenList.get(index).type.equals("COMMA")) {
            commas.add(getToken());
            exps.add(expBuild());
        }
        stringList.add("<FuncRParams>");
        return new FuncRParams(exps, commas);
    }

    //  MulExp → UnaryExp | MulExp ('*' | '/' | '%') UnaryExp
    // 消除左递归后成为 UnaryExp {('*' | '/' | '%') UnaryExp}
    MulExp mulExpBuild() {
        ArrayList<UnaryExp> unaryExps = new ArrayList<>();
        ArrayList<Token> signals = new ArrayList<>();
        unaryExps.add(unaryExpBuild());
        while (tokenList.get(index).type.equals("MULT") || tokenList.get(index).type.equals("DIV") || tokenList.get(index).type.equals("MOD")) {
            stringList.add("<MulExp>");
            signals.add(getToken());
            unaryExps.add(unaryExpBuild());
        }
        stringList.add("<MulExp>");
        return new MulExp(unaryExps, signals);
    }

    // AddExp → MulExp | AddExp ('+' | '−') MulExp
    // 消除左递归后为 MulExp{('+' | '−') MulExp}

    AddExp addExpBuild() {
        ArrayList<MulExp> mulExps = new ArrayList<>();
        ArrayList<Token> signals = new ArrayList<>();
        mulExps.add(mulExpBuild());
        while (tokenList.get(index).type.equals("PLUS") || tokenList.get(index).type.equals("MINU")) {
            stringList.add("<AddExp>");
            signals.add(getToken());
            mulExps.add(mulExpBuild());
        }
        stringList.add("<AddExp>");
        return new AddExp(mulExps, signals);
    }

    // RelExp → AddExp | RelExp ('<' | '>' | '<=' | '>=') AddExp
    // 消除左递归后变成 AddExp{('<' | '>' | '<=' | '>=') AddExp}
    RelExp relExpBuild() {
        ArrayList<AddExp> addExps = new ArrayList<>();
        ArrayList<Token> signals = new ArrayList<>();
        addExps.add(addExpBuild());
        while (tokenList.get(index).type.equals("LSS") || tokenList.get(index).type.equals("GRE") || tokenList.get(index).type.equals("LEQ") || tokenList.get(index).type.equals("GEQ")) {
            stringList.add("<RelExp>");
            signals.add(getToken());
            addExps.add(addExpBuild());
        }
        stringList.add("<RelExp>");
        return new RelExp(addExps, signals);
    }

    //  EqExp → RelExp | EqExp ('==' | '!=') RelExp
    // 消除左递归后变成 RelExp{('==' | '!=')RelExp}

    EqExp eqExpBuild() {
        ArrayList<RelExp> relExps = new ArrayList<>();
        ArrayList<Token> signals = new ArrayList<>();
        relExps.add(relExpBuild());
        while (tokenList.get(index).type.equals("EQL") || tokenList.get(index).type.equals("NEQ")) {
            stringList.add("<EqExp>");
            signals.add(getToken());
            relExps.add(relExpBuild());
        }
        stringList.add("<EqExp>");
        return new EqExp(relExps, signals);
    }

    //  LAndExp → EqExp | LAndExp '&&' EqExp
    //  消除左递归后变成 EqExp{'&&'EqExp}
    LAndExp lAndExpBuild() {
        ArrayList<Token> signals = new ArrayList<>();
        ArrayList<EqExp> exps = new ArrayList<>();
        exps.add(eqExpBuild());
        while (tokenList.get(index).type.equals("AND")) {
            stringList.add("<LAndExp>");
            signals.add(getToken());
            exps.add(eqExpBuild());
        }
        stringList.add("<LAndExp>");
        return new LAndExp(exps, signals);
    }

    //  LOrExp → LAndExp | LOrExp '||' LAndExp
    // 消除左递归后变成 LAndExp{'||'LAndExp}

    LOrExp lOrExpBuild() {
        ArrayList<Token> signals = new ArrayList<>();
        ArrayList<LAndExp> exps = new ArrayList<>();
        exps.add(lAndExpBuild());
        while (tokenList.get(index).type.equals("OR")) {
            stringList.add("<LOrExp>");
            signals.add(getToken());
            exps.add(lAndExpBuild());
        }
        stringList.add("<LOrExp>");
        return new LOrExp(exps, signals);
    }


    // ConstExp → AddExp
    ConstExp constExpBuild() {
        AddExp addExp = addExpBuild();
        stringList.add("<ConstExp>");
        return new ConstExp(addExp);
    }
}