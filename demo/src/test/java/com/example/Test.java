package com.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/18.
 */
public class Test {
    private static final Map<Integer,Node> numMap = new HashMap<Integer,Node>();
    static {
        numMap.put(4,new Node(1));
        numMap.put(1,new Node(2));
        numMap.put(3,new Node(3));
        numMap.put(5,new Node(4));
        numMap.put(2,new Node(5));
        numMap.put(9,new Node(6));
        numMap.put(6,new Node(7));
        numMap.put(7,new Node(8));
    }
    public static void main(String[] args){

    }
    public static List<Integer> getEndNum(int[] nums){
        List<Integer> numList = new ArrayList<Integer>();
        if (nums.length==1) {
            numList.add(nums[0]);
            return numList;
        }else {
            int max = 0;
            int index = 0;
            for (int i = 0; i<nums.length;i++){
                if (nums[i]>max){
                    max = nums[i];
                    index = i+1;
                }
            }
            numList.add(max);
            if (max == nums[nums.length-1]){
                return numList;
            }else{
                int[] newNums = new int[nums.length-index];
                for (int i = index;i<nums.length;i++){
                    newNums[i-index] = nums[i];
                }
                numList.addAll(getEndNum(newNums));
                return numList;
            }
        }
    }
}
class Node{
    private int index;
    private int value;//权值
    private String path;
    public Node(int index){
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
