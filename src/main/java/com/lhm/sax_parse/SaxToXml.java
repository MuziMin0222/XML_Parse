package com.lhm.sax_parse;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;

/*
使用SAX来解析XML文件
 */
public class SaxToXml {
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        //1、获取SAXParserFactory对象
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);

        //2、获取SAXParser对象
        SAXParser parser = factory.newSAXParser();

        //3、解析
        parser.parse(
                SaxToXml.class.getResourceAsStream("/Student.xml"),
                //DefaultHander是一个适配器类，重写方法需要alt+insert
                new DefaultHandler(){
                    @Override
                    public void startDocument() throws SAXException {
                        System.out.println("开始解析文档。。。。");
                    }

                    @Override
                    public void endDocument() throws SAXException {
                        System.out.println("结束解析文档。。。。");
                    }

                    @Override
                    public void startElement(
                            String uri,//当前标签的命名空间的内容
                            String localName,//标签名
                            String qName,//带命名空间的标签名
                            Attributes attributes//当前标签的属性
                    ) throws SAXException {
                        int length = attributes.getLength();
                        System.out.print("<" + qName + (length == 0?"":" "));

                        for (int i = 0; i < length; i++) {
                            String attributesQName = attributes.getQName(i);
                            String attributesValue = attributes.getValue(i);
                            System.out.print(attributesQName + "=\"" + attributesValue + "\"");
                        }
                        System.out.print(">");
                    }

                    @Override
                    public void endElement(String uri, String localName, String qName) throws SAXException {
                        System.out.print("</" + qName + ">");
                    }

                    //每次遇到字符就会调用此方法，空格也是字符，也会调用
                    //ch是传回来的字符数组，其包含从开始解析到当前这一段文本，包含着所有的元素内容
                    //start和 length分别是当前文本在数组中的开始位置和结束位置 。
                    @Override
                    public void characters(char[] ch, int start, int length) throws SAXException {
                        System.out.print(new String(ch,start,length));
                    }
                }
        );
    }
}
