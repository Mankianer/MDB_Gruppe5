package de.mankianer.gruppe5.analysis;

import de.mankianer.gruppe5.model.Tweet;
import de.mankianer.gruppe5.model.analyse.Analyse;
import de.mankianer.gruppe5.model.analyse.NameAnalyse;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.Span;
import org.apache.flink.api.common.functions.MapFunction;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Analyse nach Namen
 * @author sysadmin
 *
 */
public class NameMapFunction implements MapFunction<Tweet, Analyse> {
    @Override
    public Analyse map(Tweet value) throws Exception {
        String text = value.getText();
        ArrayList<String> namesList = new ArrayList<>();

        try (InputStream modelIn = new FileInputStream("/home/sysadmin/eclipse-workspace/Gruppe5/src/main/java/de/mankianer/gruppe5/analysis/en-ner-person.bin")){
            TokenNameFinderModel model = new TokenNameFinderModel(modelIn);
            NameFinderME nameFinder = new NameFinderME(model);

            Span[] names = nameFinder.find(text.split("\\s"));
            for(Span name : names){
                namesList.add(name.toString());
            }
        }

        String[] namesArray =namesList.toArray(new String[0]);
        return value.addAnalyse(new NameAnalyse(namesArray));
    }
}
