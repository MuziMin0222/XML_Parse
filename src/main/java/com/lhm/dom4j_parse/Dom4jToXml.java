package com.lhm.dom4j_parse;

import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.util.List;

/*
使用DOM4J来解析xml文件
 */
public class Dom4jToXml {
    public static void main(String[] args) throws DocumentException {
        //创建SAXReader对象
        SAXReader reader = new SAXReader();

        //读取整个XML文档加载到内存中
        Document document = reader.read(Dom4jToXml.class.getResourceAsStream("/Student.xml"));

        //获取根节点
        Element rootElement = document.getRootElement();
        //String name = rootElement.getName();//获得根标签的名字
        //获取到带命名空间的标签名
        QName qName = rootElement.getQName();
        //获得根标签的名字
        String name = qName.getName();
        System.out.print("<" + name);

        //获取根节点的属性
        List attrs = rootElement.attributes();
        if (attrs.size() != 0){
            attrs.forEach(o->{
                Attribute attr = (Attribute) o;
                String attrName = attr.getName();
                String attrValue = attr.getValue();
                System.out.print(attrName + "\"" + attrValue + "\" ");
            });
        }
        System.out.print(">");

        //获取某个节点的所有Element子节点
        //处理根节点的子节点
        List elements = rootElement.elements();
        elements.forEach(o->{
            //该element就是student
            Element element = (Element) o;
            String ele_name = element.getQName().getName();
            System.out.println("<" + ele_name + ">");
            //包含子节点的子节点 ...的所有的文本值 )
            System.out.println(element.getStringValue());

            //获取该节点的所有子节点
            List<Element> childElement = element.elements();
            childElement.forEach(o1->{
                String childName = o1.getQName().getName();
                System.out.print("<" + childName + ">");

                //不递归，将不会获取子元素的文本值
                String text = o1.getText();
                //会递归，这里会获取此节点及其所有子节点的文本值
                //String stringValue = o1.getStringValue();
                System.out.print(text);
                System.out.print("</" + childName + ">");
            });
            System.out.print("</" + ele_name + ">");
        });
        System.out.println("</" + name + ">");
    }
}
