package cn.edu.ustb.spark.sparkSQL;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;

public class SparkSQL03_Env_Convert {
    public static void main(String[] args) {
        final SparkConf conf = new SparkConf().setAppName("Env_Convert").setMaster("local[*]");
        final SparkSession sparkSession = SparkSession
                .builder()
                .config(conf)
                .getOrCreate();

        //TODO 环境之间的转换
        //      Core: SparkContext -> SQL: SparkSession
        new SparkSession(new SparkContext(conf));
        //TODO  SQL : SparkSession -> SparkContext
        final SparkContext sc = sparkSession.sparkContext();
        //TODO  进一步得到Java版本的环境对象
        final JavaSparkContext jsc = new JavaSparkContext(sc);

        sparkSession.close();
    }
}
