package cn.mldn.em.service.back;

import java.util.Map;

import cn.mldn.em.vo.Member;

public interface IMemberServiceBack {
	
    public Map<String,Object> login(Member vo) throws Exception ;
}
