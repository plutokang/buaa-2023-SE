package Node;

import Main.Token;

import java.util.ArrayList;

public class Block {
    public Token leftBrace;
    public Token rightBrace;
    public ArrayList<BlockItem> BlockItemArray;

    public Block(Token leftBrace, Token rightBrace, ArrayList<BlockItem> BlockItemArray) {
        this.leftBrace = leftBrace;
        this.rightBrace = rightBrace;
        this.BlockItemArray = BlockItemArray;
    }
}
