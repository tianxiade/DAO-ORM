package cn.mldn.util.bean;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;

import cn.mldn.util.StringUtils;

/**
 * ����Ĺ����Ǹ��ݵ�ǰ��servlet�����Լ�����������ʵ�����Ե����á�
 * ǰ�᣺����VO����һ����Servlet����ʵ������ɣ������ṩ����Ӧ��getter������
 * @author mldn
 */
public class BeanValueUtil {
	private Object servletObject ;	// �����Servlet����
	private String attributeName ;	// ���Ե����֣����С�.��
	private String attributeValue ;	// ���Ե����ݣ�ͨ���ⲿ��request.getParameter()���յ�
	/**
	 * ��������Ҫ�Ķ���
	 * @param servletObject �����˲�����Servlet����
	 * @param attributeName ����Ĳ�������
	 * @param attributeValue ����Ĳ�������
	 */
	public BeanValueUtil(Object servletObject, String attributeName, String attributeValue) {
		this.servletObject = servletObject;
		this.attributeName = attributeName.trim();
		this.attributeValue = attributeValue.trim();
	}
	/**
	 * ʵ���������ݵ����ô������������������ø�ָ��VO�������
	 */
	public void setObjectValue() {	// ʵ�����ݵ�����
		Object currentObject = null ;	// �������ǵ�ǰ��VO�����
		if (this.attributeName.contains(".")) {	// ��������У���.������Ϊ��ʱ�����ݿ��Դ���
			String result [] = this.attributeName.split("\\.") ;	// ���ա�.�����
			try {
				Method getMethod = this.servletObject.getClass().getMethod("get" + StringUtils.initcap(result[0])) ;
				currentObject = getMethod.invoke(this.servletObject) ;
				if (result.length == 2) {	// ��������
					Field field = currentObject.getClass().getDeclaredField(result[1]) ;	// ȡ��ָ�����ԵĶ���
					Method setMethod = currentObject.getClass().getMethod("set" + StringUtils.initcap(result[1]), field.getType()) ;
					setMethod.invoke(currentObject, this.convertValue(field.getType().getSimpleName())) ;	// ʵ����setter����
				} else {	// �༶����
					for (int x = 1 ; x < result.length - 1 ; x ++) {
						Field subField = currentObject.getClass().getDeclaredField(result[x]) ;
						Method getSubMethod = currentObject.getClass().getMethod("get" + StringUtils.initcap(result[x])) ; 
						if (getSubMethod.invoke(currentObject) == null) {	// ��ʾ���еĶ���δ����ʼ��
							currentObject = this.objectNewInstance(currentObject, result[x], subField) ;
						} else {
							currentObject = getSubMethod.invoke(currentObject) ;
						}
					}
					Field attField = currentObject.getClass().getDeclaredField(result[result.length - 1]) ;
					Method attMethod = currentObject.getClass().getMethod("set" + StringUtils.initcap(result[result.length - 1]), attField.getType()) ;
					attMethod.invoke(currentObject, this.convertValue(attField.getType().getSimpleName())) ;
				}
			} catch (Exception e) {
				// e.printStackTrace();
			}
		}
	}
	/**
	 * �������setter����ʵ�ֶ����ʵ��������
	 * @param currentObject ��ǰ�Ķ���
	 * @param attr ����
	 * @param field �������������ͣ�ֻ��֪�����Ͳſ��Է���ʵ��������
	 * @return һ��ʵ�����õĶ���
	 */
	private Object objectNewInstance(Object currentObject, String attr, Field field) throws Exception {
		Object newObject = field.getType().newInstance(); // ����ʵ��������
		Method setMethod = currentObject.getClass().getMethod("set" + StringUtils.initcap(attr), field.getType());
		setMethod.invoke(currentObject, newObject);
		return newObject;
	}

	/** 
	 * ��Ϊ��������ݶ���String��������Ҫ�����Ϊָ��������
	 * @return
	 */
	private Object convertValue(String type) {
		if ("int".equals(type) || "Integer".equals(type)) {
			return Integer.parseInt(this.attributeValue) ;	// �����ݱ�Ϊ����
		} 
		if ("double".equalsIgnoreCase(type)) {
			return Double.parseDouble(this.attributeValue) ;
		}
		if ("date".equalsIgnoreCase(type)) {
			try {
				if (this.attributeValue.matches("\\d{4}-\\d{2}-\\d{2}")) {
					return new SimpleDateFormat("yyyy-MM-dd").parse(this.attributeValue);
				}
				if (this.attributeValue.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")) {
					return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(this.attributeValue);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null ;
		}
		return this.attributeValue ;
	}
	
}
