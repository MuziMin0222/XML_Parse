package com.lhm.dom4j_parse;

import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/*
使用Dom4J对xml文件进行增删改查
 */
public class UserDom4JToCRUD {
    public static void main(String[] args) throws DocumentException, IOException {
        //创建SAXReader对象
        SAXReader reader = new SAXReader();

        //读取整个XML文档加载到内存中
        Document document = reader.read(Dom4jToXml.class.getResourceAsStream("/Student.xml"));

        Create(document);
    }

    //将Document写入到新文件中-----普通输出
    public static void Writer(Document document) throws IOException {
        FileWriter fw = new FileWriter("D:\\code\\workspace_IdeaUi\\xml_parse\\Dom4jStudent.xml");
        document.write(fw);
        fw.close();
    }

    //简单输出紧凑的样式
    public static void Writer1(Document document) throws IOException {
        FileWriter fw = new FileWriter("D:\\code\\workspace_IdeaUi\\xml_parse\\Dom4jStudent.xml");
        OutputFormat of = OutputFormat.createCompactFormat();
        of.setEncoding("UTF-8");
        XMLWriter writer = new XMLWriter(fw, of);
        writer.write(document);
        writer.flush();
        writer.close();
    }

    //美化输出
    public static void Writer2(Document document) throws IOException {
        FileWriter fw = new FileWriter("D:\\code\\workspace_IdeaUi\\xml_parse\\Dom4jStudent.xml");
        OutputFormat of = new OutputFormat();
        of.setEncoding("UTF-8");
        of.setIndent(true);
        of.setNewlines(true);
        XMLWriter xmlWriter = new XMLWriter(fw, of);
        xmlWriter.write(document);
        xmlWriter.flush();
        xmlWriter.close();
    }

    //便捷美化输出
    public static void Writer3(Document document) throws IOException {
        FileWriter fw = new FileWriter("D:\\code\\workspace_IdeaUi\\xml_parse\\Dom4jStudent.xml");
        OutputFormat of = OutputFormat.createPrettyPrint();
        XMLWriter xmlWriter = new XMLWriter(fw, of);
        xmlWriter.write(document);
        xmlWriter.flush();
        xmlWriter.close();
    }


    //添加操作
    public static void Create(Document document) throws IOException {
        //获取根节点
        Element rootElement = document.getRootElement();
        //在根节点上添加一个子节点
        Element newElement = rootElement.addElement("student");
        //在该新添加的子节点上为指定的属性赋值
        newElement.addAttribute("id", "006");
        newElement.addAttribute("sex","男");
        //在该新添加的子节点上再添加新的子节点
        Element childEle1 = newElement.addElement("name");
        childEle1.setText("代海辉");
        Element childEle2 = newElement.addElement("age");
        childEle2.setText("50");
        Element childEle3 = newElement.addElement("intro");
        childEle3.setText("花心萝卜头");

        //Writer(document);
        //Writer1(document);
        //Writer2(document);
        Writer3(document);
    }

    //删除操作
    public static void Delete(Document document) throws IOException {
        //获取根节点
        Element rootElement = document.getRootElement();
        //通过根节点来获取其所有子节点
        List<Element> elements = rootElement.elements();
        elements.forEach(o->{
            //根据属性名获取属性值
            String id = o.attributeValue("id");
            if ("001".equalsIgnoreCase(id)){
                //通过父节点来删除子节点
                Element parent = o.getParent();
                parent.remove(o);
            }else {
                System.out.println("删除失败。。。");
            }
        });

        Writer3(document);
    }

    //修改操作
    public static void Update(Document document) throws IOException {
        //获取根节点
        Element rootElement = document.getRootElement();
        //获取子节点
        List<Element> elements = rootElement.elements();
        elements.forEach(o->{
            if ("002".equals(o.attributeValue("id"))){
                List<Element> childEle = o.elements();
                childEle.forEach(e1->{
                    String type = e1.getName();
                    if ("name".equals(type)){
                        e1.setText("李煌民");
                    }
                    if ("age".equals(type)){
                        e1.setText("21");
                    }
                    if ("intro".equals(type)){
                        e1.setText("长得帅惹人爱");
                    }
                });
            }
        });

        Writer3(document);
    }

    //查找操作
    public static void Select(Document document){
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
