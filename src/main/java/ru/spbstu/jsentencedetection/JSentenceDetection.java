package ru.spbstu.jsentencedetection;

import jsc.independentsamples.SmirnovTest;
import net.uaprom.jmorphy2.MorphAnalyzer;
import net.uaprom.jmorphy2.ParsedWord;
import org.apache.spark.api.java.JavaRDD;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.nd4j.linalg.api.ndarray.INDArray;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Created by pavel1 on 17.10.16.
 */
public class JSentenceDetection extends DetectionBase<String> {

    private WordVectors model;
    private MorphAnalyzer morph;
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

        super();
        try {
            morph = Jmorphy2TestsHelpers.newMorphAnalyzer("/pymorphy2_dicts_ru");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            File gModel = new File("/Users/pavel1/workspace/summer_practice/jword2vec/src/main/resources/ruscorpora.model.bin");
            model = WordVectorSerializer.loadGoogleModel(gModel, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JSentenceDetection(String modelName){

        super();
        try {
            morph = Jmorphy2TestsHelpers.newMorphAnalyzer("/pymorphy2_dicts_ru");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            File gModel = new File(modelName);
            model = WordVectorSerializer.loadGoogleModel(gModel, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Boolean detect(List<String> data, String value){

        List<List<String>> dataListList = data.stream().map(e -> Arrays.asList(e.split(" "))).collect(Collectors.toList());

     //   dataListList = dataListList.stream().map(x -> {
     //       x.stream().map(a -> rusvecForm(a)).collect(Collectors.toList());
     //       return x;
     //   }).collect(Collectors.toList());

        for (List<String> list : dataListList){
            for(int i = 0; i < list.size(); ++i){
                list.set(i, rusvecForm(list.get(i)));
            }
        }

        List<List<INDArray>> dataLis = dataListList.stream().map(x -> x.stream()
                .map(y -> model.getWordVectorMatrix(y)).collect(Collectors.toList())).collect(Collectors.toList());
        for (List<INDArray> list : dataLis){
            list.removeAll(Collections.singleton(null));
        }
        dataLis.removeAll(Collections.singleton(null));

        List<JavaRDD<INDArray>> dataList = dataLis.stream().map(x -> sc.parallelize(x)).collect(Collectors.toList());

        List<INDArray> valueLis = Arrays.stream(value.split(" ")).map(x -> rusvecForm(x))
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

  //  public Boolean detect_old_almostworking(List<String> data, String value){

  //      List<List<String>> dataListList = data.stream().map(e -> Arrays.asList(e.split(" "))).collect(Collectors.toList());

  //      for(List<String> x : dataListList){
  //         x.stream().map(this::rusvecForm).collect(Collectors.toList());
  //      }

  //      List<JavaRDD<String>> dataListBase = dataListList.stream().map(x -> sc.parallelize(x)).collect(Collectors.toList());

  //      List<JavaRDD<INDArray>> dataList = dataListBase.stream().map(x -> x.map(y -> model.getWordVectorMatrix(y))).collect(Collectors.toList());
  //
  //
  //      JavaRDD<INDArray> valueList = sc.parallelize(Arrays.asList(value.split(" "))).map(x -> rusvecForm(x))
  //              .map(x -> model.getWordVectorMatrix(x));

  //      List<INDArray> dataVectorsList = dataList.stream().map(x -> x.reduce((a,b) -> a.add(b))).collect(Collectors.toList());
  //      JavaRDD<INDArray> dataVectors = sc.parallelize(dataVectorsList);

  //      INDArray valueVector = valueList.reduce((x,y) -> x.add(y));


   //     JavaRDD<Double> dataSample = dataVectors.flatMap(x -> dataVectors.map(y -> y.mul(x).getDouble(0)).collect());

//        JavaRDD<Double> valueSample = dataVectors.map(x -> x.mul(valueVector).getDouble(0));

  //      SmirnovTest test = new SmirnovTest(dataSample.collect().stream().mapToDouble(x->x).toArray(),
    //            valueSample.collect().stream().mapToDouble(x->x).toArray());

      //  Double pvalue = test.getSP();

     //   return pvalue < 0.025;
    //}

//    public Boolean detect_old(List<String> data, String value){
//
//        JavaRDD<List<String>> dataListBase = sc.parallelize(data).map(e -> Arrays.asList(e.split(" ")));
//
//        //dataListBase = dataListBase.map(list -> list.stream().map(this::rusvecForm).collect(Collectors.toList()));
//
//        dataListBase = dataListBase.map(list -> {
//            for(String x : list){
//                x = rusvecForm(x);
//            }
//            return list;
//        });
//        JavaRDD<List<INDArray>> dataList = dataListBase.map(list -> {
//            List<INDArray> array = new ArrayList<INDArray>();
//            for(String x : list){
//                array.add(model.getWordVectorMatrix(x));
//            }
//            return array;
//        });
//        JavaRDD<INDArray> valueList = sc.parallelize(Arrays.asList(value.split(" "))).map(x -> rusvecForm(x))
//                .map(x -> model.getWordVectorMatrix(x));
//
//        JavaRDD<INDArray> dataVectors = dataList.map(list -> {
//            return sc.parallelize(list).reduce((x,y) -> x.add(y));
//        });
//        INDArray valueVector = valueList.reduce((x,y) -> x.add(y));
//
//
//        JavaRDD<Double> dataSample = dataVectors.flatMap(x -> dataVectors.map(y -> y.mul(x).getDouble(0)).collect());

//        JavaRDD<Double> valueSample = dataVectors.map(x -> x.mul(valueVector).getDouble(0));
//
//        SmirnovTest test = new SmirnovTest(dataSample.collect().stream().mapToDouble(x->x).toArray(),
//                valueSample.collect().stream().mapToDouble(x->x).toArray());
//
//        Double pvalue = test.getSP();

//        return pvalue < 0.025;
//    }

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

}
