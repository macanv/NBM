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
     * ���췽�����������ݵĳ�ʼ��
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
     * ѵ�������ʼ�
     */
    public void run(){
        double p_span = 0.0;
        double p_ham = 0.0;
        for (Map.Entry<String, Integer> entry : spamCountMap.entrySet()){
            String word = entry.getKey();
            int  countOfSpam = entry.getValue();
            int N_i = 1;
            //��������������ʼ���Ҳ���ֹ�����ôNi = 2,����Ϊ1
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
     * �ж�һ���ʼ��Ƿ��������ʼ�
     * @param path ��Ҫ�ж��ʼ��ĵ�ַ
     * @return  �����ʼ�����true, ���򷵻�false
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
                    //����������һ���ʼ���probability
                    //����õ��ʳ����������ʼ����ֵ���
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
