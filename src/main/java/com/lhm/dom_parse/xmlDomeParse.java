package com.lhm.dom_parse;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/*
xml文档的dom解析
 */
public class xmlDomeParse {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        //1、使用DocumentBuilderFactory对象
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        //2、创建DocumentBuilder对象
        DocumentBuilder builder = factory.newDocumentBuilder();

        //读取文件
        InputStream is = xmlDomeParse.class.getClassLoader().getResourceAsStream("pom.xml");
        //InputStream is = xmlDomeParse.class.getResourceAsStream("pom.xml");
        if (is == null) return;
        //3、获取和整个xml文档相关的Document对象
        Document document = builder.parse(is);
        System.out.println(document);//[#document: null]

        //4、获取xml文档的根元素
        Element element = document.getDocumentElement();

        //增删改查
        Element haha = document.createElement("haha");
        element.appendChild(haha);

        //将内存的Document对象写到文件中
        TransformerFactory tff = TransformerFactory.newInstance();
        Transformer tf = tff.newTransformer();
        DOMSource ds = new DOMSource(document);
        StreamResult sr = new StreamResult(new File("D:\\pom.xml"));
        tf.transform(ds,sr);


        System.out.println(element);//[project: null]

        //获取根元素的名称
        String tagName = element.getTagName();
        System.out.println("<" + tagName + " ");
        //获取根标签的属性
        NamedNodeMap deAttr = element.getAttributes();
        for (int x = 0;x < deAttr.getLength();x++){
            Attr attr = (Attr) deAttr.item(x);
            String attrName = attr.getName();
            String attrValue = attr.getValue();
            System.out.println(attrName + "=\"" + attrValue + "\" ");
        }
        //获取根标签的起始标记的 “>”
        System.out.println(">");

        //获取根标签的子元素,指的是Project元素的子元素
        NodeList nl = element.getChildNodes();
        for(int i = 0;i < nl.getLength();i++){
            Node node = nl.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE){
                System.out.println(node);
            }
        }

        //根标签的结束标签
        System.out.println("</" + tagName + ">");
    }

    public static void parse(Node node){
        //如果是Document对象，则获取根节点对象，再传递至该方法
        if (node.getNodeType() == Node.DOCUMENT_NODE){
            Document doc = (Document) node;
            Element de = doc.getDocumentElement();
            parse(de);
        }
        //如果是Element节点对象,打印节点名称，打印属性，获取子节点
        if (node.getNodeType() == Node.ELEMENT_NODE){
            Element e = (Element) node;
            String tagName = e.getTagName();
            System.out.println("<" + tagName + (e.hasAttributes()?" ":""));
            NamedNodeMap attrs = e.getAttributes();

            for (int i = 0;i < attrs.getLength();i++){
                Attr attr = (Attr) attrs.item(i);
                String attrName = attr.getName();
                String attrValue = attr.getValue();
                System.out.println(attrName + "=\"" + attrValue + "\" ");
            }
            System.out.println(">");
            NodeList nl = e.getChildNodes();
            for (int i = 0; i< nl.getLength();i++){
                Node node1 = nl.item(i);
                parse(node1);
            }
        }

        if (node.getNodeType() == Node.TEXT_NODE){
            Text text = (Text) node;
            String value = text.getWholeText();
            System.out.print(value);
        }
    }
}
