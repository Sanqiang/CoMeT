package Struct;

import API.config;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class WordTree {

    public static WordTree Dic = null;
    public static WordTree NoiseEn = null;
    public static WordTree NoiseCn = null;

    public WordTree() {
        this.CharArr = new ArrayList<String>();
        this.nextTree = new ArrayList<WordTree>();
        this.IsEnd = false;
        this.Char = "empty";
    }
    public ArrayList<String> CharArr;
    public ArrayList<WordTree> nextTree;
    public boolean IsEnd;
    public int WordID = 0;
    public String Char;
    //static OP 
    public static int RecordId = 0;

    public static void createWord(String word, WordTree wt) {
        if (0 == word.length() && wt.IsEnd) {
            wt.WordID = WordTree.RecordId++;
            return;
        }
        String CurrentChar = word.substring(0, 1);
        String RemainingWord = word.substring(1, word.length());
        int index;
        boolean IsEndFlag = false;
        if (-1 == (index = wt.CharArr.indexOf(CurrentChar))) {
            String NextChar = CurrentChar;
            WordTree NextWT = new WordTree();
            wt.CharArr.add(NextChar);
            if (RemainingWord.length() == 0) {
                IsEndFlag = true;
            }
            NextWT.Char = CurrentChar;
            NextWT.IsEnd = IsEndFlag;
            wt.nextTree.add(NextWT);
            createWord(RemainingWord, NextWT);
        } else {
            //String NextChar=wt.CharArr.get(index);
            WordTree NextWT = wt.nextTree.get(index);
            if (RemainingWord.length() == 0) {
                IsEndFlag = true;
            }
            NextWT.Char = CurrentChar;
            if (NextWT.IsEnd == false) {
                NextWT.IsEnd = IsEndFlag;
            }
            createWord(RemainingWord, NextWT);
        }
    }

    public static WordTree createDic(String path) {
        WordTree WTOutput = new WordTree();
        try {
            File f = new File(path);
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            while (null != (line = br.readLine())) {
                createWord(line, WTOutput);
            }
            br.close();
            RecordId = 0;
            return WTOutput;
        } catch (Exception e) {
            return null;
        }
    }

    public static WordTree IsContain(String keyword, WordTree dic) {
        if (dic.IsEnd && keyword.length() == 0) {
            return dic;
        }
        try {
            String CurrentChar = keyword.substring(0, 1);
            int index;
            if (-1 != (index = dic.CharArr.indexOf(CurrentChar))) {
                dic = dic.nextTree.get(index);
                keyword = keyword.substring(1);
                return IsContain(keyword, dic);
            } else {
                return null;
            }
        } catch (Exception e) {
            //System.out.println("Err:"+dic.IsEnd);
            return null;
        }
    }

    public static void intitializeWordTree() {
        WordTree.Dic = WordTree.createDic(config.DicPath);
        WordTree.NoiseEn = WordTree.createDic(config.NoiseEnPath);
        WordTree.NoiseCn = WordTree.createDic(config.NoiseCnPath);
    }
}
