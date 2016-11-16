package ru.spbstu.jsentencedetection;

import jsc.independentsamples.SmirnovTest;
import net.uaprom.jmorphy2.MorphAnalyzer;
import net.uaprom.jmorphy2.ParsedWord;
import org.apache.spark.api.java.JavaRDD;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.tartarus.snowball.ext.englishStemmer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * Created by pavel1 on 17.10.16.
 */
public class JSentenceDetection extends DetectionBase<String> {

    private WordVectors model;
    private MorphAnalyzer morph;
    private englishStemmer stemmer;
    private String language = "Russian";
    private static final HashMap<String, String> endings;
    static
    {
        endings = new HashMap<String, String>();
        endings.put("NOUN", "_S");
        endings.put("VERB", "_V");
        endings.put("ADJF", "_A");
        endings.put("ADJS", "_A");
        endings.put("ADVB", "_ADV");
    }

    public JSentenceDetection() {
        this("russian", "ruscorpora.model.bin");

    }

    public JSentenceDetection(String lang, String modelName){

        super();
        language = lang;
        stemmer = new englishStemmer();
        try {
            morph = Jmorphy2TestsHelpers.newMorphAnalyzer("/pymorphy2_dicts_ru");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File gModel = new File(classLoader.getResource(modelName).getFile());
            model = WordVectorSerializer.loadGoogleModel(gModel, true);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    public Boolean detect(List<String> data, String value){

        List<List<String>> dataListList = data.stream().map(e -> Arrays.asList(e.split(" "))).collect(Collectors.toList());

        for (List<String> list : dataListList){
            for(int i = 0; i < list.size(); ++i){
                if(language.equals("russian")) {
                    list.set(i, rusvecForm(list.get(i)));
                }
                if(language.equals("english")) {
                    list.set(i, engvecForm(list.get(i)));
                }
            }
        }

        List<List<INDArray>> dataLis = dataListList.stream().map(x -> x.stream()
                .map(y -> model.getWordVectorMatrix(y)).collect(Collectors.toList())).collect(Collectors.toList());
        for (List<INDArray> list : dataLis){
            list.removeAll(Collections.singleton(null));
        }
        dataLis.removeAll(Collections.singleton(null));

        List<JavaRDD<INDArray>> dataList = dataLis.stream().map(x -> sc.parallelize(x)).collect(Collectors.toList());

        Function<String, String> vecform = x->x;
        if(language.equals("russian")) {
            vecform = this::rusvecForm;
        }
        if(language.equals("english")) {
            vecform = this::engvecForm;
        }
        List<INDArray> valueLis = Arrays.stream(value.split(" ")).map(vecform)
                .map(x -> model.getWordVectorMatrix(x)).collect(Collectors.toList());
        valueLis.removeAll(Collections.singleton(null));
        JavaRDD<INDArray> valueList = sc.parallelize(valueLis);

        List<INDArray> dataVectorsList = dataList.stream().map(x -> x.reduce((a,b) -> a.add(b))).collect(Collectors.toList());
        JavaRDD<INDArray> dataVectors = sc.parallelize(dataVectorsList);

        INDArray valueVector = valueList.reduce((x,y) -> x.add(y));


        JavaRDD<Double> dataSample = dataVectors.flatMap(x -> dataVectorsList.stream().map(y -> y.mul(x).getDouble(0)).collect(Collectors.toList()));

        JavaRDD<Double> valueSample = dataVectors.map(x -> x.mul(valueVector).getDouble(0));

        List<Double> test1 = dataSample.collect();
        List<Double> test2 = valueSample.collect();

        SmirnovTest test = new SmirnovTest(dataSample.collect().stream().mapToDouble(x->x).toArray(),
                valueSample.collect().stream().mapToDouble(x->x).toArray());

        Double pvalue = test.getSP();

        return pvalue < 0.025;
    }


    private String rusvecForm(String word){
        ParsedWord parsedWord = null;
        try {
            parsedWord = morph.parse(word).get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (parsedWord == null || parsedWord.tag.POS == null) {
            return word;
        }
        if(!endings.containsKey(parsedWord.tag.POS.toString())){
            return word;
        }
        return parsedWord.normalForm + endings.get(parsedWord.tag.POS.toString());
    }

    private String engvecForm(String word){
        stemmer.setCurrent(word);
        if(stemmer.stem()) {
            return stemmer.getCurrent();
        }
        return word;
    }
}
