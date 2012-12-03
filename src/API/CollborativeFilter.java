/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package API;

import DB.DB;
import DB.ExtractData;
import Sim.SimScore;
import Struct.Term;
import Struct.WordTree;
import index.SplitWord;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class CollborativeFilter {
    //User Recommend
    public static void main(String[] args) throws Exception {
        WordTree.intitializeWordTree();

        TreeMap<Integer, ArrayList<Term>> user_vsml = ExtractData.extractUser();
        TreeMap<Integer, ArrayList<Term>> user_vsmr = ExtractData.extractUser();
        for (Map.Entry<Integer, ArrayList<Term>> iteml : user_vsml.entrySet()) {
            int userl = iteml.getKey();
            ArrayList<Term> userlvsm = iteml.getValue();
            for (Map.Entry<Integer, ArrayList<Term>> itemr : user_vsmr.entrySet()) {
                int userr = itemr.getKey();
                ArrayList<Term> userrvsm = itemr.getValue();
                if (userl != userr && iteml.getValue().size() > 0 && itemr.getValue().size() > 0) {
                    double score = new SimScore(userlvsm, userrvsm).getVSMScore();
                    DB db = new DB();
                    if (Double.isNaN(score) || Double.isInfinite(score) || score == 0/*
                             * ||score==0
                             */) {
                    } else {
                        db.executeInsert("insert into UserGroup(user_idl,user_idr,score) values(" + userl + "," + userr + "," + score + ")");
                        System.out.println(score);
                    }
                    db.close();
                }
            }
        }
    }
}
