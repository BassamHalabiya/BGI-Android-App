package com;

/**
 * Created by Sachi on 1/29/2018.
 */

public class DataSample {
    private String disease;
    private String genes_tested;
    private String tag;
    private String result;


    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public String getGenes_tested() {
        return genes_tested;
    }

    public void setGenes_tested(String genes_tested) {
        this.genes_tested = genes_tested;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public DataSample(String disease, String genes_tested, String tag, String result) {
        this.disease = disease;
        this.genes_tested = genes_tested;
        this.tag = tag;
        this.result = result;

    }

    @Override
    public String toString() {
        return "DataSample{" +
                "disease='" + disease + '\'' +
                ", genes_tested='" + genes_tested + '\'' +
                ", tag='" + tag + '\'' +
                ", result='" + result + '\'' +
                '}';
    }
}
