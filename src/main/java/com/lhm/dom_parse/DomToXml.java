package com.lhm.dom_parse;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.InputStream;

/*
使用DOM来解析XML文档
 */
public class DomToXml {
    public static void main(String[] args) throws Exception {
        //创建文档创建器
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        //创建DOM文档
        InputStream is = DomToXml.class.getResourceAsStream("/pom.xml");
        if (is == null){
            throw new RuntimeException("流文件为null。。。。。");
        }
        Document document = builder.parse(is);
        System.out.println(document);//[#document: null]

        //获取xml文件的根元素
        Element rootElement = document.getDocumentElement();
        System.out.println(rootElement);//[project: null]

        parse(rootElement);
    }

    //将内存中的Document对象写到文件中
    public static void WriterXmlToFile(Document document) throws TransformerException {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new File("D:\\code\\a.xml"));
        transformer.transform(source,result);
    }

    //将节点标签传入该方法中
    public static void parse(Node node){
        //如果是Document节点对象，则获得根节点对象，再传递至该方法
        if (node.getNodeType() == Node.DOCUMENT_NODE){
            //将该节点对象转为文档节点对象
            Document doc = (Document) node;
            //获取该文档节点对象的根节点
            Element rootElement = doc.getDocumentElement();
            //将这个根节点对象传递到此方法中
            parse(rootElement);
        }

        //如果node是Element节点对象，打印节点名称，打印属性，获取其子节点
        if (node.getNodeType() == Node.ELEMENT_NODE){
            //将node对象强转为Element对象
            Element element = (Element) node;
            //获取该节点元素的名称
            String tagName = element.getTagName();

            //判断该节点是否含有属性
            boolean flag = element.hasAttributes();

            //输出节点名称，如果该节点有属性，那么节点名称后面加上空格
            System.out.print("<" + tagName + (flag?" ":""));

            //输出包含该节点的属性
            if (flag){
                //得到节点属性对象
                NamedNodeMap attrs = element.getAttributes();
                int length = attrs.getLength();
                for (int i = 0; i < length;i++){
                    Attr attr = (Attr)attrs.item(i);
                    String attrName = attr.getName();
                    String attrValue = attr.getValue();
                    System.out.print(attrName + "=\"" +attrValue + "\"" + (i == length-1?"":" "));
                }
            }
            System.out.print(">");

            //获得该根节点的所有子节点,，包括文本节点
            NodeList nl = element.getChildNodes();
            for(int i = 0; i < nl.getLength();i++){
                Node node1 = nl.item(i);
                //将该节点作为子根节点，作为此方法的参数
                parse(node1);
            }
            System.out.print("</" + tagName + ">");
        }

        //如果该内容是文本内容，不是属性内容，那么也要打印出来
        if (node.getNodeType() == Node.TEXT_NODE){
            //将该节点转为文本节点
            Text text = (Text) node;
            //返回 Text节点的逻辑相邻文本节点的所有文本到此节点，以文档顺序连接。
            String value = text.getWholeText();
            System.out.print(value);
        }
    }
}
