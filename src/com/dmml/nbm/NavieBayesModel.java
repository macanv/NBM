package com.dmml.nbm;

import javax.print.Doc;
import java.util.Map;

/**
 * Created by macan on 2016/11/4.
 */
public class NavieBayesModel {
    private int numberOfSpam;
    private int getNumberOfHam;

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
        getNumberOfHam = corpus.getNumberOfHam();

        spamCountMap = corpus.getSpanTermCountMap();
        hamCountMap = corpus.getHamTermCountMap();

        spamPrior = (numberOfSpam + 1) / (numberOfSpam + getNumberOfHam + numberOfCategort);
        hamPrior = (getNumberOfHam + 1) / (numberOfSpam + getNumberOfHam + numberOfCategort);
    }


    /**
     * ѵ�������ʼ�
     */
    public void tarinSpam(){

    }
}
