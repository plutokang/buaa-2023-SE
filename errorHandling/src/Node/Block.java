package Node;

import Main.Token;

import java.util.ArrayList;

public class Block {
    Token leftBrace;
    Token rightBrace;
    ArrayList<BlockItem> BlockItemArray;

    public Block(Token leftBrace, Token rightBrace, ArrayList<BlockItem> BlockItemArray) {
        this.leftBrace = leftBrace;
        this.rightBrace = rightBrace;
        this.BlockItemArray = BlockItemArray;
    }
    public ArrayList<Integer> getReturnBlockItemType()
    {
        ArrayList<Integer>temp = new ArrayList<>();
        for(int i = 0;i < this.BlockItemArray.size();i++)
        {
            if(this.BlockItemArray.get(i).stmt != null && this.BlockItemArray.get(i).stmt.returnToken != null)
            {
                if(this.BlockItemArray.get(i).stmt.exp != null)
                    temp.add(1);
                else
                    temp.add(0);
            }
        }
        return temp;
    }
    public ArrayList<Integer> getReturnBlockItemLine()
    {
        ArrayList<Integer>temp = new ArrayList<>();
        for(int i = 0;i < this.BlockItemArray.size();i++)
        {
            if(this.BlockItemArray.get(i).stmt != null && this.BlockItemArray.get(i).stmt.returnToken != null)
            {
                temp.add(this.BlockItemArray.get(i).stmt.returnToken.lineNum);
            }
        }
        return temp;
    }
}
