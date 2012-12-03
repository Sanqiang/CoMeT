package index;

import API.config;
import Struct.PostingIndex;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Index {

    TreeMap<String, ArrayList<PostingIndex>> IncomeIndexList;

    public Index(TreeMap<String, ArrayList<PostingIndex>> IncomeIndexList) {
        this.IncomeIndexList = IncomeIndexList;
    }

    public void Build() {
        try {
            DataOutputStream DosForTerm = new DataOutputStream(new FileOutputStream(new File(config.IndexTermPath)));
            DataOutputStream DosForPost = new DataOutputStream(new FileOutputStream(new File(config.IndexPostPath)));

            for (Entry<String, ArrayList<PostingIndex>> TermIndex : IncomeIndexList.entrySet()) {
                DosForTerm.writeUTF(TermIndex.getKey());
                for (PostingIndex PostingIndexItem : TermIndex.getValue()) {
                    DosForPost.writeInt(PostingIndexItem.Frequency);
                    DosForPost.writeInt(PostingIndexItem.StartIndex);
                    DosForPost.writeUTF(PostingIndexItem.DocumentId);
                    DosForPost.writeInt(PostingIndexItem.DocumentLength);
                }
                DosForPost.writeInt(-1);
            }
            DosForTerm.writeUTF("|EOF|");
            DosForPost.writeUTF("|EOF|");
            DosForTerm.close();
            DosForPost.close();
        } catch (Exception ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
