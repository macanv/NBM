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
     * 构造方法，进行数据的初始化
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
     * 训练垃圾邮件
     */
    public void tarinSpam(){

    }
}
