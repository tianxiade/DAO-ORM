package cn.mldn.em.dao;

import java.sql.SQLException;
import java.util.List;

import cn.mldn.em.vo.Dept;
import cn.mldn.util.dao.IDAO;

public interface IDeptDAO extends IDAO<Integer, Dept> {
    public List<Dept> findAllByEmpyt() throws SQLException ;
    public boolean doUpdateCurrnum(Integer id , Integer num) throws SQLException ;
}
