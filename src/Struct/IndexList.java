package Struct;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.TreeMap;

public class IndexList {

    public TreeMap<String, ArrayList<Term>> NormalIndex = new TreeMap<String, ArrayList<Term>>();
    public TreeMap<String, ArrayList<PostingIndex>> PostIndex = new TreeMap<String, ArrayList<PostingIndex>>();

    private ArrayList<String> getUnionTerm() {
        ArrayList<String> IndexTermList = new ArrayList<String>();
        for (ArrayList<Term> IndexObj : NormalIndex.values()) {
            for (Term t : IndexObj) {
                if (!IndexTermList.contains(t.Word)) {
                    IndexTermList.add(t.Word);
                }
            }
        }
        return IndexTermList;
    }

    //get term-posting from document-termlist
    public TreeMap<String, ArrayList<PostingIndex>> getInvertedIndexList() {
        TreeMap<String, ArrayList<PostingIndex>> InvertedIndexList = new TreeMap<String, ArrayList<PostingIndex>>();
        //initialize
        for (String Word : getUnionTerm()) {
            ArrayList<PostingIndex> Posting = new ArrayList<PostingIndex>();
            InvertedIndexList.put(Word, Posting);
        }
        //initialize
        for (Entry<String, ArrayList<Term>> NIItem : NormalIndex.entrySet()) {
            for (Term t : NIItem.getValue()) {
                PostingIndex PI = new PostingIndex(t.Word, NIItem.getKey(), t.Frequency, t.StartIndex, t.DocumentLength);
                InvertedIndexList.get(t.Word).add(PI);
            }
        }
        return InvertedIndexList;
    }
}
