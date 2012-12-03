package API;

import DB.DB;
import DB.ExtractData;
import Sim.SimScore;
import Struct.Term;
import Struct.WordTree;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class Rec_Speaker {
    
    public static void main(String[] args) throws Exception {
        WordTree.intitializeWordTree();
        
        TreeMap<Integer, ArrayList<Term>> speaker_vsm = ExtractData.extractColloquiumBaseOnSpeaker();
        TreeMap<Integer, ArrayList<Term>> user_vsm = ExtractData.extractUserBaseOnUserGroup(3);
        
        for (Map.Entry<Integer, ArrayList<Term>> itemuser : user_vsm.entrySet()) {
            for (Map.Entry<Integer, ArrayList<Term>> itemspeaker : speaker_vsm.entrySet()) {
                int user_id = itemuser.getKey();
                int speaker_id = itemspeaker.getKey();
                ArrayList<Term> user_vsml = itemuser.getValue();
                ArrayList<Term> speaker_vsml = itemspeaker.getValue();
                if (user_vsml.size() > 0 && speaker_vsml.size() > 0) {
                    double score = new SimScore(user_vsml, speaker_vsml).getVSMScore();
                                        DB db = new DB();
                    if (Double.isNaN(score) || Double.isInfinite(score)||score==0) {
                    } else {
                        db.executeInsert("insert into rec_speaker(speaker_id,user_id,score) values(" + speaker_id + "," + user_id + "," + score + ")");
                        System.out.println(score);
                        System.out.println(score);
                    }
                    db.close();
                }
            }
        }
        
    }
}
