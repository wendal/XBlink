package org.xblink.types;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xblink.XType;
import org.xblink.annotations.XBlinkAlias;
import org.xblink.annotations.XBlinkAsArray;
import org.xblink.reader.XMLObjectReader;
import org.xblink.util.ClassUtil;
import org.xblink.util.NodeUtil;
import org.xblink.writer.XMLObjectWriter;
import org.xblink.writer.XMLWriterUtil;

/**
 * 数组类型.
 * 
 * 
 * @author pangwu86(pangwu86@gmail.com)
 * 
 */
public class XArray extends XType {

	public boolean getAnnotation(Field field) {
		XBlinkAsArray xArray = field.getAnnotation(XBlinkAsArray.class);
		if (null != xArray) {
			fieldTypes.add(field);
			return true;
		}
		return false;
	}

	public void writeItem(Object obj, XMLWriterUtil writer) throws Exception {
		for (Field field : fieldTypes) {
			// 判断是数组吗
			if (!field.getType().isArray()) {
				throw new Exception("字段：" + field.getName() + " 不是一个数组.");
			}
			// TODO 8中基本类型
			Object[] objs = (Object[]) field.get(obj);
			// 列表为空的话
			if (objs.length == 0) {
				continue;
			}
			// 判断是否需要起别名,添加前缀
			StringBuffer fieldName = ClassUtil.getFieldName(field);
			// 前缀
			boolean addSuffix = field.getAnnotation(XBlinkAlias.class) == null ? true : false;
			if (addSuffix) {
				fieldName.append(ARRAY);
			}
			writer.writeStartElement(fieldName.toString());
			// 数组内容
			for (Object object : objs) {
				new XMLObjectWriter().write(object, writer);
			}
			// 后缀
			writer.writeEndElement();

		}
	}

	public void readItem(Object obj, Node baseNode) throws Exception {
		for (Field field : fieldTypes) {
			// 判断是数组吗
			if (!field.getType().isArray()) {
				throw new Exception("字段：" + field.getName() + " 不是一个数组.");
			}
			StringBuffer fieldName = ClassUtil.getFieldName(field);
			boolean addSuffix = field.getAnnotation(XBlinkAlias.class) == null ? true : false;
			if (addSuffix) {
				fieldName.append(ARRAY);
			}
			Node tarNode = NodeUtil.getTarNode(baseNode, fieldName.toString());
			if (null == tarNode) {
				continue;
			}
			Class<?> fieldClass;
			// 特殊情况root的array
			if (tarNode.getNodeName().equals(ROOT + ARRAY)) {
				fieldClass = getImplClass().getNewInstanceClass();
			} else {
				fieldClass = field.getType().getComponentType();
			}
			field.set(obj, traceXPathArray(tarNode, fieldClass));
		}
	}

	/**
	 * 
	 * @param baseNode
	 * @param fieldClass
	 * @return
	 * @throws Exception
	 */
	private Object[] traceXPathArray(Node baseNode, Class<?> fieldClass) throws Exception {
		NodeList nodeList = baseNode.getChildNodes();
		int nodeListLength = nodeList.getLength();
		if (nodeList == null || nodeListLength == 0) {
			return (Object[]) Array.newInstance(fieldClass, 0);
		}
		nodeListLength = (nodeListLength - 1) / 2;
		Object[] result = (Object[]) Array.newInstance(fieldClass, nodeListLength);
		for (int idx = 0; idx < nodeListLength; idx++) {
			result[idx] = new XMLObjectReader().read(
					ClassUtil.getInstance(fieldClass, getImplClass()), nodeList.item(idx * 2 + 1),
					getImplClass(), getClassLoaderSwitcher());
		}
		return result;
	}
}
