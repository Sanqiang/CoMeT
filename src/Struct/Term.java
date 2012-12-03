package Struct;

import java.io.Serializable;

public class Term implements Serializable{
    public String Word;
    public int Frequency;
    public int StartIndex;
    public int DocumentLength;

    public Term(String Word, int StartIndex,int DocumentLength) {
        this.Word = Word;
        this.Frequency = 1;
        this.StartIndex = StartIndex;
        this.DocumentLength=DocumentLength;
    }
}
