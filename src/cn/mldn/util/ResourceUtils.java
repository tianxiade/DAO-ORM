package cn.mldn.util;

import java.util.ResourceBundle;

public class ResourceUtils {
	/**
	 * ���Ը���ָ������Դ���ƺ�ָ����keyȡ�ö�Ӧ��value���� 
	 * @param baseName
	 * @param key
	 * @return
	 */
	public static String get(String baseName, String key) {
		return ResourceBundle.getBundle(baseName).getString(key) ;
	}
}
