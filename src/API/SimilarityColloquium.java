package API;

import DB.DB;
import DB.ExtractData;
import Sim.SimScore;
import Struct.Term;
import Struct.WordTree;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class SimilarityColloquium {
    //User Recommend

    public static void main(String[] args) throws Exception {
        WordTree.intitializeWordTree();

        TreeMap<Integer, ArrayList<Term>> user_vsml = ExtractData.extractColloquium();
        TreeMap<Integer, ArrayList<Term>> user_vsmr = ExtractData.extractColloquium();
        for (Map.Entry<Integer, ArrayList<Term>> iteml : user_vsml.entrySet()) {
            int coll = iteml.getKey();
            ArrayList<Term> userlvsm = iteml.getValue();
            for (Map.Entry<Integer, ArrayList<Term>> itemr : user_vsmr.entrySet()) {
                int colr = itemr.getKey();
                ArrayList<Term> userrvsm = itemr.getValue();
                if (coll != colr && iteml.getValue().size() > 0 && itemr.getValue().size() > 0) {
                    double score = new SimScore(userlvsm, userrvsm).getVSMScore();
                    DB db = new DB();
                    if (Double.isNaN(score) || Double.isInfinite(score) || score == 0/*
                             * ||score==0
                             */) {
                    } else {
                        db.executeInsert("insert into ColloquiumGroup(col_idl,col_idr,score) values(" + coll + "," + colr + "," + score + ")");
                        System.out.println(score);
                    }
                    db.close();
                }
            }
        }
    }
}
