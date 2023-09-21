import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class MyLexer {
    private HashMap<String, String> reservedWord = new HashMap<>();
    private HashMap<String, String> delimiter = new HashMap<>();
    private ArrayList<Token> tokenList = new ArrayList<>();
    private boolean isAnnotation = false;

    public MyLexer() throws IOException {
        buildReservedWord();
        buildDelimiter();
        String fileName = "testfile.txt";
        BufferedReader stream = openFile(fileName);
        String line;
        int rowNum = 0;
        while ((line = stream.readLine()) != null) {
            analyzeLine(line, rowNum);
            rowNum++;
        }
        printToken();
    }

    BufferedReader openFile(String fileName) {
        BufferedReader stream = null;
        try {
            stream = new BufferedReader(new FileReader(fileName));
        } catch (FileNotFoundException e) {
            System.out.println("Error: could not open " + fileName);
        }
        return stream;
    }

    void buildReservedWord() {
        this.reservedWord.put("main", "MAINTK");
        this.reservedWord.put("const", "CONSTTK");
        this.reservedWord.put("int", "INTTK");
        this.reservedWord.put("break", "BREAKTK");
        this.reservedWord.put("continue", "CONTINUETK");
        this.reservedWord.put("if", "IFTK");
        this.reservedWord.put("else", "ELSETK");
        this.reservedWord.put("for", "FORTK");
        this.reservedWord.put("getint", "GETINTTK");
        this.reservedWord.put("printf", "PRINTFTK");
        this.reservedWord.put("return", "RETURNTK");
        this.reservedWord.put("void", "VOIDTK");
    }

    void buildDelimiter() {
        this.delimiter.put("!", "NOT");
        this.delimiter.put("&&", "AND");
        this.delimiter.put("||", "OR");
        this.delimiter.put("+", "PLUS");
        this.delimiter.put("*", "MULT");
        this.delimiter.put("/", "DIV");
        this.delimiter.put("%", "MOD");
        this.delimiter.put("-", "MINU");
        this.delimiter.put("<", "LSS");
        this.delimiter.put(">", "GRE");
        this.delimiter.put("<=", "LEQ");
        this.delimiter.put(">=", "GEQ");
        this.delimiter.put("==", "EQL");
        this.delimiter.put("!=", "NEQ");
        this.delimiter.put("=", "ASSIGN");
        this.delimiter.put(";", "SEMICN");
        this.delimiter.put("(", "LPARENT");
        this.delimiter.put(")", "RPARENT");
        this.delimiter.put(",", "COMMA");
        this.delimiter.put("[", "LBRACK");
        this.delimiter.put("]", "RBRACK");
        this.delimiter.put("{", "LBRACE");
        this.delimiter.put("}", "RBRACE");
        this.delimiter.put(" ", "SPACE");
        this.delimiter.put("&", "ONEAND");
        this.delimiter.put("|", "ONEOR");
        this.delimiter.put("\t", "TAB");
    }

    void analyzeLine(String line, int lineNum) {
        int pos = 0;
        for (pos = 0; pos < line.length(); pos++) {
            if(this.isAnnotation == true && line.charAt(pos) == '*' && pos + 1 < line.length() &&line.charAt(pos + 1) == '/')
            {
                this.isAnnotation = false;
                pos += 2;
                if(pos >= line.length())
                    return;;
            }
            if(this.isAnnotation == true)
            {
                continue;
            }
            if(line.charAt(pos) == '/' && pos +1 < line.length() && line.charAt(pos + 1) == '*')
            {
                this.isAnnotation = true;
                continue;
            }
            if(line.charAt(pos) == '/' && pos +1 < line.length() && line.charAt(pos + 1) == '/')
            {
                return;
            }
            String word = "";
            if (line.charAt(pos) == '"') {
                word += String.valueOf(line.charAt(pos));
                pos++;
                while (pos < line.length() && line.charAt(pos) != '"') {
                    word += String.valueOf(line.charAt(pos));
                    pos++;
                }
                word += line.charAt(pos);
                Token temp = new Token(lineNum, word, "STRCON");
                this.tokenList.add(temp);
                continue;
            }
            String type = isDelimiter(line.charAt(pos));
            if (type == null) {
                while (pos < line.length() && isDelimiter(line.charAt(pos)) == null) {
                    word += String.valueOf(line.charAt(pos));
                    pos++;
                }
                analyzeWord(word, lineNum);
                pos--;
                continue;
            } else if (!type.equals("SPACE") && !type.equals("TAB")) {
                word = "";
                word += String.valueOf(line.charAt(pos));
                if(pos + 1 < line.length())
                {
                    String temp = isDelimiter(line.charAt(pos + 1));
                    if(type.equals("ONEAND") && temp != null && temp.equals("ONEAND"))
                    {
                        word += String.valueOf(line.charAt(pos + 1));
                        type = "AND";
                        pos++;
                    }
                    if(type.equals("ONEOR") && temp != null && temp.equals("ONEOR"))
                    {
                        word += String.valueOf(line.charAt(pos + 1));
                        type = "OR";
                        pos++;
                    }
                    if(type.equals("LSS") && temp != null && temp.equals("ASSIGN"))
                    {
                        word += String.valueOf(line.charAt(pos + 1));
                        type = "LEQ";
                        pos++;
                    }
                    if(type.equals("GRE") && temp != null && temp.equals("ASSIGN"))
                    {
                        word += String.valueOf(line.charAt(pos + 1));
                        type = "GEQ";
                        pos++;
                    }
                    if(type.equals("ASSIGN") && temp != null && temp.equals("ASSIGN"))
                    {
                        word += String.valueOf(line.charAt(pos + 1));
                        type = "EQL";
                        pos++;
                    }
                    if(type.equals("NOT") && temp != null && temp.equals("ASSIGN"))
                    {
                        word += String.valueOf(line.charAt(pos + 1));
                        type = "NEQ";
                        pos++;
                    }
                }
                Token temp = new Token(lineNum,word, type);
                this.tokenList.add(temp);
            }
        }
    }

    void printToken() {
        String filePath = "output.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (int i = 0; i < this.tokenList.size(); i++) {
                String line = this.tokenList.get(i).type + " " + this.tokenList.get(i).value;
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    String isDelimiter(char signal) {
        String type = this.delimiter.get(String.valueOf(signal));
        return type;
    }

    String isReservedWord(String word) {
        String type = this.reservedWord.get(word);
        return type;
    }

    void analyzeWord(String word, int lineNum) {
        if (word.charAt(0) >= '0' && word.charAt(0) <= '9') {
            this.tokenList.add(new Token(lineNum, word, "INTCON"));
            return;
        }
        String type = (isReservedWord(word) == null) ? "IDENFR" : isReservedWord(word);
        this.tokenList.add(new Token(lineNum, word, type));
    }
}
