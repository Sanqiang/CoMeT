package API;

import DB.DB;
import DB.ExtractData;
import Sim.SimScore;
import Struct.SpeakerVectorSpace;
import Struct.Term;
import Struct.WordTree;
import index.SplitWord;
import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class Run {

    public static void main(String[] args) throws Exception {
        WordTree.intitializeWordTree();
        ArrayList<Term> TestData = SplitWord.Result("Machine Learning computer information");
//        DB db = new DB();
//        db.getResultSet("select * from speaker");
//        ArrayList<Term> r1 = SplitWord.Result("Zhao San qiang");
//        ArrayList<Term> r2 = SplitWord.Result("Zhao San qiang is pig");
//        SimScore ss = new SimScore(r1, r2);
//        System.out.println(ss.getVSMScore());

//        SpeakerVectorSpace svsm =  new ExtractData().writeSPeakerVS();
//        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("D:\\workspace\\train\\speakervsm.txt"));
//        oos.writeObject(svsm);

//        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("D:\\workspace\\train\\speakervsm.txt"));
//        SpeakerVectorSpace svsm2 = (SpeakerVectorSpace) ois.readObject();
//        for (Map.Entry<Integer,ArrayList<Term>> speaker : svsm2.entrySet()) {
//            ArrayList<Term> TrainData = speaker.getValue();
//            System.out.println(new SimScore(TestData, TrainData).getVSMScore());
//        }

        //User Recommend
//        TreeMap<Integer, ArrayList<Term>> user_vsml = (TreeMap<Integer, ArrayList<Term>>) new ExtractData().extractUser().clone();
//        TreeMap<Integer, ArrayList<Term>> user_vsmr = (TreeMap<Integer, ArrayList<Term>>) new ExtractData().extractUser().clone();
//
//        for (Map.Entry<Integer, ArrayList<Term>> iteml : user_vsml.entrySet()) {
//            int userl = iteml.getKey();
//            ArrayList<Term> userlvsm = iteml.getValue();
//            for (Map.Entry<Integer, ArrayList<Term>> itemr : user_vsmr.entrySet()) {
//                int userr = itemr.getKey();
//                ArrayList<Term> userrvsm = itemr.getValue();
//                if (userl != userr) {
//                    double score = new SimScore(userlvsm, userrvsm).getVSMScore();
//                    DB db = new DB();
//                    if (Double.isNaN(score) || Double.isInfinite(score)/*||score==0*/) {
//                    } else {
//                        db.executeInsert("insert into UserGroup values(" + userl + "," + userr + "," + score + ")");
//                    }
//                    db.close();
//                }
//            }
//        }
    }
}
