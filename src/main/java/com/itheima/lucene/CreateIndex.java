package com.itheima.lucene;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;

/**
 * Date: 2018/11/30
 * Time: 14:44
 */
public class CreateIndex {

    @Test
    public void createIndex() throws IOException {

        File targetFile = new File("E:\\luceneIndex");
        //索引库写入的磁盘目录
        Directory directory = FSDirectory.open(targetFile);
        //索引库写入文档用到的分词器   标准分词器 中文只能一个字的分
        //StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_47);
        Analyzer analyzer = new IKAnalyzer();
        //索引库的配置对象
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_47, analyzer);
        //(Directory d, IndexWriterConfig conf)
        //使用lucene的对象写入到索引库
        IndexWriter writer = new IndexWriter(directory, config);

        File dir = new File("E:\\searchsource");
        //获取本地磁盘文件目录下的所有文件
        File[] files = dir.listFiles();
        //遍历读取数据
        for (File file : files) {
            System.out.println("文件名称为:======" + file.getName());
            System.out.println("文件的内容为:======" + FileUtils.readFileToString(file));
            System.out.println("文档的大小为:======" + file.length());
            System.out.println("文件的路径为:======" + file.getAbsolutePath());
            //使用当前的文件创建Document对象
            Document doc = new Document();
            /*StringField 特点：不分词存储 支持索引查询
             * TextField 特点：  分词存储   支持索引查询
             * LongField  特点： 数值类型  分词 索引
             * Store.YES 表示数据被存储在索引库
             * Store.NO 表示支持索引查询 数据不会存储
             * 如果数据需要查询后使用 必须YES存储
             * */
            doc.add(new TextField("fileName", file.getName(), Field.Store.YES));
            doc.add(new TextField("fileContent", FileUtils.readFileToString(file), Field.Store.YES));
            doc.add(new LongField("fileSize", file.length(), Field.Store.YES));
            doc.add(new StringField("filePath", file.getAbsolutePath(), Field.Store.YES));

            //调用写入对象的方法
            writer.addDocument(doc);

        }
        writer.commit();
        writer.close();
    }
}
