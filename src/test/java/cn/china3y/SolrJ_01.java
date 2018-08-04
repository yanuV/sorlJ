package cn.china3y;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class SolrJ_01 {


    @Test
    public void test_01() throws Exception {
        String url = "http://localhost:8080/solr/products";

        SolrServer solrServer = new HttpSolrServer(url);

        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery("product_name:家天下");
        QueryResponse response = solrServer.query(solrQuery);
        SolrDocumentList results = response.getResults();
        System.out.println("命中的文档总数为："+results.getNumFound());
        for (SolrDocument result : results) {
            System.out.println("id为"+result.get("id"));
            System.out.println("名字为"+result.get("product_name"));
            System.out.println("价格为"+result.get("product_price"));
            System.out.println("测试更新"+result.get("product_price"));
        }
    }

//    复杂查询
    @Test
    public void test_02() throws SolrServerException {
        //1.找到需要连接的solrcore
        String url = "http://localhost:8080/solr/products";
        //2.创建solrServer对象
        SolrServer solrServer = new HttpSolrServer(url);
        //3.创建查询对象
        SolrQuery solrQuery = new SolrQuery();

        //4.设置主查询，使用默认域字段查询（复制域）
        solrQuery.setQuery("家天下");

        // 5.设置过滤查询条件：FQ：Field query
            // a.查询商品类别是时尚卫浴
        solrQuery.addFilterQuery("product_cname:环保餐具");
            // b.商品价格在1--20
        solrQuery.addFilterQuery("product_price:[1 TO 2000]");

        //6.sort排序
        solrQuery.setSort("product_price",SolrQuery.ORDER.desc);
        //7.设置分页
        solrQuery.setStart(1);
        solrQuery.setRows(10);
        //8.设置显示字段
        //solrQuery.setFields("product_name,product_price");



        //9.设置高亮
            //a.开启高亮
        solrQuery.setHighlight(true);
            //b.设置高亮字段
        solrQuery.addHighlightField("product_name");
            //c.设置高亮前缀
        solrQuery.setHighlightSimplePre("<font color='red'>\"");
            //d.设置高亮后缀
        solrQuery.setHighlightSimplePost("</font>");


        // 10.设置默认查询字段。默认查询通常和主查询条件结合使用
        solrQuery.set("df", "product_keywords");


        //11.使用solrServer执行查询
        QueryResponse response = solrServer.query(solrQuery);
        //12.获取查询结果文档集
        SolrDocumentList results = response.getResults();
              //13.获取命记录数
        long numFound = results.getNumFound();
        System.out.println("命中的文档数量为："+numFound);

        //14.遍历文档集合
        for (SolrDocument result : results) {
            //15.获取id
            String id = (String) result.get("id");
            //16.获取名字
            String product_name = (String)result.get("product_name");
            //17.获取价格
            String product_price = (String) result.get("product_price");
            //18.获取描述
            String product_desc = (String)result.get("product_desc");
            //29.获取类别名
            String product_cname = (String)result.get("product_cname");
            //20.获取图片名
            String product_pic = (String)result.get("product_pic");



//            打印
            System.out.println("获取id:"+id);
            System.out.println("获取名字:"+product_name);
            System.out.println("获取价格:"+product_price);
           // System.out.println("获取描述:"+product_desc);
            System.out.println("获取类别名:"+product_cname);
            System.out.println("获取图片名:"+product_pic);

            //21.获取查询结果高亮集
            Map<String, Map<String, List<String>>> highlightingResults = response.getHighlighting();

            //22.最后获取高亮
            Map<String, List<String>> stringListMap = highlightingResults.get(id);
            List<String> strings = stringListMap.get("product_name");
            String product_nameHL = strings.get(0);

            boolean flag = product_name==product_nameHL?true:false;
            System.out.println(product_nameHL);
            System.out.println(flag);
            System.out.println("程序颜B上爽了代码");
            System.out.println("c上传了带吗");
        }


    }





}

