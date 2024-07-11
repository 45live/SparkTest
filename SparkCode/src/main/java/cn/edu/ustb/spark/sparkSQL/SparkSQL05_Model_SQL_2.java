package cn.edu.ustb.spark.sparkSQL;

import org.apache.spark.sql.*;

public class SparkSQL05_Model_SQL_2 {
    public static void main(String[] args) {
        final SparkSession sparkSession = SparkSession.builder()
                .master("local[*]")
                .appName("SparkSQL")
                .getOrCreate();

        final Dataset<Row> ds = sparkSession.read().json("datas\\user.json");
        ds.createOrReplaceTempView("user");

        //SparkSQL采用特殊的方式将UDAF转换为UDF使用
        //udaf在使用时需要创建自定义聚合对象
        //  udaf方法在使用时需要传递两个参数
        //      第一个参数为UDAF的对象
        //      第二个参数为输入编码
        sparkSession.udf().register("avgAge",
                functions.udaf(new MyAvgAgeUDAF(), Encoders.LONG()));
        String sql = "select avgAge(age) from user";
        final Dataset<Row> sqlDS = sparkSession.sql(sql);

        sqlDS.show();
    }
}
