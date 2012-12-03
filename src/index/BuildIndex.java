/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package index;

import API.config;
import Struct.WordTree;
import Struct.Term;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BuildIndex {

    WordTree TermTree;

    public void BuildIndexTerm(ArrayList<Term> income) {
        try {
            TermTree = WordTree.createDic(config.IndexTermPath);
            FileWriter fwTerm = new FileWriter(config.IndexTermPath, true);
            for (Term t : income) {
                if (WordTree.IsContain(t.Word, TermTree) == null) {
                    fwTerm.write(t.Word);
                    fwTerm.write("\r\n");
                }
            }
            fwTerm.close();
        } catch (Exception ex) {
            Logger.getLogger(BuildIndex.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void BuildIndexPost(ArrayList<Term> income, String DocumentID) throws FileNotFoundException, IOException, Exception {
        //initialize the Post file
        BufferedReader br = new BufferedReader(new FileReader(config.IndexTermPath));
        String line = null;
        int TCount = 0;
        while ((line = br.readLine()) != null) {
            TCount++;
        }
        br.close();
        BufferedReader br2 = new BufferedReader(new FileReader(config.IndexPostPath));
        int PCount = 0;
        while ((line = br2.readLine()) != null) {
            PCount++;
        }
        br2.close();
        int IncreaseLineCount = TCount - PCount;
        FileWriter fw = new FileWriter(config.IndexPostPath, true);
        for (int i = 0; i < IncreaseLineCount; i++) {
            for (int j = 0; j < 1000; j++) {
                fw.write(" ");
            }
            fw.write("-\r\n");
        }
        fw.flush();
        fw.close();
        //create the post list

        TermTree = WordTree.createDic(config.IndexTermPath);
        ArrayList<Integer> ChangeLine = new ArrayList<Integer>();
        ArrayList<Integer> CorrespondFrequency = new ArrayList<Integer>();
        ArrayList<Integer> CorrespondIndex = new ArrayList<Integer>();
        for (Term t : income) {
            WordTree wt;
            if ((wt = WordTree.IsContain(t.Word, TermTree)) != null) {
                ChangeLine.add(wt.WordID);
                CorrespondFrequency.add(t.Frequency);
                CorrespondIndex.add(t.StartIndex);
            }
        }
        RandomAccessFile ReadRandom = new RandomAccessFile(config.IndexPostPath, "r");
        int CurrentCount = 0;
        while ((line = ReadRandom.readLine()) != null) {

            int InsideIndex;
            if (-1 != (InsideIndex = ChangeLine.indexOf(++CurrentCount))) {
                long postion = ReadRandom.getFilePointer();
                byte x = ' ';
                while ((x = ReadRandom.readByte()) != 32) {
                    postion = ReadRandom.getFilePointer();
                }
                RandomAccessFile WriteRandom = new RandomAccessFile(config.IndexPostPath, "rw");
                WriteRandom.seek(postion);
                String InsertLineItem = DocumentID + ":" + CorrespondFrequency.get(InsideIndex) + ":" + CorrespondIndex.get(InsideIndex) + "|";
                WriteRandom.writeBytes(InsertLineItem);
            }
        }
    }
}
