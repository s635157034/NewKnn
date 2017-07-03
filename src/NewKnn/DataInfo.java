package NewKnn;

/**
 * Created by root on 17-6-26.
 */
public class DataInfo {
    double[] point=null;
    String label=null;
    String mostLabel=null;
    private double weight=0;
    private int right=0;
    private int tmpRight=0;
    private int wrong=0;
    private int tmpWrong=0;

    public DataInfo(String input) {
        String[] str = input.split(",");
        int len=str.length;
        label=str[len-1];
        point=new double[len-1];
        for(int i=0;i<len-1;i++){
            point[i] = Double.parseDouble(str[i]);
        }
    }
    public DataInfo(String input,String weight){
        this(input);
        String[] str=weight.split(",");
        right=Integer.valueOf(str[0]);
        wrong=Integer.valueOf(str[1]);
    }

    public static double Outh(double[] testData, double[] inData) {
        float distance =0.0f;
        int len=testData.length;
        for(int i=0;i<len;i++){
            distance += (testData[i]-inData[i])*(testData[i]-inData[i]);
        }
        return Math.sqrt(distance);
    }

    public double getWeight() {
        return weight;
    }

    public DistanceInfo calDistance(DataInfo dataInfo, double maxDistance) {
        double distance = Outh(point, dataInfo.point);
        if (distance < maxDistance) {
            return new DistanceInfo(dataInfo, dataInfo.label, distance);
        } else return null;
    }

    //更新正确和错误次数
    public void update(){
        right+=tmpRight;
        tmpRight=0;
        wrong+=tmpWrong;
        tmpWrong=0;
        calWeight();
    }
    public void addRight(){tmpRight++;}
    public void addWrong(){tmpWrong++;}

    public String toTrainString() {
        StringBuffer str = new StringBuffer();
        int len = point.length;
        for(int i=0;i<len;i++){
            str.append(point[i]);
            str.append(",");
        }
        str.append(label).append("#");
        str.append(right).append(",").append(wrong);
        return str.toString();
    }
    public String toTestString() {
        StringBuffer str = new StringBuffer();
        int len = point.length;
        for(int i=0;i<len;i++){
            str.append(point[i]);
            str.append(",");
        }
        str.append(label+"#"+mostLabel);
        return str.toString();
    }

    /*
     * 权重函数
     */
    private void calWeight(){
        // TODO: 17-6-27 修改权重函数 
        weight = Math.atan((1 + right) / (1 + wrong)-1);
    }
}

class DistanceInfo implements Comparable{
    DataInfo dataInfo;
    String label;
    double distance;

    public DistanceInfo(DataInfo dataInfo, String label, double distance) {
        this.dataInfo = dataInfo;
        this.label = label;
        this.distance = distance;
    }

    public double getWeight() {
        return dataInfo.getWeight();
    }

    @Override
    public int compareTo(Object o) {
        return label.compareTo(((DistanceInfo)o).label);
    }
}