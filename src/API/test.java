package API;

import DB.DB;
import Retrieval.StdRetrieval;
import Sim.SimScore;
import Struct.Term;
import Struct.TermList;
import Struct.WordTree;
import com.mysql.jdbc.ResultSet;
import edu.pitt.sis.ImageEx.ImageItem;
import edu.pitt.sis.ImageEx.ImageRank;
import index.SplitWord;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class test {

    public static void main(String[] args) throws Exception {
        ArrayList<String> list = new ArrayList<String>();
        DB db = new DB();
        ResultSet rs = (ResultSet) db.getResultSet("select * from speaker where picurl is not null and picurl <> 'http://halley.exp.sis.pitt.edu/comet/images/speaker/avartar.gif'");
        while (rs.next()) {
            String url = rs.getString("picurl");
            list.add(url);
        }
        URL u = new URL("http://t2.gstatic.com/images?q=tbn:ANd9GcTSjh7L8bI6Qhqczdyy8G3lFRenJGfbk7tymrqDoAlpldChw_p7CKggeOo");
        ImageRank ir = new ImageRank(u.openStream(), list);
        ArrayList<ImageItem> result = ir.getRankedPicUrl();
        for (ImageItem it : result) {
            System.out.println("<img src='"+it.URL + "' /> " + it.score);
        }
    }
}
