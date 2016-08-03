package com.web.volunteer.util;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/7/22.
 */
public class CrawlerUtil {
    public static String sendGet(String url)
    { // 定义一个字符串用来存储网页内容
        String result = "";
        // 定义一个缓冲字符输入流
        BufferedReader in = null;
        try
        {
            // 将string转成url对象
            URL realUrl = new URL(url);
            // 初始化一个链接到那个url的连接
            URLConnection connection = realUrl.openConnection();
            // 开始实际的连接
            connection.setRequestProperty("Cookie","aliyungf_tc=AQAAAEWURz2N9QEAfs8lalCeWdx12NiP; auth=49480391%0927fca15173264b14d481d969e141cf88; CNZZDATA1257784543=409698007-1469084077-http%253A%252F%252Fwap50.yuanyula.com%252F%7C1469089477");
            connection.connect();
            // 初始化 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            // 用来临时存储抓取到的每一行的数据
            String line;
            while ((line = in.readLine()) != null)
            {
                // 遍历抓取到的每一行并将其存储到result里面
                result += line;
            }
        } catch (Exception e)
        {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        } // 使用finally来关闭输入流
        finally
        {
            try
            {
                if (in != null)
                {
                    in.close();
                }
            } catch (Exception e2)
            {
                e2.printStackTrace();
            }
        }
        return result;
    }

    public static String RegexString(String targetStr, String patternStr)
    {
        // 定义一个样式模板，此中使用正则表达式，括号中是要抓的内容
        // 相当于埋好了陷阱匹配的地方就会掉下去
        Pattern pattern = Pattern.compile(patternStr);
        // 定义一个matcher用来做匹配
        Matcher matcher = pattern.matcher(targetStr);
        // 如果找到了
        if (matcher.find())
        {
            // 打印出结果
            return matcher.group(1);
        }
        return "";
    }

    public static void savePic(String url,String name){
        try
        {
            String[] ss = url.split("\\.");
            url = url.substring(0, url.length() - ss[ss.length - 1].length() - ss[ss.length - 2].length() + 2);
            // 将string转成url对象
            URL realUrl = new URL(url);
            // 初始化一个链接到那个url的连接
            URLConnection connection = realUrl.openConnection();
            // 开始实际的连接
            connection.setConnectTimeout(5 * 1000);
            connection.connect();
            // 初始化 BufferedReader输入流来读取URL的响应
            InputStream inStream = connection.getInputStream();
            //得到图片的二进制数据，以二进制封装得到数据，具有通用性
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            //创建一个Buffer字符串
            byte[] buffer = new byte[1024];
            //每次读取的字符串长度，如果为-1，代表全部读取完毕
            int len = 0;
            //使用一个输入流从buffer里把数据读取出来
            while( (len=inStream.read(buffer)) != -1 ){
                //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
                outStream.write(buffer, 0, len);
            }
            //关闭输入流
            inStream.close();
            byte[] data = outStream.toByteArray();
            //new一个文件对象用来保存图片，默认保存当前工程根目录
            File imageFile = new File("E:\\zhenaiba\\"+name+".jpg");
            //创建输出流
            FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
            //写入数据
            fileOutputStream.write(data);
            //关闭输出流
            fileOutputStream.close();
            outStream.close();
        } catch (Exception e)
        {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
    }
}
