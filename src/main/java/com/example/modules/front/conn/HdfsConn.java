package com.example.modules.front.conn;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;

/**
 * User: lanxinghua
 * Date: 2019/3/20 11:22
 * Desc: 连接Hadoop
 */
public class HdfsConn {
    private FileSystem fileSystem = null;
    private Configuration configuration = null;
    private static final String serverUrl = "hdfs://60.205.212.196:9000/";
    private static final String HADOOP_HOME = "E:\\hadoop-2.8.3";

    //创建单例
    private static class SingletonHolder{
        private static final HdfsConn INSTANCE = new HdfsConn();
    }

    //初始化
    private HdfsConn(){
        try {
            configuration = new Configuration();
            configuration.set("fs.defaultFS", serverUrl);
            /*System.setProperty("HADOOP_USER_NAME", "hdfs");*/
            System.setProperty("HADOOP_USER_NAME", "root");
            System.setProperty("hadoop.home.dir", HADOOP_HOME);
            configuration.set("dfs.permissions", "false");
            fileSystem = FileSystem.get(configuration);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static FileSystem getFileSystem() {
        return SingletonHolder.INSTANCE.fileSystem;
    }

    public static Configuration getConfiguration() {
        return SingletonHolder.INSTANCE.configuration;
    }

    public static void main(String[] args) throws Exception{
        String dir = "/cxx/aa";
        FileSystem fs = getFileSystem();
        if (!fs.exists(new Path(dir))) {
            fs.mkdirs(new Path(dir));
        }
    }

}
