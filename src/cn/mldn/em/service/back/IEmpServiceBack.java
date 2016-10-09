package cn.mldn.em.service.back;

import java.util.Map;

import cn.mldn.em.vo.Emp;

public interface IEmpServiceBack {
    public Map<String,Object> addPre(String mid) throws Exception ;
    public boolean add(Emp vo) throws Exception ;
}
