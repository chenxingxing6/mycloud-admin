package com.example.canal;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.common.utils.AddressUtils;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * User: lanxinghua
 * Date: 2019/8/22 16:24
 * Desc: canal测试
 */
public class SimpleCanalTest {
    private static final Logger logger = LoggerFactory.getLogger("canal");

    public static void main(String[] args) {
        test01();
    }

    public static void test01(){
        new Thread(() -> {
            // 创建连接
            SocketAddress socketAddress = new InetSocketAddress(AddressUtils.getHostIp(), 11111);
            CanalConnector connector = CanalConnectors.newSingleConnector(socketAddress, "example", "", "");

            int batchSize = 1000;
            try {
                System.out.println("监听......");
                connector.connect();
                connector.subscribe(".*\\..*");
                connector.rollback();
                while (true){
                    // 获取指定的数据
                    Message msg = connector.getWithoutAck(batchSize);
                    long batchId = msg.getId();
                    int size = msg.getEntries().size();
                    if (batchId != -1 && size > 0){
                        printEntry(msg.getEntries());
                    }
                    connector.ack(batchId);
                    TimeUnit.SECONDS.sleep(1);
                }
            }catch (Exception e){
                logger.error("线程异常", e);
            }finally {
                connector.disconnect();
            }
        }).start();
    }

    private static void printEntry(List<CanalEntry.Entry> entrys) {
        for (CanalEntry.Entry entry : entrys) {
            // 操作事务忽略
            if (entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONBEGIN || entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONEND) {
                continue;
            }
            System.out.println(entry.toString());

            CanalEntry.RowChange rowChage = null;  // 执行事件信息
            String database = null; // 数据库
            String table = null;    // 执行的表
            try {
                rowChage = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
                database = entry.getHeader().getSchemaName();
                table = entry.getHeader().getTableName();
            } catch (Exception e) {
                logger.error("获取数据失败", e);
            }

            // 获取执行的事件
            CanalEntry.EventType eventType = rowChage.getEventType();
            for (CanalEntry.RowData rowData : rowChage.getRowDatasList()) {
                // 删除
                if (eventType == CanalEntry.EventType.DELETE) {
                    delete(rowData.getBeforeColumnsList(), database, table);
                }
                // 新增
                else if (eventType == CanalEntry.EventType.INSERT) {
                    insert(rowData.getBeforeColumnsList(), database, table);
                }
                // 修改
                else if (eventType == CanalEntry.EventType.UPDATE){
                    update(rowData.getBeforeColumnsList(), database, table);
                }
                // 改表结构
                else if (eventType == CanalEntry.EventType.ALTER){
                    System.out.println("修改表结构");
                }
                else {

                }
            }
        }
    }

    /**
     * 数据库执行了添加操作
     * @param columns
     * @param database
     * @param table
     */
    private static void insert(List<CanalEntry.Column> columns, String database, String table){
        JSONObject json=new JSONObject();
        for (CanalEntry.Column column : columns) {
            json.put(column.getName(), column.getValue());
        }
        System.out.println("数据库："+database+"==>表："+table+"==>添加数据："+JSON.toJSONString(json));
    }

    /**
     * 数据库执行了修改操作
     * @param columns
     * @param database
     * @param table
     */
    private static  void update( List<CanalEntry.Column> columns,String database,String table){
        JSONObject json=new JSONObject();
        for (CanalEntry.Column column : columns) {
            json.put(column.getName(), column.getValue());
        }
        System.out.println("数据库："+database+"==>表："+table+"==>修改数据："+JSON.toJSONString(json));
    }

    /**
     * 数据库执行了删除操作
     * @param columns
     * @param database
     * @param table
     */
    private static  void delete( List<CanalEntry.Column> columns,String database,String table){
        JSONObject json=new JSONObject();
        for (CanalEntry.Column column : columns) {
            json.put(column.getName(), column.getValue());
        }
        System.out.println("数据库："+database+"==>表："+table+"==>删除数据："+JSON.toJSONString(json));
    }
}
