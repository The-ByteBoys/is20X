package tools;

import java.util.List;

public class PointCalculator {
    private double mean;
    private double stdev;

    public PointCalculator(List<Double> numList){
        calculate(numList);
    }

    public PointCalculator(List<Double> numList, int length){
        for (int i = numList.size(); i < length; i++) {
            numList.add(0.0);
        }

        calculate(numList);
    }

    public double getMean() {
        return mean;
    }

    public double getStdev() {
        return stdev;
    }

    public double getPoints(double value, int weight){
        double points = 0.0;
        if(value != 0){
            points = (value-mean)/stdev*1;
        }
        return points*((double) weight/100);
    }

    private void calculate(List<Double> numList){
        double sum = 0.0;
        double standardDev = 0.0;
        int length = numList.size();

        for(double num : numList){
            sum += num;
        }

        mean = sum/length;

        for(double num : numList){
            standardDev += Math.pow(num-mean, 2);
        }

        stdev = Math.sqrt(standardDev/length);
    }
}
