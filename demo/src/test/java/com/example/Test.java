package com.example;

import java.util.*;

/**
 * 求最长递增子序列
 * Created by Roc on 2016/4/18.
 */
public class Test {
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        System.out.println("输入无重复整数序列，英文逗号分割：");
        String sNum = input.next();
        String[] numList = sNum.split(",");
        int[] nums = new int[numList.length];
        Map<Integer,Node> nodeMap = new HashMap<Integer,Node>();
        //输入数组，形成map
        for (int i = 0; i < numList.length; i++){
            try {
                int num = Integer.valueOf(numList[i]);
                nums[i] = num;
                Node node = new Node();
                node.setIndex(i);
                node.setValue(num);
                nodeMap.put(num, node);
            }catch (Exception e){
                System.out.print("输入格式有误!");
            }
        }
        int length = 0;//标识当前最长节点数
        String path = "";//存放最终路径
        //获取结尾数组
        List<Integer> endList = getEndNum(nums);
        for (int i = 0; i < endList.size();i++){
            Node node = nodeMap.get(endList.get(i));
            if (node!=null){
                node.setPre(node,nums,nodeMap);
                Node newNode = getPath(node.getPreOne(),node.getPreTwo(),nums,nodeMap);
                node.setNodeNum(newNode.getNodeNum()+1);
                node.setPath(newNode.getPath()+node.getValue());
                if (node.getNodeNum()>length){
                    length = node.getNodeNum();
                    path = node.getPath();
                }
            }else {
                System.out.print("map返回null");
            }
        }
        System.out.println("最长递增子序列为：");
        System.out.print(path);
    }

    /**
     *获取所有最长递增子序列可能的结尾数
     * 思路：首先寻找数组中的最大值，若其不在最长子序列中，则只可能最长子序列的结尾数在其右侧，在右侧中寻找最大值，同理
     * @param nums
     * @return
     */
    public static List<Integer> getEndNum(int[] nums){
        List<Integer> numList = new ArrayList<Integer>();
        if (nums.length==1) {//数组中只有一个数，返回
            numList.add(nums[0]);
            return numList;
        }else {
            int max = 0;
            int index = 0;
            for (int i = 0; i<nums.length;i++){
                if (nums[i]>max){//获取数组中的最大值并记录其位置
                    max = nums[i];
                    index = i+1;
                }
            }
            numList.add(max);
            if (max == nums[nums.length-1]){//若最大值在数组末尾则结束寻找，已找到所有可能结尾。
                return numList;
            }else{//若最大值在数组中部，将右侧数组同理寻找
                int[] newNums = new int[nums.length-index];
                for (int i = index;i<nums.length;i++){
                    newNums[i-index] = nums[i];
                }
                numList.addAll(getEndNum(newNums));//递归，将最大值和右侧找出的所有最大值返回
                return numList;
            }
        }
    }

    /**
     * 求某一节点的最长路径
     * 计算该节点左侧两个可能在最长子序列的节点经过的节点数，选择经过节点最多的节点加入路径（若左侧只有一个节点计算后直接加入），递归
     * 若左侧没有节点，说明该节点为起点
     * @param one
     * @param two
     * @param nums
     * @param nodeMap
     * @return
     */
    public static Node getPath(Node one, Node two, int[]nums, Map<Integer,Node> nodeMap){//one的index始终大于two
        if (one==null&&two==null){//左侧没有节点，该点为起点
            Node node = new Node();
            node.setNodeNum(0);
            node.setPath("");
            return node;
        }
        //距该节点最近左节点路径计算
        one.setPre(one,nums,nodeMap);
        Node newOne = getPath(one.getPreOne(),one.getPreTwo(),nums,nodeMap);
        one.setNodeNum(newOne.getNodeNum()+1);
        one.setPath(newOne.getPath()+one.getValue()+"-");
        if (two==null){//该点左侧只有一个节点
            return one;
        }else {
            //距该节点次近左节点路径计算
            two.setPre(two,nums,nodeMap);
            Node newTwo = getPath(two.getPreOne(),two.getPreTwo(),nums,nodeMap);
            if (newOne.getNodeNum()>=newTwo.getNodeNum()){//比较经过的节点数，该逻辑导致了若存在多条等长子序列只会显示一条。
                /**
                 * 改进思路：将该方法增加选择标识，用作标识相等节点数时选哪一个，将出现相等情况的地方也坐上标识，比如将连接符“-”改为“+”
                 * 若结果里某条路径中含有‘+’，则进行处理，处理后返回其衍生的等长数组，也是递归
                 */
                return one;
            }else{
                two.setNodeNum(newTwo.getNodeNum() + 1);
                two.setPath(newTwo.getPath()+two.getValue()+"-");
                return two;
            }
        }
    }
}
class Node{
    private int index;
    private int value;
    private int nodeNum;//权值
    private String path;
    private Node preOne;//该节点左侧第一个小于它的节点
    private Node preTwo;//该节点左侧第二个小于它的节点

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getNodeNum() {
        return nodeNum;
    }

    public void setNodeNum(int nodeNum) {
        this.nodeNum = nodeNum;
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

    public Node getPreOne() {
        return preOne;
    }

    public void setPreOne(Node preOne) {
        this.preOne = preOne;
    }

    public Node getPreTwo() {
        return preTwo;
    }

    public void setPreTwo(Node preTwo) {
        this.preTwo = preTwo;
    }

    /**
     * 设置左侧两个小于其的节点
     * 若该节点在在最长子序列中，则其左侧离其最近的且值小于它两个节点中必有一个在最长子序列
     * 若最近的值大于次近的，则最近的一定在最长子序列，所以设次近的为null，若最近的小于次近的，则二者皆有可能
     * @param node
     * @param nums
     * @param nodeMap
     */
    public void setPre(Node node,int[] nums,Map<Integer,Node> nodeMap){
        for (int i = node.getIndex()-1;i>-1;i--){
            if(node.getValue()>nums[i]){
                if (node.getPreOne() == null){
                    node.setPreOne(nodeMap.get(nums[i]));
                }else if (node.getPreOne().getValue()<nums[i]){
                    node.setPreTwo(nodeMap.get(nums[i]));
                    break;
                }
            }
        }
    }
}
