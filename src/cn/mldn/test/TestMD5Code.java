package cn.mldn.test;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;


import cn.mldn.util.MD5Code;

public class TestMD5Code {
	public static void main(String[] args) {
		String salt = Base64.encode("mldnjava".getBytes());
		System.out.println(salt);
		String pwd = "java" ;
		System.out.println(new MD5Code().getMD5ofStr(pwd + "({" + salt + "})"));
	}
}
