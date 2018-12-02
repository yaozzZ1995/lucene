package com.itheima.lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;

/**
 * Date: 2018/11/30
 * Time: 15:47
 */
public class QueryIndex {
    //查询所有的方法
    @Test
    public void queryAll() throws Exception {
        //创建查询所有索引库的数据
        Query query = new MatchAllDocsQuery();
        doQuery(query);
    }

    //根据字条查询数据
    @Test
    public void queryByTerm() throws Exception {
        Query query = new TermQuery(new Term("fileName","spring"));
        doQuery(query);
        String[] arr={"1"};

    }

    //根据数值查询范围  查询1--50字节内的范围文档
    @Test
    public void queryByRange()throws Exception{
        Query query = NumericRangeQuery.newLongRange("fileSize",1L,50L,true,true);
        doQuery(query);
    }

    //组合查询
    @Test
    public void queryByBoolean() throws Exception{

        //Query query0 = new TermQuery(new Term("fileName","传智播客"));
        Query query1 = new TermQuery(new Term("fileContent","传智播客"));
        //Query query2 = new TermQuery(new Term("fileName","不明觉厉"));
        Query query3 = NumericRangeQuery.newLongRange("fileSize",1L,50L,true,true);
        BooleanQuery query = new BooleanQuery();
        //query.add(query0, BooleanClause.Occur.SHOULD);
        query.add(query1, BooleanClause.Occur.MUST);
        //query.add(query2, BooleanClause.Occur.SHOULD);
        query.add(query3, BooleanClause.Occur.MUST);
        doQuery(query);
    }


    //分词查询
    @Test
    public void queryByParser() throws Exception{
        //定义输入的查询关键短语
        String searchStr = "传智播客的spring全家桶不明觉厉";
        //创建分词的解析对象
        QueryParser parser = new QueryParser(Version.LUCENE_47,"fileName", new IKAnalyzer());
        //解析查询字符串获取查询对象
        Query query = parser.parse(searchStr);
        doQuery(query);
    }

    //多域字段分词查询
    @Test
    public void queryByMultiParser()throws Exception{
        //定义输入的查询关键短语
        String searchStr = "传智播客的spring全家桶不明觉厉";
        //创建分词的解析对象
        String[] fileds = {"fileName","fileContext"};
        MultiFieldQueryParser parser = new MultiFieldQueryParser(Version.LUCENE_47,fileds, new IKAnalyzer());
        //解析查询字符串获取对象
        Query query = parser.parse(searchStr);
        doQuery(query);
    }

    //查询方法
    private void doQuery(Query query) throws IOException {
        FSDirectory fsDirectory = FSDirectory.open(new File("E:\\luceneIndex"));
        //创建索引库的读取对象 用于读取索引库目录
        IndexReader reader = DirectoryReader.open(fsDirectory);
        //创建索引库的检索对象,用于检索索引数据
        IndexSearcher indexSearcher = new IndexSearcher(reader);

        //参数1 为需要的查询对象
        //参数2 为查询下的记录数量
        TopDocs topDocs = indexSearcher.search(query, 100);
        System.out.println("总共命中文档的数量: " + topDocs.totalHits);
        //获取所有 文档的id和积分
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            System.out.println("文档的id: " + scoreDoc.doc);
            System.out.println("文档的得分: " + scoreDoc.score);
            //通过文档id提取document对象
            Document document = indexSearcher.doc(scoreDoc.doc);
            System.out.println("文档的名为：=======" + document.get("fileName"));
            System.out.println("文档内容为：===========" + document.get("fileContent"));
            System.out.println("文档的大小为：=======" + document.get("fileSize"));
            System.out.println("路径为：======" + document.get("filePath"));
        }
    }
}
