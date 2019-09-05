package com.lhm.dom_parse;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
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

public class DomToXmlCRUD {
    public static void main(String[] args) throws Exception {
        Document document = CreateDoc(new File("D:\\code\\workspace_IdeaUi\\xml_parse\\src\\main\\resources\\Student.xml"));
        Updata(document,"005","李煌民");
    }

    /**
     *
     * @param file  IO中的文件流作为形式参数
     * @return Document对象
     * @throws Exception   抛出的异常以最大的异常为准
     */
    //获取Document对象
    public static Document CreateDoc(File file) throws Exception {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = builder.parse(file);
        return document;
    }

    //通过节点来遍历整个XML文档
    public static void ReadByNodeName(Node node){
        //如果该节点是一个标签节点
        if (node.getNodeType() == Node.ELEMENT_NODE){
            System.out.println("该标签节点名字为：" + node.getNodeName());
            System.out.println("该标签节点类型为：" + node.getNodeType());
            System.out.println("该标签节点值为：" + node.getNodeValue());
        }
        //获得该节点的所有子节点
        NodeList nl = node.getChildNodes();
        //遍历所有的子节点
        for (int i = 0; i < nl.getLength(); i++) {
            Node node1 = nl.item(i);
            //将所有的子节点变成根节点来遍历该子节点下面的子节点
            ReadByNodeName(node1);
        }
    }

    //因为增删改都需要使用Transform来将xml文件进行更新
    public static void ReadXmlFromRamToFile(File file,Document document) throws TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer tf = factory.newTransformer();
        //将内存的中xml读取到程序中
        DOMSource source = new DOMSource(document);
        //将读入的xml写入到指定的文件中
        StreamResult result = new StreamResult(file);
        tf.transform(source,result);
    }

    //查找方法，通过传入一个标签序号来查找对应的标签
    public static void Select(Document document,int itemNum){
        Element student = (Element)document.getElementsByTagName("student").item(itemNum - 1);
        if (student != null){
            //获得其id属性
            String id = student.getAttribute("id");
            //获得Sex属性值
            String sex = student.getAttribute("sex");
            // 分别获得第itemNum-1个元素下的name、age、intro的文本值
            // 因为第itemNum-1个student下只有一个name元素，所以使用item(0),age和intro亦然
            String name = student.getElementsByTagName("name").item(0).getTextContent();
            String age = student.getElementsByTagName("age").item(0).getTextContent();
            String intro = student.getElementsByTagName("intro").item(0).getTextContent();
            System.out.println(id + "---" + sex + "---" + name + "---" + age + "---" + intro);
        }else {
            System.out.println("该" +itemNum+ "学生不存在");
        }
    }

    //删除方法，根据传入的第几个元素进行删除
    public static void Delete(Document document,int studentNum) throws TransformerException {
        Element student = (Element)document.getElementsByTagName("student").item(studentNum - 1);
        if (student != null){
            student.getParentNode().removeChild(student);
            System.out.println("删除成功。。。。");
        }else {
            System.out.println("删除第" + studentNum + "位学生失败。。。。");
        }

        //将内存中的信息更新到新的xml文件中
        ReadXmlFromRamToFile(new File("D:\\code\\workspace_IdeaUi\\xml_parse\\newStudent.xml"),document);
    }

    //增加方法，传入标签名字，属性值
    public static void Create(
            Document document,
            String Student_name,
            String Student_id,
            String Student_sex,
            String Student_age,
            String Student_intro) throws TransformerException {
        //找到跟节点，根节点只有一个
        Element students = (Element)document.getElementsByTagName("students").item(0);
        //创建各个节点
        Element ele_student = document.createElement("student");
        ele_student.setAttribute("id",Student_id);
        ele_student.setAttribute("sex",Student_sex);
        Element ele_name = document.createElement("name");
        ele_name.setTextContent(Student_name);
        Element ele_age = document.createElement("age");
        ele_age.setTextContent(Student_age);
        Element ele_intro = document.createElement("intro");
        ele_intro.setTextContent(Student_intro);
        //使用appendChild方法增加父子关系
        students.appendChild(ele_student);
        ele_student.appendChild(ele_name);
        ele_student.appendChild(ele_age);
        ele_student.appendChild(ele_intro);
        //写入到xml文件中
        ReadXmlFromRamToFile(new File("D:\\code\\workspace_IdeaUi\\xml_parse\\newStudent.xml"),document);
        System.out.println("添加成功。。。。。");
    }

    //修改方法
    public static void Updata(Document document,String id,String newName) throws TransformerException {
        boolean isFund = false;
        //获得student节点集合
        NodeList nl = document.getElementsByTagName("student");
        //遍历student集合，并从中获得其id属性为传入的id值的元素，然后修改其元素下的name的文本值
        for (int i = 0; i < nl.getLength(); i++) {
            Element item = (Element)nl.item(i);
            if (id.equalsIgnoreCase(item.getAttribute("id"))){
                item.getElementsByTagName("name").item(0).setTextContent(newName);
                isFund = true;
                break;
            }
        }

        if (isFund){
            //写入到新的xml文件中
            ReadXmlFromRamToFile(new File("D:\\code\\workspace_IdeaUi\\xml_parse\\newStudent.xml"),document);
            System.out.println("修改成功。。。。");
        }else {
            System.out.println("修改失败.....");
        }
    }

}
