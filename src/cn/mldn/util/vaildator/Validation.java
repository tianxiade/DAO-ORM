package cn.mldn.util.vaildator;

import java.util.HashMap;
import java.util.Map;

import cn.mldn.util.ResourceUtils;
import cn.mldn.util.servlet.DispatcherServlet;

public class Validation {
	private Validation() {}
	/**
	 * �������ݵ���֤�����������Ҫ���д�����֣�������������֤������ô�Ὣ������Ϣ������Map�������棩<br>
	 * Map���ϻᱣ���������ݣ�<br>
	 * <li>key = ���������ƣ�</li>
	 * <li>value = �����������Ϣ��</li>
	 * @param servlet ��Ϊ�����Ĵ���֮����Ҫ����Pages��Messages��getXxx()�Ĳ�������
	 * @return
	 */
	public static Map<String,String> validate(DispatcherServlet servlet) {
		Map<String,String> errors = new HashMap<String,String>() ;
		String ruleKey = servlet.getClass().getSimpleName() + "." + servlet.getStatus() + ".rules" ;
		try {
			String rule = ResourceUtils.get("Validations", ruleKey) ;
			if (rule != null) {	// ����Ӧ�ù���
				String result [] = rule.split("\\|") ;
				for (int x = 0 ; x < result.length ; x ++) {	// ��ʼȡ��ÿһ��������м��
					String temp [] = result[x].split(":") ;
					String val = servlet.getStringParameter(temp[0]) ;
					switch(temp[1]) {
						case "string" : {
							if (!Validation.validateEmpty(val)) {	// ��֤ʧ��
								errors.put(temp[0], servlet.getMessageValue("validation.string.msg")) ;
							}
							break ;
						}
						case "int" : {
							if (!Validation.validateInt(val)) {	// ��֤ʧ��
								errors.put(temp[0], servlet.getMessageValue("validation.int.msg")) ;
							}
							break ;
						}
						case "double" : {
							if (!Validation.validateDouble(val)) {	// ��֤ʧ��
								errors.put(temp[0], servlet.getMessageValue("validation.double.msg")) ;
							}
							break ;
						}
						case "date" : {
							if (!Validation.validateDate(val)) {	// ��֤ʧ��
								errors.put(temp[0], servlet.getMessageValue("validation.date.msg")) ;
							}
							break ;
						}
						case "datetime" : {
							if (!Validation.validateDatetime(val)) {	// ��֤ʧ��
								errors.put(temp[0], servlet.getMessageValue("validation.datetime.msg")) ;
							}
							break ;
						}
					}
				}
			}
		} catch (Exception e) {	// �����˴��󣬾ͱ�ʾû�й���
			e.printStackTrace();
		}
		return errors ;
	}
	/**
	 * ��֤�����Ƿ�������
	 * @param val
	 * @return
	 */
	public static boolean validateDatetime(String val) {
		if (validateEmpty(val)) {
			return val.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}") ;
		}
		return false ;
	}
	/**
	 * ��֤�����Ƿ�������
	 * @param val
	 * @return
	 */
	public static boolean validateDate(String val) {
		if (validateEmpty(val)) {
			return val.matches("\\d{4}-\\d{2}-\\d{2}") ;
		}
		return false ;
	}
	/**
	 * ��֤�����Ƿ���С��
	 * @param val
	 * @return
	 */
	public static boolean validateDouble(String val) {
		if (validateEmpty(val)) {
			return val.matches("\\d+(\\.\\d+)?") ;
		}
		return false ;
	}
	/**
	 * ��֤�����Ƿ�������
	 * @param val
	 * @return
	 */
	public static boolean validateInt(String val) {
		if (validateEmpty(val)) {
			return val.matches("\\d+") ;
		}
		return false ;
	}
	/**
	 * ��ָ֤���������Ƿ�Ϊnull�����ǡ�""��
	 * @return ����ǿ��ַ����򷵻�false�����򷵻�true
	 */
	public static boolean validateEmpty(String val) {
		if (val == null || "".equals(val)) {
			return false ;
		}
		return true ;
	}
}
