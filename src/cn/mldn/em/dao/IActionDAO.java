package cn.mldn.em.dao;

import java.sql.SQLException;
import java.util.Set;

import cn.mldn.em.vo.Action;
import cn.mldn.util.dao.IDAO;

public interface IActionDAO extends IDAO<Integer, Action> {
	/**
	 * �����û����ȡ���û���Ӧ������Ȩ������
	 * @param mid
	 * @return
	 * @throws SQLException
	 */
	public Set<String> findAllByMember(String mid) throws SQLException;
}
