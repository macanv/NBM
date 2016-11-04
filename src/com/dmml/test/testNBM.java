package com.dmml.test;

/**
 * Created by macan on 2016/10/19.
 */

import com.dmml.nbm.Corpus;
import com.dmml.nbm.NavieBayesModel;
import org.junit.Test;

/**
 * NBM test
 */
public class testNBM {

    @Test
    public  void  testModel(){
        String dataPath = "emails/training/SMSCollection.txt";
        String textPath = "emails/test/test.txt";
        Corpus corpus = new Corpus(dataPath);
        NavieBayesModel model = new NavieBayesModel(corpus, 2);
        model.run();

        boolean isspam = model.isSpam(textPath);
        System.out.println("the mail is spam?  " + isspam);
    }
}
