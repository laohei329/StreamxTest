package com.atguigu.bigdata;


import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.functions.source.SocketTextStreamFunction;
import org.apache.flink.streaming.api.scala.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;
import scala.Char;

/**
 * @Author lzc
 * @Date 2022/3/24 15:28
 */
public class UnboundedWC {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        DataStreamSource<String> streamSource = env.socketTextStream("10.10.101.62", 9999);


        SingleOutputStreamOperator<String> stringSingleOutputStreamOperator = streamSource.flatMap(new FlatMapFunction<String, String>() {
            public void flatMap(String value, Collector<String> out) throws Exception {
                String[] strs = value.split(" ");
                for (String str : strs) {
                    out.collect(str);
                }
            }
        });
        stringSingleOutputStreamOperator.print();

//                .flatMap(new FlatMapFunction<String, String>() {
//                    @Override
//                    public void flatMap(String line,
//                                        Collector<String> out) throws Exception {
//                        for (String word : line.split(" ")) {
//                            out.collect(word);
//                        }
//                    }
//                })
//                .map(new MapFunction<String, Tuple2<String, Long>>() {
//                    @Override
//                    public Tuple2<String, Long> map(String word) throws Exception {
//                        return Tuple2.of(word, 1l);
//                    }
//                })
//                .keyBy(new KeySelector<Tuple2<String, Long>, String>() {
//                    @Override
//                    public String getKey(Tuple2<String, Long> t) throws Exception {
//                        return t.f0;  // t._1
//                    }
//                })
//                .sum(1)


        env.execute();

    }
}
