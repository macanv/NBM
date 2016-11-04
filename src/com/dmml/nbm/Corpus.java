package com.dmml.nbm;

/**
 * Created by macan on 2016/10/19.
 */

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 语料库处理
 */
public class Corpus {
    /**
     * word-index map
     */
    private Map<String, Integer> spanTermCountMap;
    private Map<String, Integer> hamTermCountMap;


    private int numberOfSpam;
    private int numberOfHam;

    /**
     * 保存邮件label 1--- 0----
     */
    private  ArrayList<Integer> category;

    public Corpus(String dataPath){
        category = new ArrayList<>();
        spanTermCountMap = new HashMap<>();
        hamTermCountMap = new HashMap<>();

        numberOfHam = 0;
        numberOfSpam = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(dataPath)));
            String line;
            while ((line = reader.readLine()) != null){
                StringTokenizer tokenizer = new StringTokenizer(line);
                String word ;
                int i = 0;

                if (tokenizer.hasMoreTokens()){
                    String head = tokenizer.nextToken();
                    //如果这篇文档是垃圾邮件
                    if (head.equals("spam")){
                        applyDcoument(tokenizer, true);
                    }else if (head.equals("ham")){ //正常邮件
                        applyDcoument(tokenizer, false);
                    }
                }


            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static boolean removeTerm(String string){
        // TODO Auto-generated method stub
        string = string.toLowerCase().trim();
        //匹配字符串
        Pattern MY_PATTERN = Pattern.compile(".*[a-zA-Z]+.*");
        Matcher m = MY_PATTERN.matcher(string);
        // filter @xxx and URL
        if(string.matches(".*www\\..*") || string.matches(".*\\.com.*") ||
                string.matches(".*http:.*") || string.matches("From:") || string.matches("to:") ||
                string.matches(">?") )
            return true;
        if (!m.matches()) {
            return true;
        } else
            return false;
    }



    public void applyDcoument(StringTokenizer tokenizer, boolean isSpan){
        String word;
        while (tokenizer.hasMoreTokens()) {
            word = tokenizer.nextToken();
            if (word.length() < 2 || removeTerm(word)){
                continue;
            }
            if (isSpan){
                numberOfSpam++;
                countSpanWord(word);
            }else{
                numberOfHam++;
                countHanWord(word);
            }
        }

    }
    /**
     * 统计span邮件上每个单词的词频
     * @param word 单词
     */
    public  void countSpanWord(String word){
        if (spanTermCountMap.containsKey(word)) {
            spanTermCountMap.put(word, spanTermCountMap.get(word) + 1);
        } else {
            spanTermCountMap.put(word, 1);
        }
    }

    /**
     * 统计ham邮件上每个单词的词频
     * @param word word
     */
    public  void countHanWord(String word){
        if (hamTermCountMap.containsKey(word)) {
            hamTermCountMap.put(word, hamTermCountMap.get(word) + 1);
        } else {
            hamTermCountMap.put(word, 1);
        }
    }


    public ArrayList<Integer> getCategory() {
        return category;
    }

    public Map<String, Integer> getSpanTermCountMap() {
        return spanTermCountMap;
    }

    public Map<String, Integer> getHamTermCountMap() {
        return hamTermCountMap;
    }

    public int getNumberOfSpam() {
        return numberOfSpam;
    }

    public int getNumberOfHam() {
        return numberOfHam;
    }
}
