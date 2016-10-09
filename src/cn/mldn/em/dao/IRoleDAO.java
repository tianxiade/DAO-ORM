package cn.mldn.em.dao;

import java.sql.SQLException;
import java.util.Set;

import cn.mldn.em.vo.Role;
import cn.mldn.util.dao.IDAO;

public interface IRoleDAO extends IDAO<Integer, Role> {
	/**
	 * 可以根据用户编号取的所有的角色信息
	 * @param mid 用户编号
	 * @return
	 * @throws SQLException
	 */
	public Set<String> findAllByMember(String mid) throws SQLException ; 
}
