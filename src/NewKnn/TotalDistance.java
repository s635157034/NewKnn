package NewKnn;

import java.util.Arrays;

/**
 * Created by root on 17-6-26.
 */
public class TotalDistance {
    DistanceInfo[] total = new DistanceInfo[KnnMain.K];
    double maxDistance = -1;
    int now = 0;//
    String mostLabel=null;

    public double add(DistanceInfo distanceInfo) {
        if (now < KnnMain.K-1) {
            total[now] = distanceInfo;
            now++;
            return 99999;
        } else if (now == KnnMain.K-1) {
            total[now] = distanceInfo;
            now++;
        } else {
            int max = getMaxDistance();
            total[max] = distanceInfo;
        }
        return total[getMaxDistance()].distance;
    }

    public int getMaxDistance() {
        maxDistance = total[0].distance;
        int max = 0;
        for (int i = 1; i < now; i++) {
            if (maxDistance < total[i].distance) {
                maxDistance = total[i].distance;
                max = i;
            }
        }
        return max;
    }

    public String getLabel(int weight) {
        // TODO: 17-6-26 添加权重部分 
        Arrays.sort(total);
        double count = 0;
        double longest = 0;
        if (weight > 0) {
            for (int i = 0; i < KnnMain.K-1; i++) {
                if (total[i].label.equals(total[i+1].label)) {
                    if (weight == 1) {
                        count += total[i].getWeight() + 1.57;
                    } else if (weight == 2) {
                        if (total[i].distance == 0) {
                        } else count += 1 / Math.sqrt(total[i].distance);
                    }
                } else {
                    count = 0;// 如果不等于，就换到了下一个数，那么计算下一个数的次数时，count的值应该重新赋值为一
                    continue;
                }
                if (count > longest) {
                    mostLabel = total[i].label;
                    longest = count;
                }
            }
        }else {
            for (int i = 0; i < KnnMain.K-1; i++) {
                if (total[i].label.equals(total[i+1].label)) {
                    count++;
                } else {
                    count = 0;// 如果不等于，就换到了下一个数，那么计算下一个数的次数时，count的值应该重新赋值为一
                    continue;
                }
                if (count > longest) {
                    mostLabel = total[i].label;
                    longest = count;
                }
            }
        }

        return mostLabel;
    }
    public void addWeight(String label){
        for (int i = 0; i < KnnMain.K; i++) {
            if (total[i].distance == 0)
                continue;
            if(label.equals(total[i].label)){
                total[i].dataInfo.addRight();
            }else {
                total[i].dataInfo.addWrong();
            }
        }
    }
}
