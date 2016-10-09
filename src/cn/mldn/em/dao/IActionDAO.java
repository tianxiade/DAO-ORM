package cn.mldn.em.dao;

import java.sql.SQLException;
import java.util.Set;

import cn.mldn.em.vo.Action;
import cn.mldn.util.dao.IDAO;

public interface IActionDAO extends IDAO<Integer, Action> {
	/**
	 * 根据用户编号取得用户对应的所有权限数据
	 * @param mid
	 * @return
	 * @throws SQLException
	 */
	public Set<String> findAllByMember(String mid) throws SQLException;
}
