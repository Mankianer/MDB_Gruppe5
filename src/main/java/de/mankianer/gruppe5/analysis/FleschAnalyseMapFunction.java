package de.mankianer.gruppe5.analysis;

import de.mankianer.gruppe5.model.Tweet;
import de.mankianer.gruppe5.model.analyse.Analyse;
import de.mankianer.gruppe5.model.analyse.FleschAnalyse;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import org.apache.flink.api.common.functions.MapFunction;

import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FleschAnalyseMapFunction implements MapFunction<Tweet, Analyse> {
    @Override

    // FI = 206,835 - 84,6 x WL - 1,015 x SL
    // WL = durchschnittliche Wortlaenge in Silben ohne Schluss-e
    // SL = durchschnittliche Satzlaenge in Woerter
    public Analyse map(Tweet tweet) throws Exception {
        //final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        //DataSet<String> input = env.fromElements(tweet.getText());
        String text = tweet.getText();


        String[] sentences;
        ArrayList<String> words = new ArrayList<>();
        //Berechnen von SL

        try (InputStream modelIn = new FileInputStream("/home/sysadmin/eclipse-workspace/Gruppe5/src/main/java/de/mankianer/gruppe5/analysis/en-sent.bin")) {
            SentenceModel model = new SentenceModel(modelIn);
            SentenceDetectorME sentenceDetector = new SentenceDetectorME(model);
            sentences = sentenceDetector.sentDetect(text);
        }

        int sentencesCount = sentences.length;
        int wordsCount = 0;

        for (String sentence : sentences){
            String[] wordsA = sentence.split("\\W");
            words.addAll(Arrays.asList(wordsA));
        }
        //TODO test
        words.removeIf((s) -> s.equals(""));
        wordsCount = words.size();

        //SL
        int sl = sentencesCount / wordsCount;

        //Berechnen von WL
        int syllablesCount = 0;
        for (String word : words){
            syllablesCount += countSyllablesWrapper(word);
        }

        //WL
        int wl = syllablesCount / wordsCount;


        // FI = 206,835 - 84,6 x WL - 1,015 x SL
        double fi = 206.835 - 84.6 * wl - 1.015 * sl;

        return tweet.addAnalyse(new FleschAnalyse(fi));
    }

    private int countSyllablesWrapper(String word)
    {
    	if(word.length() == 0) return 0;
    	
        int count = 0;
        word = word.toLowerCase();

        if (word.charAt(word.length()-1) == 'e') {
            if (silente(word))
                count = count + countSyllabes(word.substring(0, word.length()-1));
            else //the
                count++;
        } else
            count = count + countSyllabes(word);

        return count;
    }

    private int countSyllabes(String word) {
        int count = 0;
        Pattern splitter = Pattern.compile("[^aeiouy]*[aeiouy]+");
        Matcher m = splitter.matcher(word);

        while (m.find())
            count++;

        return count;
    }

    private boolean silente(String word) {
        word = word.substring(0, word.length()-1);

        Pattern pat = Pattern.compile("[aeiouy]");
        Matcher m = pat.matcher(word);

        return m.find();
    }


}
