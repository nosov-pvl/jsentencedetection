package ru.spbstu.jsentencedetection;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
//import ru.spbstu.frauddetection.FraudConfig.ObjectModel.Group;
//import ru.spbstu.frauddetection.InputDataCalculator.ValueGroup;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

public abstract class DetectionBase<T> implements Closeable{
    protected JavaSparkContext sc;

    public DetectionBase(JavaSparkContext sc) {
        this.sc = sc;
    }

    public DetectionBase(){
        SparkConf conf = new SparkConf()
                .setAppName("Detectio").setMaster("local[*]");
        sc = new JavaSparkContext(conf);
    }

    public DetectionBase(String master){
        SparkConf conf = new SparkConf()
                .setAppName("Detectio").setMaster(master);
        sc = new JavaSparkContext(conf);
    }

    public JavaSparkContext getContext() {
        return sc;
    }

    public void close() throws IOException {
        sc.close();
    }

//    public Object convertToType(ValueGroup valueGroup, Group configGroup) {
//        return valueGroup;
//    }
//
//    public List convertToTypeList(List<ValueGroup> data, Group configGroup) {
//        return data;
//    }

    public abstract Boolean detect(List<T> data, T value);
}
