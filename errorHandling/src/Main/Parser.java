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
    SymbolTable rootTable = new SymbolTable(null);
    SymbolTable curTable = rootTable;
    int index = 0;
    CompUnit compUnit = null;
    ArrayList<String> stringList = new ArrayList<>();
    ArrayList<String> errorType = new ArrayList<>();
    ArrayList<Integer> errorLineNum = new ArrayList<>();
    ArrayList<String> funcFName = new ArrayList<>();
    ArrayList<Integer> funcFDimension = new ArrayList<>();
    int inForNum = 0;
    int funfFLineNum = -1;

    public void printError() {
        String filePath = "error.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (int i = 0; i < this.errorType.size(); i++) {
                writer.write(String.valueOf(this.errorLineNum.get(i)) + ' ' + this.errorType.get(i) + '\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void error(String type, int lineNum) {
        errorType.add(type);
        errorLineNum.add(lineNum + 1);
    }

    private ArrayList<Integer> judgeReturnType(Block block) {
        ArrayList<Integer> returnTypeList = block.getReturnBlockItemType();
        ArrayList<Integer> returnLineList = block.getReturnBlockItemLine();
        ArrayList<Integer> errorLineList = new ArrayList<>();
        for (int i = 0; i < returnTypeList.size(); i++) {
            if (returnTypeList.get(i) == 1)
                errorLineList.add(returnLineList.get(i));
        }
        return errorLineList;
    }

    private boolean judgeSameNumInPrint(String formatString, int num) {
        int count = 0;
        for (int i = 0; i < formatString.length(); i++) {
            if (formatString.charAt(i) == '%')
                count++;
        }
        if (count != num)
            return false;
        return true;
    }

    private boolean judgeConstChange(LVal lVal) {
        Symbol symbol = searchSymbol(lVal.Ident.value);
        if (symbol != null && symbol.isConst)
            return false;
        return true;
    }

    private boolean judgeFormatString(String formatString) {
        if (formatString.charAt(0) != '"' || formatString.charAt(formatString.length() - 1) != '"')
            return false;
        for (int i = 1; i < formatString.length() - 1; i++) {
            if (formatString.charAt(i) == '\\') {
                if ((i == formatString.length() - 1) || (i < formatString.length() - 1 && formatString.charAt(i + 1) != 'n'))
                    return false;
            } else if (formatString.charAt(i) == '%') {
                if ((i == formatString.length() - 1) || (i < formatString.length() - 1 && formatString.charAt(i + 1) != 'd'))
                    return false;
            } else if (!(formatString.charAt(i) == 32 || formatString.charAt(i) == 33 || (formatString.charAt(i) >= 40 && formatString.charAt(i) <= 126)))
                return false;
        }
        return true;
    }

    //如果存在重命名，那么返回值是false
    private boolean judgeRename(String name) {
        Symbol temp = curTable.symbolMap.get(name);
        return temp == null ? true : false;
    }

    //如果存在，则返回true，否则返回false
    private boolean judgeExistSymbol(String name) {
        SymbolTable tempTable = curTable;
        do {
            Symbol temp = tempTable.symbolMap.get(name);
            if (temp != null)
                return true;
            tempTable = tempTable.parrentTable;
        } while (tempTable != null);
        return false;
    }

    private Symbol searchSymbol(String name) {
        SymbolTable tempTable = curTable;
        do {
            Symbol temp = tempTable.symbolMap.get(name);
            if (temp != null)
                return temp;
            tempTable = tempTable.parrentTable;
        } while (tempTable != null);
        return null;
    }

    private Symbol searchFunc(String name) {
        return rootTable.symbolMap.get(name);
    }

    private boolean judgeParamsNum(String name, int num) {
        Symbol func = searchFunc(name);
        if (func != null && func.isFunc == true && func.paramsNum == num)
            return true;
        return false;
    }

    private boolean judgeParamsType(String name, FuncRParams funcRParams) {
        Symbol func = searchFunc(name);
        ArrayList<Integer> funcRType = new ArrayList<>();
        if (funcRParams == null) {
            if (func.paramsNum == 0)
                return true;
            else
                return false;
        }
        if (func != null) {
            for (int i = 0; i < funcRParams.expArray.size(); i++) {
                Exp temp = funcRParams.expArray.get(i);
                UnaryExp unaryExp = temp.addExp.mulExps.get(0).unaryExps.get(0);
                if (unaryExp.primaryExp != null) {
                    PrimaryExp primaryExp = unaryExp.primaryExp;
                    if (primaryExp.number != null) {
                        funcRType.add(0);
                        continue;
                    } else if (primaryExp.lVal != null) {
                        int temp1 = searchSymbol(primaryExp.lVal.Ident.value).dimension;
                        funcRType.add(temp1 - primaryExp.lVal.leftBraceArray.size());
                        continue;
                    } else {
                        UnaryExp unaryExp1 = primaryExp.exp.addExp.mulExps.get(0).unaryExps.get(0);
                        while (unaryExp1.primaryExp != null && unaryExp1.primaryExp.exp != null)
                            unaryExp1 = unaryExp1.primaryExp.exp.addExp.mulExps.get(0).unaryExps.get(0);
                        if (unaryExp1.primaryExp == null) {
                            if (unaryExp1.Ident != null) {
                                Symbol func1 = searchFunc(unaryExp1.Ident.value);
                                if (func1.isVoid == true) {
                                    return false;
                                } else {
                                    funcRType.add(0);
                                    continue;
                                }
                            } else {
                                funcRType.add(0);
                                continue;
                            }
                        }
                    }
                } else if (unaryExp.Ident != null) {
                    Symbol func1 = searchFunc(unaryExp.Ident.value);
                    if (func1.isVoid == true) {
                        return false;
                    } else {
                        funcRType.add(0);
                        continue;
                    }
                } else {
                    funcRType.add(0);
                    continue;
                }
            }
        }
        for (int i = 0; i < funcRType.size(); i++) {
            if (func.paramsType.get(i) != funcRType.get(i)) {
                return false;
            }
        }
        return true;
    }

    private void test() {
        System.out.print(rootTable);
    }

    public Parser(ArrayList<Token> tokenList) {

        this.tokenList = tokenList;
        compUnit = ComUnitBuild();
        test();
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

    private void addVarAndConst(String name, int dimension, boolean isConst) {
        Symbol temp = new Symbol(name, dimension, isConst);
        this.curTable.symbolMap.put(name, temp);
    }

    private void addFunc(String name, boolean isVoid, int params, ArrayList<Integer> paramsType) {
        Symbol temp = new Symbol(name, isVoid, params, paramsType);
        this.curTable.symbolMap.put(name, temp);
    }

    //CompUnit → {Decl} {FuncDef} MainFuncDef
    public CompUnit ComUnitBuild() {
        ArrayList<Decl> declList = new ArrayList<>();
        ArrayList<FuncDef> funcDefList = new ArrayList<>();
        MainFuncDef mainFun = null;
        while (index < tokenList.size()) {
            if (!tokenList.get(index + 1).type.equals("MAINTK") && (index >= tokenList.size() - 2 || !tokenList.get(index + 2).type.equals("LPARENT"))) {
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
        while ((index < tokenList.size())&&(!tokenList.get(index).type.equals("SEMICN") && tokenList.get(index).lineNum == tokenList.get(index - 1).lineNum)) {
            if (tokenList.get(index).type.equals("COMMA")) {
                commaList.add(getToken());
            } else {
                constDefList.add(constDefBuild());
            }
        }
        Token semicToken = null;
        if ((index < tokenList.size())&&(tokenList.get(index).type.equals("SEMICN")))
            semicToken = getToken();
        else
            error("i", constToken.lineNum);
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
        while ((index < tokenList.size())&&!tokenList.get(index).type.equals("ASSIGN")) {
            leftBraces.add(getToken());
            constExps.add(constExpBuild());
            if ((index < tokenList.size())&&tokenList.get(index).type.equals("RBRACK"))
                rightBraces.add((getToken()));
            else {
                error("k", leftBraces.get(0).lineNum);
                rightBraces.add(null);
            }
        }
        Token assToken = getToken();
        ConstInitVal constInitVal = constInitValBuild();
        stringList.add("<ConstDef>");
        if (!judgeRename(ident.value))
            error("b", ident.lineNum);
        else addVarAndConst(ident.value, constExps.size(), true);
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
        while ((index < tokenList.size())&&tokenList.get(index).type.equals("COMMA")) {
            commas.add(getToken());
            varDefs.add(varDefBuild());
        }
        semiComma = null;
        if ((index < tokenList.size())&&tokenList.get(index).type.equals("SEMICN"))
            semiComma = getToken();
        else
            error("i", btype.type.lineNum);
        stringList.add("<VarDecl>");
        return new VarDecl(btype, varDefs, commas, semiComma);
    }

    // VarDef → Ident { '[' ConstExp ']' } | Ident { '[' ConstExp ']' } '=' InitVal
    VarDef varDefBuild() {
        Token ident = getToken();
        ArrayList<Token> leftBraces = new ArrayList<>();
        ArrayList<Token> rightBraces = new ArrayList<>();
        ArrayList<ConstExp> constExps = new ArrayList<>();
        while ((index < tokenList.size())&&tokenList.get(index).type.equals("LBRACK")) {
            leftBraces.add(getToken());
            constExps.add(constExpBuild());
            if (tokenList.get(index).type.equals("RBRACK"))
                rightBraces.add(getToken());
            else {
                error("k", leftBraces.get(0).lineNum);
                rightBraces.add(null);
            }
        }
        if ((index < tokenList.size())&&tokenList.get(index).type.equals("ASSIGN")) {
            Token ass = getToken();
            InitVal initVal = initValBuild();
            stringList.add("<VarDef>");
            if (!judgeRename(ident.value))
                error("b", ident.lineNum);
            else addVarAndConst(ident.value, constExps.size(), false);
            return new VarDef(ident, leftBraces, constExps, rightBraces, ass, initVal);
        } else {
            stringList.add("<VarDef>");
            if (!judgeRename(ident.value))
                error("b", ident.lineNum);
            else addVarAndConst(ident.value, constExps.size(), false);
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
        boolean isVoid = tokenList.get(index).type.equals("VOIDTK") ? true : false;

        FuncType type = funcTypeBuild();
        Token ident = getToken();
        Token leftBrace = getToken();
        FuncFParams funcFParams = null;
        if (!tokenList.get(index).type.equals("RPARENT") && !tokenList.get(index).type.equals("LBRACE")) {
            funcFParams = funcFParamsBuild();
        }
        if (funcFParams != null) {
            for (int i = 0; i < funcFParams.funcFParamArray.size(); i++) {
                int dimension = 0;
                FuncFParam funcFParam = funcFParams.funcFParamArray.get(i);
                if (funcFParam.firstLeftBrace != null)
                    dimension++;
                if (funcFParam.secondLeftBrace != null)
                    dimension++;
                this.funcFName.add(funcFParam.Ident.value);
                this.funcFDimension.add(dimension);
                this.funfFLineNum = funcFParam.Ident.lineNum;
            }
        }
        ArrayList<Integer> paramsType = new ArrayList<>();
        if (funcFParams != null) {
            for (int i = 0; i < funcFParams.funcFParamArray.size(); i++) {
                if (funcFParams.funcFParamArray.get(i).firstLeftBrace != null) {
                    if (funcFParams.funcFParamArray.get(i).secondLeftBrace != null)
                        paramsType.add(2);
                    else
                        paramsType.add(1);
                } else paramsType.add(0);
            }
        }
        Token rightBrace = null;
        if (tokenList.get(index).type.equals("RPARENT"))
            rightBrace = getToken();
        else
            error("j", leftBrace.lineNum);
        if (!judgeRename(ident.value))
            error("b", ident.lineNum);
        else
            addFunc(ident.value, isVoid, funcFParams == null ? 0 : funcFParams.funcFParamArray.size(), paramsType);
        Block block = blockBuild();
        stringList.add("<FuncDef>");
        if (isVoid) {
            ArrayList<Integer> errLine = judgeReturnType(block);
            if (errLine.size() != 0) {
                for (int j = 0; j < errLine.size(); j++)
                    error("f", errLine.get(j));
            }
        } else {
            ArrayList<Integer> returnTypeList = block.getReturnBlockItemType();
            if (returnTypeList.size() == 0)
                error("g", tokenList.get(index - 1).lineNum);
        }
        return new FuncDef(type, ident, leftBrace, rightBrace, funcFParams, block);
    }

    //    MainFuncDef → 'int' 'main' '(' ')' Block
    MainFuncDef MainFuncDefBuild() {
        Token intToken = getToken();
        Token mainToken = getToken();
        Token leftBrace = getToken();
        Token rightBrace = null;
        if (tokenList.get(index).type.equals("RPARENT"))
            rightBrace = getToken();
        else
            error("j", leftBrace.lineNum);
        Block block = blockBuild();
        stringList.add("<MainFuncDef>");
        addFunc("main", false, 0, new ArrayList<Integer>());
        ArrayList<Integer> returnTypeList = block.getReturnBlockItemType();
        if (returnTypeList.size() == 0)
            error("g", tokenList.get(index - 1).lineNum);
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
            if (tokenList.get(index).type.equals("RBRACK"))
                firstRightBrace = getToken();
            else
                error("k", firstLeftBrace.lineNum);
            if (tokenList.get(index).type.equals("LBRACK")) {
                secondLeftBrace = getToken();
                constExp = constExpBuild();
                if (tokenList.get(index).type.equals("RBRACK"))
                    secondRightBrace = getToken();
                else
                    error("k", secondLeftBrace.lineNum);
            }
        }
        stringList.add("<FuncFParam>");
        return new FuncFParam(bType, ident, firstLeftBrace, firstRightBrace, secondLeftBrace, secondRightBrace, constExp);
    }

    //  Block → '{' { BlockItem } '}'
    Block blockBuild() {
        SymbolTable table = new SymbolTable(curTable);
        curTable.nextTableList.add(table);
        table.parrentTable = curTable;
        curTable = table;
        if (funcFDimension.size() != 0) {
            for (int i = 0; i < funcFDimension.size(); i++) {
                if (!judgeRename(funcFName.get(i)))
                    error("b", funfFLineNum);
                else addVarAndConst(funcFName.get(i), funcFDimension.get(i), false);
            }
            funcFName.clear();
            funcFDimension.clear();
            funfFLineNum = -1;
        }
        Token leftBrace = getToken();
        ArrayList<BlockItem> blockItems = new ArrayList<>();
        while (!tokenList.get(index).type.equals("RBRACE")) {
            blockItems.add(blockItemBuild());
        }
        Token rightBrace = getToken();
        stringList.add("<Block>");
        curTable = curTable.parrentTable;
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
            Token rightBrace = null;
            if (tokenList.get(index).type.equals("RPARENT"))
                rightBrace = getToken();
            else
                error("j", leftBrace.lineNum);
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
            this.inForNum++;
            Stmt stmt = stmtBuild();
            this.inForNum--;
            stringList.add("<Stmt>");
            return new Stmt(forToken, leftBrace, rightBrace, forStmt1, semiToken1, cond, semiToken2, forStmt2, stmt);
        }
        //'break' ';' | 'continue' ';'
        else if (tokenList.get(index).type.equals("CONTINUETK") || tokenList.get(index).type.equals("BREAKTK")) {
            Token t = getToken();
            if (this.inForNum <= 0)
                error("m", t.lineNum);
            Token semi = null;
            if (index < tokenList.size() && tokenList.get(index).type.equals("SEMICN"))
                semi = getToken();
            else
                error("i", t.lineNum);
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
            Token semi = null;
            if (index < tokenList.size() && tokenList.get(index).type.equals("SEMICN"))
                semi = getToken();
            else
                error("i", returnToken.lineNum);
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
            Token rightBrace = null;
            if (tokenList.get(index).type.equals("RPARENT"))
                rightBrace = getToken();
            else
                error("j", leftBrace.lineNum);
            Token semi = null;
            if (index < tokenList.size() && tokenList.get(index).type.equals("SEMICN"))
                semi = getToken();
            else
                error("i", leftBrace.lineNum);
            if (!judgeFormatString(formatString.value))
                error("a", formatString.lineNum);
            else if (!judgeSameNumInPrint(formatString.value, exps.size()))
                error("l", printf.lineNum);
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
                Token semi = null;
                if (index < tokenList.size() && tokenList.get(index).type.equals("SEMICN"))
                    semi = getToken();
                else {
                    UnaryExp unaryExp = exp.addExp.mulExps.get(0).unaryExps.get(0);
                    if (unaryExp.Ident != null)
                        error("i", unaryExp.Ident.lineNum);
                    else if (unaryExp.unaryOp != null)
                        error("i", unaryExp.unaryOp.Op.lineNum);
                    else {
                        PrimaryExp primaryExp = unaryExp.primaryExp;
                        if (primaryExp.leftBrace != null)
                            error("i", primaryExp.leftBrace.lineNum);
                        else if (primaryExp.lVal != null)
                            error("i", primaryExp.lVal.Ident.lineNum);
                        else
                            error("i", primaryExp.number.intConst.lineNum);
                    }
                }
                stringList.add("<Stmt>");
                stringList.add("<Stmt>");
                return new Stmt(exp, semi);
            } else {
                // LVal '=' Exp ';'
                if (!flag1) {
                    LVal lVal = lValBuild();
                    Token ass = getToken();
                    Exp exp = expBuild();
                    Token semi = null;
                    stringList.add("<Stmt>");
                    if (index < tokenList.size() && tokenList.get(index).type.equals("SEMICN"))
                        semi = getToken();
                    else
                        error("i", lVal.Ident.lineNum);
                    if (!judgeConstChange(lVal))
                        error("h", lVal.Ident.lineNum);

                    return new Stmt(lVal, ass, exp, semi);
                }
                // LVal '=' 'getint''('')'';'
                else {
                    LVal lVal = lValBuild();
                    Token ass = getToken();
                    Token getIntToken = getToken();
                    Token leftBrace = getToken();
                    Token rightBrace = null;
                    if (tokenList.get(index).type.equals("RPARENT"))
                        rightBrace = getToken();
                    else
                        error("i", leftBrace.lineNum);
                    Token semi = null;
                    if (index < tokenList.size() && tokenList.get(index).type.equals("SEMICN"))
                        semi = getToken();
                    else
                        error("i", lVal.Ident.lineNum);
                    stringList.add("<Stmt>");
                    if (!judgeConstChange(lVal))
                        error("h", lVal.Ident.lineNum);
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
        if (!judgeConstChange(lVal))
            error("h", lVal.Ident.lineNum);
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
            if (tokenList.get(index).type.equals("RBRACK"))
                rightBraces.add(getToken());
            else {
                error("k", leftBraces.get(0).lineNum);
                rightBraces.add(null);
            }
        }
        if (tokenList.get(index).type.equals("LBRACK")) {
            leftBraces.add(getToken());
            exps.add(expBuild());
            if (tokenList.get(index).type.equals("RBRACK"))
                rightBraces.add(getToken());
            else {
                error("k", leftBraces.get(1).lineNum);
                rightBraces.add(null);
            }
        }
        stringList.add("<LVal>");
        if (!judgeExistSymbol(ident.value))
            error("c", ident.lineNum);
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
            if (!tokenList.get(index).type.equals("RPARENT") && !tokenList.get(index).type.equals("SEMICN")) {
                funcRParams = funcRParamsBuild();
            }
            Token rightBrace = null;
            if (tokenList.get(index).type.equals("RPARENT"))
                rightBrace = getToken();
            else
                error("j", leftBrace.lineNum);
            stringList.add("<UnaryExp>");
            if (!judgeExistSymbol(ident.value))
                error("c", ident.lineNum);
            else {
                if (!judgeParamsNum(ident.value, funcRParams == null ? 0 : funcRParams.expArray.size()))
                    error("d", ident.lineNum);
                else if (!judgeParamsType(ident.value, funcRParams))
                    error("e", ident.lineNum);
            }
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
        while ((index < tokenList.size())&&(tokenList.get(index).type.equals("MULT") || tokenList.get(index).type.equals("DIV") || tokenList.get(index).type.equals("MOD"))) {
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
        while ((index < tokenList.size())&&(tokenList.get(index).type.equals("PLUS") || tokenList.get(index).type.equals("MINU"))) {
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