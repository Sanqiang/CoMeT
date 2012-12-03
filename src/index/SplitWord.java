package index;

import Struct.Chunk;
import Struct.WordTree;
import Struct.Term;
import Struct.TermList;
import java.util.ArrayList;

public class SplitWord {
    //SplitWordArrayList is output Split Word ArrayList

    public TermList SplitWordArrayList;
    public ArrayList<Chunk> ChunkArrayList;
    private int QuerStringLength;

    //contructor just initialize that arraylist with empty arraylist and given dictionary
    public SplitWord() throws Exception {
        this.SplitWordArrayList = new TermList();
        this.ChunkArrayList = new ArrayList<Chunk>();
    }

    //initialize the split word
    public void initialize(String QuerString) {
        QuerStringLength = QuerString.length();
        getChunk(new Chunk(QuerString, 0));
        for (Chunk Chunk : ChunkArrayList) {
            //System.out.println("Chunk:" + Chunk.ChunkString + ":" + Chunk.StartIndex);
            if (getType(Chunk.ChunkString.charAt(0)) == 4) {
                getSplitChineseWord(Chunk);
            } else if (getType(Chunk.ChunkString.charAt(0)) == 1) {
                getSplitEnglishWord(Chunk);
            } else if (getType(Chunk.ChunkString.charAt(0)) == 2) {
                this.SplitWordArrayList.put(Chunk.ChunkString, new Term(Chunk.ChunkString, Chunk.StartIndex, QuerStringLength));
            }
        }
    }
    //lexical analysis
    //split into different chunk 

    public void getChunk(Chunk ChunkItem) {
        int CurrentType = getType(ChunkItem.ChunkString.charAt(0));
        int StartIndex = 0, EndIndex = 0;
        for (int i = 1; i < ChunkItem.ChunkString.length(); i++) {
            EndIndex = i;
            int CurrentTypeLoop = getType(ChunkItem.ChunkString.charAt(i));
            if (CurrentTypeLoop != CurrentType) {
                CurrentType = getType(ChunkItem.ChunkString.charAt(i));
                if (ChunkItem.ChunkString.substring(StartIndex, EndIndex).trim().length() >= 2) {
                    ChunkArrayList.add(new Chunk(ChunkItem.ChunkString.substring(StartIndex, EndIndex), StartIndex));
                }
                StartIndex = i;
            }
        }
        ChunkArrayList.add(new Chunk(ChunkItem.ChunkString.substring(StartIndex), StartIndex));
    }
    //generate the split word

    public void getSplitChineseWord(Chunk QueryChunk) {
        //generate from the end to the start
        for (int i = QueryChunk.ChunkString.length() - 1; i >= 0; i--) {
            String InternalWord = QueryChunk.ChunkString.substring(i);
            if (WordTree.IsContain(InternalWord, WordTree.NoiseCn) != null) {
                //if find then end and recusive new getSplitWord
                getSplitChineseWord(new Chunk(QueryChunk.ChunkString.substring(0, i), i));
                return;

            } else if (WordTree.IsContain(InternalWord, WordTree.Dic) != null) {
                Term t = new Term(InternalWord, i + QueryChunk.StartIndex, QuerStringLength);
                SplitWordArrayList.put(InternalWord, t);
                //if find then end and recusive new getSplitWord
                getSplitChineseWord(new Chunk(QueryChunk.ChunkString.substring(0, i), i));
                return;
            }
        }
        if (QueryChunk.ChunkString.length() == 0) {
            return;
        }
        //if there is no find, we just cut one character from the end and start new getSplitWord
        getSplitChineseWord(new Chunk(QueryChunk.ChunkString.substring(0, QueryChunk.ChunkString.length() - 1), QueryChunk.StartIndex - 1));
    }

    public void getSplitEnglishWord(Chunk QueryChunk) {
        for (int i = QueryChunk.ChunkString.length() - 1; i >= 0; i--) {
            if (i < 1) {
                break;
            }
            if (QueryChunk.ChunkString.substring(i - 1, i).equals(" ")) {
                WordTree wt = null;
                if ((wt = WordTree.IsContain(QueryChunk.ChunkString.substring(i).toLowerCase(), WordTree.NoiseEn)) == null) {
                    String KeyWord = Stemmer.getResult(QueryChunk.ChunkString.substring(i));
                    SplitWordArrayList.put(KeyWord, new Term(KeyWord, i, QuerStringLength));
                }
            }
        }
        WordTree wt = null;
        if ((wt = WordTree.IsContain(QueryChunk.ChunkString.toLowerCase(), WordTree.NoiseEn)) == null) {
            String KeyWord = Stemmer.getResult(QueryChunk.ChunkString);
            SplitWordArrayList.put(KeyWord, new Term(KeyWord, QueryChunk.StartIndex, QuerStringLength));
        }
    }
    //return 1 English word
    //return 1(2) Number
    //return 3 Sign
    //return 4 Other(Chinese)

    public int getType(char singleChar) {
        if ((singleChar >= 65 && singleChar <= 90) || (singleChar >= 97 && singleChar <= 122)) {
            return 1;
        } else if (singleChar >= 48 && singleChar <= 57) {
            return 1;
        } else if (singleChar <= 126) {
            return 3;
        } else {
            return 4;
        }
    }

    public ArrayList<Term> getUnSortedList() {
        return SplitWordArrayList.UnSort();
    }

    public ArrayList<Term> getSortedList() {
        return SplitWordArrayList.Sort();
    }

    //ShortCut for static method
    public static ArrayList<Term> Result(String src) throws Exception {
        SplitWord sw = new SplitWord();
        sw.initialize(src);
        return sw.getUnSortedList();
    }
}
