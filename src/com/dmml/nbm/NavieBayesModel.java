package com.dmml.nbm;

import javax.print.Doc;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by macan on 2016/11/4.
 */
public class NavieBayesModel {
    private int numberOfSpam;
    private int numberOfHam;

    private Map<String, Integer> spamCountMap;
    private Map<String, Integer> hamCountMap;

    private Map<String, Double> spamProbilityMap;
    private Map<String, Double> hamProbilityMap;

    private double spamPrior;
    private double hamPrior;

    private int numberOfCategort;
    /**
     * 构造方法，进行数据的初始化
     * @param corpus
     */
    public NavieBayesModel(Corpus corpus, int numberOfCategort){
        this.numberOfCategort = numberOfCategort;
        numberOfSpam = corpus.getNumberOfSpam();
        numberOfHam = corpus.getNumberOfHam();

        spamCountMap = corpus.getSpanTermCountMap();
        hamCountMap = corpus.getHamTermCountMap();

        spamPrior = ((numberOfSpam + 1) * 1.0) / (numberOfSpam + numberOfHam + numberOfCategort);
        hamPrior = ((numberOfHam + 1) * 1.0) / (numberOfSpam + numberOfHam + numberOfCategort);

        spamProbilityMap = new HashMap<>();
        hamProbilityMap = new HashMap<>();
    }


    /**
     * 训练垃圾邮件
     */
    public void run(){
        double p_span = 0.0;
        double p_ham = 0.0;
        for (Map.Entry<String, Integer> entry : spamCountMap.entrySet()){
            String word = entry.getKey();
            int  countOfSpam = entry.getValue();
            int N_i = 1;
            //如果单词在正常邮件中也出现过，那么Ni = 2,否则为1
            if (hamCountMap.containsKey(word)){
                N_i++;
            }
            p_span = ((countOfSpam + 1) * 1.0) / (numberOfSpam + N_i);
            spamProbilityMap.put(word, p_span);
        }

        for (Map.Entry<String, Integer> entry : hamCountMap.entrySet()){
            String word = entry.getKey();
            int countOfHam = entry.getValue();
            int N_i = 1;
            if (spamCountMap.containsKey(word)){
                N_i++;
            }
            p_ham = ((countOfHam + 1 ) *1.0) / (numberOfHam + N_i);
            hamProbilityMap.put(word, p_ham);
        }
    }


    /**
     * 判断一封邮件是否是垃圾邮件
     * @param path 需要判断邮件的地址
     * @return  垃圾邮件返回true, 否则返回false
     */
    public boolean isSpam(String path) {
        boolean isSpam = false;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(path)));
            String line;
            if ((line = reader.readLine()) != null) {
                StringTokenizer tokenizer = new StringTokenizer(line);
                ArrayList<String> doc = new ArrayList<>();
                double p_spam = 1.0;
                double p_ham = 1.0;
                while (tokenizer.hasMoreTokens()) {
                    String word = tokenizer.nextToken();
                    if (word.equals("spam"))
                    doc.add(word);
                    //计算属于哪一个邮件的probability
                    //如果该单词出现在垃圾邮件的字典中
                    if (spamCountMap.containsKey(word)) {
                        p_spam *= spamProbilityMap.get(word);
                    } else {
                        p_spam *= (1 * 1.0) / (numberOfSpam + 1);
                    }
                }


                p_spam *= spamPrior;
                for (String word : doc) {
                    if (hamCountMap.containsKey(word)) {
                        p_ham *= hamProbilityMap.get(word);
                    } else {
                        p_ham *= (1 * 1.0) / (numberOfHam + 1);
                    }
                }
                p_ham *= hamPrior;

                System.out.println("p_spam = " + p_spam + "    p_ham = " + p_ham);
                if (p_spam > p_ham) {
                    isSpam = true;
                } else {
                    isSpam = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isSpam;
    }
}
