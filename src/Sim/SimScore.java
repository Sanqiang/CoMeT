package Sim;

import Struct.Term;
import java.util.ArrayList;

public class SimScore {

    ArrayList<Term> t1;
    ArrayList<Term> t2;

    public SimScore(ArrayList<Term> t1, ArrayList<Term> t2) {
        this.t1 = t1;
        this.t2 = t2;
    }

    public double getVSMScore() {
        double x = 0, y = 0, z = 0;
        boolean FirstLoop = true;
        for (Term tm1 : t1) {
            y += tm1.Frequency * tm1.Frequency;
            for (Term tm2 : t2) {
                if (FirstLoop) {
                    z += tm2.Frequency * tm2.Frequency;
                }
                if (tm1.Word.equals(tm2.Word)) {
                    x += tm1.Frequency * tm2.Frequency;
                    if (!FirstLoop) {
                        break;
                    }
                }
            }
            FirstLoop = false;
        }
        return x / (Math.sqrt(y) * Math.sqrt(z));
    }

    public float getKLDivergenceScore() {
        float kl1 = 0, kl2 = 0;
        float infinity = 10000000;
        double accretion1 = infinity, accretion2 = infinity;
        for (Term tm1 : t1) {
            for (Term tm2 : t2) {
                if (tm1.Word.equals(tm2.Word)) {
                    accretion1 = tm1.Frequency * Math.log(tm1.Frequency / tm2.Frequency);
                    accretion2 = tm2.Frequency * Math.log(tm2.Frequency / tm1.Frequency);
                    break;
                }
            }
            kl1 += accretion1;
            kl2 += accretion2;
            accretion1 = accretion2 = infinity;
        }
        //return (kl1 + kl2) / 2;
        return kl1;
    }
    
}
