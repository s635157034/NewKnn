package NewKnn;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Created by root on 17-6-22.
 */
public class KnnMain {
    public static final int K = 5;
    public static int NUMBER = 768;
    private static int Max = 1000;
    private static ArrayList<DataInfo> trainData =null;
    private static ArrayList<DataInfo> testData = null;

    public static void main(String[] args) throws Exception {
        String inputPath="test/data";
        String trainInput = "test/trainData";
        String trainPath = "test/train";
        String outputPath = "test/out";
        Train(inputPath, trainPath, NUMBER);
        Test(inputPath, trainPath, outputPath, NUMBER, 1);
        /*for(double i=-10;i<10;i++)
            System.out.println(Math.atan(i));*/

    }



    public static void Train(String inputPath,String outputPath,int number) throws Exception {
        CalTime.Start();
        trainData = new ArrayList<>(number);
        readFiles(inputPath, trainData);
        for(int i=0;i<Max;i++){
            //Iterator<DataInfo> totalit= trainData.iterator();
            /*
            * 第一重循环，循环每一个点
            */
            for (DataInfo aTestData : trainData) {
                TotalDistance totalDistance=new TotalDistance();
                double maxDistance=99999;
                /*
                * 第二次循环，每个点和所有点进行距离计算，得到K个最近点，
                */
                for (DataInfo dataInfo1 : trainData) {
                    DistanceInfo tmp = aTestData.calDistance(dataInfo1, maxDistance);
                    if (tmp != null) {
                        maxDistance = totalDistance.add(tmp);
                    }
                }
                //将对错加到临时变量中
                if (!aTestData.label.equals(totalDistance.getLabel(1)))
                    totalDistance.addWeight(aTestData.label);
            }

            //更新对错次数
            for(DataInfo tmp:trainData){
                tmp.update();
            }
        }
        writeFIles(outputPath);
        CalTime.End();
    }

    public static void Test(String inputPath, String trainPath, String outputPath, int number, int weight) throws Exception {
        CalTime.Start();
        int count=0;
        testData = new ArrayList<>(number);
        trainData = new ArrayList<>(number);
        readFiles(inputPath,testData);
        readTrainFiles(trainPath);
        for (DataInfo aTestData : testData) {
            TotalDistance totalDistance = new TotalDistance();
            double maxDistance = 99999;
            DataInfo dataInfo = aTestData;
            /*
             * 每个点和训练集进行距离计算，得到K个最近点，
             */
            for (DataInfo dataInfo1 : trainData) {
                DistanceInfo tmp = dataInfo.calDistance(dataInfo1, maxDistance);
                if (tmp != null) {
                    maxDistance = totalDistance.add(tmp);
                }
            }
            //输出原始类别和计算后的类别
            dataInfo.mostLabel = totalDistance.getLabel(weight);
            if (!dataInfo.mostLabel.equals(dataInfo.label))
                count++;
        }
        writeTestFIles(outputPath);
        System.out.println(count);
        System.out.println((double) count / testData.size());
        CalTime.End();
    }

    public static void readFiles(String path,ArrayList<DataInfo> infos) throws Exception {
        FileInputStream fs = new FileInputStream(path);
        Scanner scanner = new Scanner(fs);
        while (scanner.hasNext()){
            infos.add(new DataInfo(scanner.nextLine()));
        }
    }
    public static void writeFIles(String path) throws  Exception{
        FileOutputStream fos = new FileOutputStream(path);
        PrintWriter pw = new PrintWriter(fos);
        Iterator<DataInfo> it= trainData.iterator();
        while (it.hasNext()) {
            pw.println(it.next().toTrainString());
        }
        pw.close();
    }
    public static void readTrainFiles(String path) throws Exception {
        FileInputStream fs = new FileInputStream(path);
        Scanner scanner = new Scanner(fs);
        while (scanner.hasNext()){
            String[] str = scanner.nextLine().split("#");
            DataInfo tmp = new DataInfo(str[0], str[1]);
            tmp.update();
            trainData.add(tmp);
        }
    }
    public static void writeTestFIles(String path) throws  Exception{
        FileOutputStream fos = new FileOutputStream(path);
        PrintWriter pw = new PrintWriter(fos);
        Iterator<DataInfo> it=testData.iterator();
        while (it.hasNext()) {
            pw.println(it.next().toTestString());
        }
        pw.close();
    }
    static class CalTime{
        private static long start=0;
        private static long end=0;
        public static void Start(){
            start=System.currentTimeMillis();
        }
        public static void End(){
            end=System.currentTimeMillis();
            formatDuring(end - start);
        }
        public static void formatDuring(long mss) {
            long days = mss / (1000 * 60 * 60 * 24);
            long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
            long seconds = (mss % (1000 * 60)) / 1000;
            System.out.println(days + " 天 " + hours + " 小时 " + minutes + " 分钟 "
                    + seconds + " 秒 "+mss%1000 +"毫秒");
        }
    }
}

