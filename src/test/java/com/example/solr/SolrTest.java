package com.example.solr;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.collections.Maps;

import java.util.*;

/**
 * User: lanxinghua
 * Date: 2019/8/23 18:20
 * Desc: solr测试
 */
public class SolrTest {
    private static final Logger logger = LoggerFactory.getLogger("Solr");
    private static final String SOLR_URL = "http://localhost:8983/solr/";
    private static final String SOLR_CORE = "test";

    public static void main(String[] args) {
//        test01();
//        test02();
        test04();
    }

    public static void test01(){
        // 1.测试插入文档
        Map<String, String> map = Maps.newHashMap();
        map.put("id", "00001");
        map.put("name", "蓝星花1");
        map.put("age", "20");
        map.put("address", "杭州市拱墅区");
        addDocument(map, SOLR_CORE);
    }

    public static void test02(){
        // 2.bean插入文档
        List<Person> list = Lists.newArrayList();
        list.add(new Person("00002", "user2", 20, "杭州2"));
        list.add(new Person("00003", "user3",20, "杭州3"));
        list.add(new Person("00004", "user4", 20, "杭州4"));
        addDocumentByBean(list, SOLR_CORE);
    }

    public static void test03(){
        // 3.根据id集合删除索引
        List<String> ids = Lists.newArrayList();
        ids.add("00001");
        ids.add("00002");
        ids.add("00003");
        ids.add("00004");
        deleteDocumentByIds(ids, SOLR_CORE);
    }

    public static void test04(){
        // 4.查询
        getDocument(SOLR_CORE);
    }

    public static void test05(){
        // 5.spell测试
        getSpell(SOLR_CORE);
    }

    /**
     * 添加文档
     * @param map
     * @param core
     */
    private static void addDocument(Map<String, String> map, String core){
        try {
            SolrInputDocument sid = new SolrInputDocument();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                sid.addField(entry.getKey(), entry.getValue());
            }
            HttpSolrClient solrClient = getSolrClient(core);
            solrClient.add(sid);
            commitAndCloseSolr(solrClient);
            logger.info("addDocument core:{} data:{}", core, JSON.toJSONString(map));

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 添加文档
     * @param core
     */
    private static void addDocumentByBean(List<Person> peoples, String core){
        try {
            HttpSolrClient solrClient = getSolrClient(core);
            solrClient.addBeans(peoples);
            commitAndCloseSolr(solrClient);
            logger.info("addDocumentByBean core:{} data:{}", core, JSON.toJSONString(peoples));

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 根据id删除索引
     * @param ids
     * @param core
     */
    private static void deleteDocumentByIds(List<String> ids, String core){
        try {
            HttpSolrClient solrClient = getSolrClient(core);
            solrClient.deleteById(ids);
            commitAndCloseSolr(solrClient);
            logger.info("deleteDocumentByIds core:{} data:{}", core, JSON.toJSONString(ids));

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 获取文档
     * @param core
     */
    private static void getDocument(String core){
        try {
            HttpSolrClient solrClient = getSolrClient(core);
            SolrQuery query = new SolrQuery();
            // q查询
            query.set("q", "id:00003");

            // filter查询
            query.addFilterQuery("id:[0 TO 00003]");

            // 排序
            query.setSort("id", SolrQuery.ORDER.desc);

            // 分页
            query.setStart(0);
            query.setRows(1);

            // 设置高亮
            query.setHighlight(true);

            // 设置高亮字段
            query.addHighlightField("name");

            // 设置高亮样式
            query.setHighlightSimplePre("<font color='red'>");
            query.setHighlightSimplePost("</font>");

            QueryResponse result = solrClient.query(query);
            // 1.获取查询的条数
            logger.info("--------------------第一种方式--------------------");
            SolrDocumentList results = result.getResults();
            logger.info("一共查询到{}条记录", results.getNumFound());
            for (SolrDocument solrDocument : results) {
                System.out.println("id:" + solrDocument.get("id"));
                System.out.println("name:" + solrDocument.get("name"));
                System.out.println("age:" + solrDocument.get("age"));
                System.out.println("address:" + solrDocument.get("address"));
            }

            // 2.获取对象信息,需要传入对应对象的类class
            logger.info("--------------------第二种方式--------------------");
            List<Person> peoples = result.getBeans(Person.class);
            logger.info("一共查询到{}条记录", peoples.size());
            for (Person people : peoples) {
                System.out.println(JSON.toJSONString(people));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 查询使用spell接口，输入错误，solr可以给出建议词
     * @param core
     */
    private static void getSpell(String core){
        try {
            HttpSolrClient solrClient = getSolrClient(core);
            SolrQuery sq = new SolrQuery();
            sq.set("qt", "/spell");
            // 原本是lisi，这里拼写错误，测试solr返回建议词语
            sq.set("q", "liss");
            QueryResponse query = solrClient.query(sq);
            SolrDocumentList results = query.getResults();
            // 获取查询条数
            long count = results.getNumFound();

            // 判断是否查询到
            if (count == 0) {
                SpellCheckResponse spellCheckResponse = query.getSpellCheckResponse();
                List<SpellCheckResponse.Collation> collatedResults = spellCheckResponse.getCollatedResults();
                for (SpellCheckResponse.Collation collation : collatedResults) {
                    long numberOfHits = collation.getNumberOfHits();
                    System.out.println("建议条数为:" + numberOfHits);
                    List<SpellCheckResponse.Correction> misspellingsAndCorrections = collation.getMisspellingsAndCorrections();
                    for (SpellCheckResponse.Correction correction : misspellingsAndCorrections) {
                        String source = correction.getOriginal();
                        String current = correction.getCorrection();
                        System.out.println("推荐词语为：" + current + "原始的输入为：" + source);
                    }
                }
            } else {
                for (SolrDocument solrDocument : results) {
                    // 获取key集合
                    Collection<String> fieldNames = solrDocument.getFieldNames();
                    // 根据key集合输出value
                    for (String field : fieldNames) {
                        System.out.println("key: " + field + ",value: " + solrDocument.get(field));
                    }
                }
            }
            // 关闭连接
            commitAndCloseSolr(solrClient);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 获取Solr服务
     * @param core
     * @return
     */
    private static HttpSolrClient getSolrClient(String core){
        return new HttpSolrClient(SOLR_URL + core);
    }

    /**
     * 提交并关闭服务
     * @param client
     */
    private static void commitAndCloseSolr(HttpSolrClient client){
        try {
            client.commit();
            client.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
