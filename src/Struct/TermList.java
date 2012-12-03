//used for midium of document processing
package Struct;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeMap;

public class TermList extends TreeMap<String, Term> {

    @Override
    public Term put(String key, Term value) {
        if (this.keySet().contains(key)) {
            this.get(key).Frequency++;
            return this.get(key);
        } else {
            return super.put(key, value);
        }
    }

    public ArrayList<Term> UnSort() {
        ArrayList<Term> arr = new ArrayList<Term>(this.values());
        return arr;
    }

    public ArrayList<Term> Sort() {
        ArrayList<Term> arr = new ArrayList<Term>(this.values());
        Collections.sort(arr, new Comparator() {

            @Override
            public int compare(Object o1, Object o2) {
                Term t1 = (Term) o1;
                Term t2 = (Term) o2;
                if (t1.Frequency > t2.Frequency) {
                    return -1;
                } else if (t1.Frequency < t2.Frequency) {
                    return 1;
                } else {
                    return 0;
                }
                //throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        return arr;
    }
}
