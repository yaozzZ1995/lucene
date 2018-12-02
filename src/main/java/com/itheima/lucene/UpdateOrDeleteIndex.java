package com.itheima.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;

/**
 * Date: 2018/11/30
 * Time: 16:59
 */
public class UpdateOrDeleteIndex {
    //修改索引库
    @Test
    public void updateIndex() throws Exception {
        //索引库写入的磁盘目录
        FSDirectory directory = FSDirectory.open(new File("E:\\luceneIndex"));
        //分词器
        Analyzer analyzer = new IKAnalyzer();
        //索引库配置对象
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_47, analyzer);
        //修改索引库
        IndexWriter writer = new IndexWriter(directory, config);
        Document document = new Document();
        document.add(new TextField("fileName", "test", Field.Store.YES));
        writer.updateDocument(new Term("fileName", "spring.txt"), document);

        writer.commit();
        writer.close();
    }

    //修改索引库
    @Test
    public void delete() throws Exception {
        //索引库写入的磁盘目录
        FSDirectory directory = FSDirectory.open(new File("E:\\luceneIndex"));
        //分词器
        Analyzer analyzer = new IKAnalyzer();
        //索引库配置对象
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_47, analyzer);
        //修改索引库
        IndexWriter writer = new IndexWriter(directory, config);




        //删除一条
        writer.deleteDocuments(new Term("fileName","test"));
        //writer.deleteAll();

        writer.close();
    }
}
