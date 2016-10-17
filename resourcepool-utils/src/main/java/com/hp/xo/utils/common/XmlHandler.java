package com.hp.xo.utils.common;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class XmlHandler {
	private final static Logger log = Logger.getLogger(XmlHandler.class);
	   
	/**
	 * 将Dom4j格式的xml文档对象doc写入到指定介质中
	 * @param Document doc xml文档对象
	 * @param OutputStream dest 目的流对象引用
	 * @return boolean 写入成功为true,失败为false
	 */	
	public static boolean WriteXmlDoc(Document domcument,OutputStream dest){
		log.info("文档对象 : "+domcument.getUniquePath()+",输出流对象 : "+dest.getClass());
		boolean isDone = false;
		try{
			OutputFormat format = OutputFormat.createPrettyPrint();
			XMLWriter writer = new XMLWriter(dest,format);
			writer.write(domcument);
			writer.close();
			isDone = true;
		}
		catch (UnsupportedEncodingException e1){
			log.warn(e1);
		}
		catch (IOException e1){
			log.warn(e1);
		}
		log.info("XmlUtil.WriteXmlDoc 结束");
		return isDone;
	}
	public static Document createDocument(){
		return DocumentHelper.createDocument();
		//return new Document();
	}
	public static Element createElement(final String name){
		return DocumentHelper.createElement(name);
		//return new Element(name);
	}
	/**
	 * 通过xml文件名得到DOM
	 * @param xmlFileName 文件名
	 * @return
	 * @throws DocumentException
	 */
    public static Document getDocument(String xmlFileName) {
    	try{
    		return new SAXReader().read(new File(xmlFileName));   
    	}catch(DocumentException docExp){
    		System.err.println(xmlFileName+" 不存在");
    	}
    	return null;
    }
    /**
     * 通过xml文件内容得到DOM
     * @param xmlContent 文件内容
     * @param bool
     * @return
     * @throws DocumentException
     */
    public static Document getDocument(String xmlContent, boolean bool){
    	try{
    		return DocumentHelper.parseText(xmlContent);    	
    	}catch(DocumentException docExp){
    		System.err.println("结构不合法 请检验");
    	}
    	return null;
    }
	/**
	 * 
	 * 通过输入数据流解析XML文档。
	 * 
	 * @param InputStream iio 输入数据流
	 * @return XML文档,处理失败返回null
	 */
	public static Document getDocument(InputStream inputStream){
		try{
			return new SAXReader().read(inputStream);	
		}catch(DocumentException docExp){
    		System.err.println(inputStream.getClass()+"不正确");
    	}
    	return null;	
	}
	/**
	 * 得到节点
	 * @param domcument 文档对象
	 * @param elePath 文档路径 eg: "/students/student[name=\"崔卫兵\"]/@age" or "/students/student[name=\"cwb\"]/@age"
	 * or "/students/student[name=\"cwb\"]/college" or "/students/student[name=\"cwb\"]/college/@leader" or "/students/student[name=\"lxx\"]/college"
	 * or "/students/student[name=\"lxx\"]/college/@leader"
	 * @param eleValue 属性值
	 * @return
	 */
    public static Element getNode(Document domcument, String elePath, String eleValue) {
        Element ele = null;
        List list = domcument.selectNodes(elePath);
        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            Element tmp = (Element) iter.next();
            if (tmp.getText().equals(eleValue)) {
                ele = tmp;
            }
        }
        return ele;
    }
    /**
     *  得到节点
     * @param domcument
     * @param eleName
     * @return
     */
    public static Element getSingleNode(Document domcument, String elePath) {
    	return(Element) domcument.selectSingleNode(elePath);        
    }
    public static List getNodes(Document domcument, String eleName){
    	return domcument.selectNodes(eleName);
    }
    /**
     * 添加节点
     * @param parentEle 父节点
     * @param eleName 节点名称
     * @param eleValue 节点值
     */
    public static void addNode(Element parentEle, String eleName, String eleValue) {
        Element newEle = parentEle.addElement(eleName);
        newEle.setText(eleValue);
    }
    /**
     * 添加属性
     * @param ele 节点
     * @param attributeName 属性名称
     * @param attributeValue 属性值
     */
    public static void addAttribute(Element ele, String attributeName, String attributeValue) {
    	ele.addAttribute(attributeName, attributeValue);
    }
    /**
     * 输出字符串
     * @param document
     * @return
     */
    public static String transformDOM(Document document) {
        return document.asXML();
        
    }
    /**
     * 删除节点
     * @param parentEle
     * @param eleName
     * @param eleValue
     */
    public static void removeNode(Element parentEle, String eleName, String eleValue) {
        Iterator iter = parentEle.elementIterator();
        Element delEle = null;
        while (iter.hasNext()) {
            Element tmp = (Element) iter.next();
            if (tmp.getName().equals(eleName) && tmp.getText().equals(eleValue)) {
                delEle = tmp;
            }
        }
        if (delEle != null) {
            parentEle.remove(delEle);
        }
    }

    /**
     * 删除属性
     * @param ele
     * @param attributeName
     */
    public static void removeAttr(Element ele, String attributeName) {
        Attribute att = ele.attribute(attributeName);
        ele.remove(att);
    }

    /**
     * 修改节点值
     * @param ele
     * @param newValue
     */
    public static void setNodeText(Element ele, String newValue) {
        ele.setText(newValue);
    }

   /**
    * 修改属性值
    * @param ele
    * @param attributeName
    * @param attributeValue
    */
    public static void setAttribute(Element ele, String attributeName,String attributeValue) {
        Attribute att = ele.attribute(attributeName);
        att.setText(attributeValue);
    }
}
