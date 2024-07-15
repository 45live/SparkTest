package cn.edu.ustb.spark.sparkStreaming;

import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

public class SparkStreaming06_Close {
    public static void main(String[] args) throws InterruptedException {
        final SparkConf conf = new SparkConf()
                .setMaster("local[*]")
                .setAppName("Close");

        final JavaStreamingContext jsc = new JavaStreamingContext(conf, new Duration(3000));

        final JavaReceiverInputDStream<String> socketDS = jsc.socketTextStream("localhost", 9999);
        socketDS.print();

        jsc.start();

        //close就是用于关闭资源，但是不能在主线程中关闭 => 换个线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                //关闭环境，释放资源
                try {
                    //因为刚开启就关闭显然不适合，所以等到开启3s（一个采集周期）后再关闭
                    Thread.sleep(3000L);
                    jsc.close();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        //表示当前的代码执行到此处后，会让主线程阻塞
        //  直到采集器终止后，才会继续执行下面的代码
        jsc.awaitTermination();

        //用于释放资源
        //jsc.close(); (X)
    }
}