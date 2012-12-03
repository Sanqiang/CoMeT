package API;

import DB.DB;
import DB.ExtractData;
import cc.mallet.classify.ClassifierTrainer;
import cc.mallet.classify.MaxEntTrainer;
import cc.mallet.pipe.*;
import cc.mallet.pipe.iterator.CsvIterator;
import cc.mallet.pipe.iterator.FileIterator;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import cc.mallet.types.Labeling;
import java.io.*;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Pattern;

public class Mallet_test {

    public static void main(String[] args) throws Exception {
        //setup
        ArrayList<Pipe> PipeList = new ArrayList<Pipe>();
        PipeList.add(new Input2CharSequence("UTF-8"));
        PipeList.add(new CharSequence2TokenSequence(Pattern.compile("[\\p{L}\\p{N}_]+")));
        PipeList.add(new TokenSequenceLowercase());
        PipeList.add(new TokenSequenceRemoveStopwords(false, false));
        PipeList.add(new Target2Label());
        InstanceList instances = new InstanceList(new SerialPipes(PipeList));
        //read train data
        FileIterator fileiter = new FileIterator("D:\\workspace\\train\\SpeakerRec\\", FileIterator.LAST_DIRECTORY);
        instances.addThruPipe(fileiter);
        //start train
        ClassifierTrainer trainer = new cc.mallet.classify.MaxEntTrainer();
        //ClassifierTrainer trainer = new MaxEntTrainer();
        trainer.train(instances);
        
        CsvIterator reader = new CsvIterator(new FileReader(new java.io.File("D:\\workspace\\train\\web\\test\\elizabeth_needham.txt")), "(\\w+)\\s+(\\w+)\\s+(.*)", 3, 2, 1);
        Iterator<Instance> iter = trainer.getClassifier().getInstancePipe().newIteratorFrom(reader);
        while (iter.hasNext()) {
            Labeling lab = trainer.getClassifier().classify(iter.next()).getLabeling();
            for (int rank = 0; rank < lab.numLocations(); rank++) {
                System.out.println(lab.getLabelAtRank(rank) + "-" + lab.getValueAtRank(rank));
            }
        }
    }
}
