package DB;

import Retrieval.StdRetrieval;
import Struct.SpeakerVectorSpace;
import Struct.Term;
import index.SplitWord;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExtractData {

    String path;

    public ExtractData() {
    }

    //Get user interest data
    public static TreeMap<Integer, ArrayList<Term>> extractUser() {
        TreeMap<Integer, ArrayList<Term>> vsml = new TreeMap<Integer, ArrayList<Term>>();
        try {
            DB db = new DB();
            ResultSet rs = db.getResultSet("SELECT distinct user_id from userprofile");
            while (rs.next()) {
                int user_id = rs.getInt("user_id");

                //Bookmark
                DB db2 = new DB();
                ResultSet rs2 = db2.getResultSet("select userprofile_id,usertags,comment from userprofile where user_id=" + user_id);
                String usertags = "";
                String comment = "";
                while (rs2.next()) {
                    usertags += rs2.getString("usertags");
                    comment += rs2.getString("comment");
                }
                db2.close();
                //Note
                DB db3 = new DB();
                ResultSet rs3 = db3.getResultSet("select usernote from usernote where user_id=" + user_id);
                String usernote = "";
                while (rs3.next()) {
                    usernote += rs3.getString("usernote");
                }
                db3.close();
                //tags
                DB db4 = new DB();
                ResultSet rs4 = db4.getResultSet("select tag from tag where tag_id in (select tag_id from tags where user_id=" + user_id + ")");
                String tag = "";
                while (rs4.next()) {
                    tag += rs4.getString("tag");
                }
                db4.close();
                //comment keyword
                DB db5 = new DB();
                ResultSet rs5 = db5.getResultSet("select keyword,comment from keywordcomment where user_id=" + user_id);
                String keyword = "";
                String commentk = "";
                while (rs5.next()) {
                    keyword += rs5.getString("keyword");
                    commentk += rs5.getString("comment");
                }
                db5.close();
                //outerlink
                DB db6 = new DB();
                ResultSet rs6 = db6.getResultSet("select innertext from userouterlink where user_id=" + user_id);
                String innertext = "";
                while (rs6.next()) {
                    innertext += rs6.getString("innertext");
                }
                db6.close();
                //progress-base
                DB db7 = new DB();
                ResultSet rs7 = db7.getResultSet("select detail,title from colloquium where col_id in (select col_id from rec_progressbase where user_id= " + user_id + " order by score )  limit 1,3");
                String title = "";
                String detail = "";
                while (rs7.next()) {
                    title += rs7.getString("title");
                    detail += rs7.getString("detail");
                }
                db7.close();
                //community
                DB db8 = new DB();
                ResultSet rs8 = db8.getResultSet("select comm_name, comm_desc from community where comm_id in (select comm_id from contribute where user_id=" + user_id + " ) ");
                String comm_name = "";
                String comm_desc = "";
                while (rs8.next()) {
                    comm_name += rs8.getString("comm_name");
                    comm_desc += rs8.getString("comm_desc");
                }
                db8.close();
                //series
                DB db9 = new DB();
                ResultSet rs9 = db9.getResultSet("select name,description from series where user_id=" + user_id);
                String sname = "";
                String sdesc = "";
                while (rs9.next()) {
                    sname += rs9.getString("name");
                    sdesc += rs9.getString("description");
                }
                db9.close();
                //extra comment
                DB db10 = new DB();
                ResultSet rs10 = db10.getResultSet("select comment from comment where user_id= " + user_id);
                String commentx = "";
                while (rs10.next()) {
                    commentx += rs10.getString("comment");
                }
                db10.close();

                String Data = usertags + " " + comment + " " + usernote + " " + tag + " " + " " + keyword + " " + commentk + " " + title + " " + detail + " " + innertext + " " + comm_name + " " + comm_desc + " " + sname + " " + sdesc + " " + commentx;
                Data = StdRetrieval.getCleanData(Data);
                ArrayList<Term> vsm = SplitWord.Result(Data);
                vsml.put(user_id, vsm);
            }

        } catch (Exception ex) {
            Logger.getLogger(ExtractData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return vsml;
    }

    //Get user interest data
    public static TreeMap<Integer, ArrayList<Term>> extractUserBaseOnUserGroup(int rank) {
        TreeMap<Integer, ArrayList<Term>> vsml = new TreeMap<Integer, ArrayList<Term>>();
        try {

            DB db = new DB();
            ResultSet rs = db.getResultSet("SELECT distinct user_id from userprofile");
            while (rs.next()) {
                int user_id = rs.getInt("user_id");
                String Data = getDataByUser(user_id);

                //Collaborative filter
                DB db0 = new DB();
                ResultSet rs0 = db0.getResultSet("SELECT user_idr from usergroup where user_idl=" + user_id + " order by score limit 1," + rank);
                while (rs0.next()) {
                    Data += getDataByUser(rs0.getInt("user_idr"));
                }
                //Collaborative filter

                Data = StdRetrieval.getCleanData(Data);

                ArrayList<Term> vsm = SplitWord.Result(Data);
                if (vsm != null && vsm.size() > 0) {
                    vsml.put(user_id, vsm);
                    System.out.println(user_id);
                }



            }

        } catch (Exception ex) {
            Logger.getLogger(ExtractData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return vsml;
    }

    //Get colloquium data
    public static TreeMap<Integer, ArrayList<Term>> extractColloquium() {
        TreeMap<Integer, ArrayList<Term>> vsml = new TreeMap<Integer, ArrayList<Term>>();
        try {
            DB db = new DB();
            ResultSet rs = db.getResultSet("SELECT distinct col_id from colloquium");
            while (rs.next()) {
                int col_id = rs.getInt("col_id");

                //detail title
                DB db2 = new DB();
                ResultSet rs2 = db2.getResultSet("select title,detail from colloquium where col_id=" + col_id);
                String title = "";
                String detail = "";
                while (rs2.next()) {
                    title += rs2.getString("title");
                    detail += rs2.getString("detail");
                }
                db2.close();
                //term
                DB db3 = new DB();
                ResultSet rs3 = db3.getResultSet("select term from colterm where col_id=" + col_id);
                String term = "";
                while (rs3.next()) {
                    term += rs3.getString("term");
                }
                db3.close();
                //comment
                DB db4 = new DB();
                ResultSet rs4 = db4.getResultSet("select comment from comment where comment_id in (select comment_id from comment_col where col_id=" + col_id + ")");
                String comment = "";
                while (rs4.next()) {
                    comment += rs4.getString("comment");
                }
                db4.close();
                //entity
                /*
                 * DB db5 = new DB(); ResultSet rs5 = db5.getResultSet("select
                 * entity from entity where entity_id in (select entity_id from
                 * entities where col_id=" + col_id + " ) "); String entity =
                 * ""; while (rs5.next()) { entity += rs5.getString("entity"); }
                db5.close();
                 */
                //community
                DB db6 = new DB();
                ResultSet rs6 = db6.getResultSet("select comm_name, comm_desc from community where comm_id in (select comm_id from contribute where col_id=" + col_id + " ) ");
                String comm_name = "";
                String comm_desc = "";
                while (rs6.next()) {
                    comm_name += rs6.getString("comm_name");
                    comm_desc += rs6.getString("comm_desc");
                }
                db6.close();
                //series
                DB db7 = new DB();
                ResultSet rs7 = db7.getResultSet("select name,description from series where series_id in (select series_id from seriescol where col_id= " + col_id + " ) ");
                String sname = "";
                String sdesc = "";
                while (rs7.next()) {
                    sname += rs7.getString("name");
                    sdesc += rs7.getString("description");
                }
                db7.close();
                //affilicate
                DB db8 = new DB();
                ResultSet rs8 = db8.getResultSet("select affiliate from affiliate where affiliate_id in (select affiliate_id from affiliate_col where col_id= " + col_id + " ) ");
                String affilicate = "";
                while (rs8.next()) {
                    affilicate += rs8.getString("affiliate");
                }
                db8.close();
                //host
                DB db9 = new DB();
                ResultSet rs9 = db9.getResultSet("select host from host where host_id in (select host_id from colloquium where col_id= " + col_id + " ) ");
                String host = "";
                while (rs9.next()) {
                    host += rs9.getString("host");
                }
                db9.close();


                String Data = title + " " + comment + " " + detail + " " + term + " " + " " + comment + " " + comm_name + " " + comm_desc + " " + detail + " " + /*
                         * entity + " " +
                         */ sname + " " + sdesc + " " + affilicate + " " + host;
                Data = StdRetrieval.getCleanData(Data);
                ArrayList<Term> vsm = SplitWord.Result(Data);
                vsml.put(col_id, vsm);
            }

        } catch (Exception ex) {
            Logger.getLogger(ExtractData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return vsml;
    }

    //Get colloquium data
    public static TreeMap<Integer, ArrayList<Term>> extractColloquiumBaseOnSpeaker() {
        TreeMap<Integer, ArrayList<Term>> vsml = new TreeMap<Integer, ArrayList<Term>>();
        try {
            DB db0 = new DB();
            ResultSet rs0 = db0.getResultSet("select distinct speaker_id from col_speaker");
            int speaker_id = 0;
            while (rs0.next()) {
                speaker_id = rs0.getInt("speaker_id");
                String Data = "";

                DB db = new DB();
                ResultSet rs = db.getResultSet("SELECT distinct col_id from colloquium where speaker_id=" + speaker_id);
                while (rs.next()) {
                    int col_id = rs.getInt("col_id");

                    //detail title
                    DB db2 = new DB();
                    ResultSet rs2 = db2.getResultSet("select title,detail from colloquium where col_id=" + col_id);
                    String title = "";
                    String detail = "";
                    while (rs2.next()) {
                        title += rs2.getString("title");
                        detail += rs2.getString("detail");
                    }
                    db2.close();
                    //term
                    DB db3 = new DB();
                    ResultSet rs3 = db3.getResultSet("select term from colterm where col_id=" + col_id);
                    String term = "";
                    while (rs3.next()) {
                        term += rs3.getString("term");
                    }
                    db3.close();
                    //comment
                    DB db4 = new DB();
                    ResultSet rs4 = db4.getResultSet("select comment from comment where comment_id in (select comment_id from comment_col where col_id=" + col_id + ")");
                    String comment = "";
                    while (rs4.next()) {
                        comment += rs4.getString("comment");
                    }
                    db4.close();
                    //entity
                    /*
                     * DB db5 = new DB(); ResultSet rs5 =
                     * db5.getResultSet("select entity from entity where
                     * entity_id in (select entity_id from entities where
                     * col_id=" + col_id + " ) "); String entity = ""; while
                     * (rs5.next()) { entity += rs5.getString("entity"); }
                     * db5.close();
                     */
                    //community
                    DB db6 = new DB();
                    ResultSet rs6 = db6.getResultSet("select comm_name, comm_desc from community where comm_id in (select comm_id from contribute where col_id=" + col_id + " ) ");
                    String comm_name = "";
                    String comm_desc = "";
                    while (rs6.next()) {
                        comm_name += rs6.getString("comm_name");
                        comm_desc += rs6.getString("comm_desc");
                    }
                    db6.close();
                    //series
                    DB db7 = new DB();
                    ResultSet rs7 = db7.getResultSet("select name,description from series where series_id in (select series_id from seriescol where col_id= " + col_id + " ) ");
                    String sname = "";
                    String sdesc = "";
                    while (rs7.next()) {
                        sname += rs7.getString("name");
                        sdesc += rs7.getString("description");
                    }
                    db7.close();
                    //affilicate
                    DB db8 = new DB();
                    ResultSet rs8 = db8.getResultSet("select affiliate from affiliate where affiliate_id in (select affiliate_id from affiliate_col where col_id= " + col_id + " ) ");
                    String affilicate = "";
                    while (rs8.next()) {
                        affilicate += rs8.getString("affiliate");
                    }
                    db8.close();
                    //host
                    DB db9 = new DB();
                    ResultSet rs9 = db9.getResultSet("select host from host where host_id in (select host_id from colloquium where col_id= " + col_id + " ) ");
                    String host = "";
                    while (rs9.next()) {
                        host += rs9.getString("host");
                    }
                    db9.close();

                    Data += title + " " + comment + " " + detail + " " + term + " " + " " + comment + " " + comm_name + " " + comm_desc + " " + detail + " " + /*
                             * entity +
                             */ " " + sname + " " + sdesc + " " + affilicate + " " + host;
                }

                Data = StdRetrieval.getCleanData(Data);
                if (Data.length() > 1) {
                    ArrayList<Term> vsm = SplitWord.Result(Data);
                    if (vsm != null && vsm.size() > 0) {
                        vsml.put(speaker_id, vsm);
                        System.out.println(speaker_id);
                    }
                }


            }

        } catch (Exception ex) {
            Logger.getLogger(ExtractData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return vsml;
    }

    //Prgressbase caculation
    public static void CaculateProgressScore() {
        DB db = new DB();
        ResultSet rs = db.getResultSet("select * from rec_progressbase");
        try {
            while (rs.next()) {

                int pdg_id = rs.getInt("pdg_id");
                float timespan = rs.getFloat("timespan");
                float clicknum = rs.getFloat("clicknum");
                float movenum = rs.getFloat("movenum");
                float score = (float) (Math.log(timespan) / Math.log(1000000000));
                score *= (float) (Math.log(clicknum + 500) / Math.log(500));
                score *= (float) (Math.log(movenum) / Math.log(10000));
                if (Float.isNaN(score) || Float.isInfinite(score)) {
                    score = 0;
                }
                DB dbi = new DB();
                dbi.executeUpdate("update rec_progressbase set score=" + score + " where pdg_id=" + pdg_id);
                dbi.close();

            }
        } catch (SQLException ex) {
            Logger.getLogger(ExtractData.class.getName()).log(Level.SEVERE, null, ex);
        }
        db.close();

    }

    public static String getDataByUser(int user_id) throws SQLException {

        //Bookmark
        DB db2 = new DB();
        ResultSet rs2 = db2.getResultSet("select userprofile_id,usertags,comment from userprofile where user_id=" + user_id);
        String usertags = "";
        String comment = "";
        while (rs2.next()) {
            usertags += rs2.getString("usertags");
            comment += rs2.getString("comment");
        }
        db2.close();
        //Note
        DB db3 = new DB();
        ResultSet rs3 = db3.getResultSet("select usernote from usernote where user_id=" + user_id);
        String usernote = "";
        while (rs3.next()) {
            usernote += rs3.getString("usernote");
        }
        db3.close();
        //tags
        DB db4 = new DB();
        ResultSet rs4 = db4.getResultSet("select tag from tag where tag_id in (select tag_id from tags where user_id=" + user_id + ")");
        String tag = "";
        while (rs4.next()) {
            tag += rs4.getString("tag");
        }
        db4.close();
        //comment keyword
        DB db5 = new DB();
        ResultSet rs5 = db5.getResultSet("select keyword,comment from keywordcomment where user_id=" + user_id);
        String keyword = "";
        String commentk = "";
        while (rs5.next()) {
            keyword += rs5.getString("keyword");
            commentk += rs5.getString("comment");
        }
        db5.close();
        //outerlink
        DB db6 = new DB();
        ResultSet rs6 = db6.getResultSet("select innertext from userouterlink where user_id=" + user_id);
        String innertext = "";
        while (rs6.next()) {
            innertext += rs6.getString("innertext");
        }
        db6.close();
        //progress-base
        DB db7 = new DB();
        ResultSet rs7 = db7.getResultSet("select detail,title from colloquium where col_id in (select col_id from rec_progressbase where user_id= " + user_id + " order by score )  limit 1,3");
        String title = "";
        String detail = "";
        while (rs7.next()) {
            title += rs7.getString("title");
            detail += rs7.getString("detail");
        }
        db7.close();
        //community
        DB db8 = new DB();
        ResultSet rs8 = db8.getResultSet("select comm_name, comm_desc from community where comm_id in (select comm_id from contribute where user_id=" + user_id + " ) ");
        String comm_name = "";
        String comm_desc = "";
        while (rs8.next()) {
            comm_name += rs8.getString("comm_name");
            comm_desc += rs8.getString("comm_desc");
        }
        db8.close();
        //series
        DB db9 = new DB();
        ResultSet rs9 = db9.getResultSet("select name,description from series where user_id=" + user_id);
        String sname = "";
        String sdesc = "";
        while (rs9.next()) {
            sname += rs9.getString("name");
            sdesc += rs9.getString("description");
        }
        db9.close();
        //extra comment
        DB db10 = new DB();
        ResultSet rs10 = db10.getResultSet("select comment from comment where user_id= " + user_id);
        String commentx = "";
        while (rs10.next()) {
            commentx += rs10.getString("comment");
        }
        db10.close();
        String Data = usertags + " " + comment + " " + usernote + " " + tag + " " + " " + keyword + " " + commentk + " " + title + " " + detail + " " + innertext + " " + comm_name + " " + comm_desc + " " + sname + " " + sdesc + " " + commentx;
        return Data;
    }
    /*
     * public ExtractData(String path) { this.path = path; }
     *
     * public SpeakerVectorSpace writeSPeakerVS() throws Exception {
     * SpeakerVectorSpace vsml = new SpeakerVectorSpace(); try { DB db = new
     * DB(); ResultSet rs = db.getResultSet("select distinct speaker_id from
     * speaker"); while (rs.next()) { int speaker_id = rs.getInt("speaker_id");
     * DB db2 = new DB(); ResultSet rs2 = db2.getResultSet("select detail,title
     * from colloquium where speaker_id in (" + speaker_id + ")"); while
     * (rs2.next()) { String title = rs2.getString("title"); String detail =
     * rs2.getString("detail"); //title and detail weight 2:1 ArrayList<Term>
     * vsm = SplitWord.Result(StdRetrieval.getCleanData(title + title +
     * detail)); vsml.put(speaker_id, vsm); } db2.close(); } db.close(); return
     * vsml; } catch (SQLException ex) {
     * Logger.getLogger(ExtractData.class.getName()).log(Level.SEVERE, null,
     * ex); return null; } }
     *
     * public void extractName() { DB db = new DB(); try { String speaker_name =
     * ""; ResultSet rs = db.getResultSet("select * from col_speaker"); while
     * (rs.next()) { speaker_name = rs.getString("name").replace(" ", "_"); File
     * f = new File(path + speaker_name); if (!f.isDirectory()) { f.mkdir(); }
     * extractColloquium(speaker_name); } db.close(); } catch (SQLException ex)
     * { Logger.getLogger(ExtractData.class.getName()).log(Level.SEVERE, null,
     * ex); } }
     *
     * public void extractColloquium(String speaker_id) { DB db = new DB();
     * speaker_id = speaker_id.replace("_", " "); try { String sql = "";
     * ResultSet rs = db.getResultSet(sql); while (rs.next()) { int col_id =
     * rs.getInt("col_id"); String title = rs.getString("title"); String detail
     * = rs.getString("detail"); String CurrentPath = path +
     * speaker_id.replace(" ", "_") + "\\" + col_id + "-" + title + ".txt"; File
     * f = new File(CurrentPath); if (!f.isFile()) { if (f.createNewFile()) {
     * DataOutputStream dos = new DataOutputStream(new FileOutputStream(f));
     * dos.writeUTF(title + "\r\n" + detail); dos.close();
     * System.out.println("Finished:" + speaker_id); db.close(); } } } } catch
     * (SQLException ex) {
     * Logger.getLogger(ExtractData.class.getName()).log(Level.SEVERE, null,
     * ex); } catch (IOException ee) { } }
     */

    public static void main(String[] args) throws Exception {
    }
}
