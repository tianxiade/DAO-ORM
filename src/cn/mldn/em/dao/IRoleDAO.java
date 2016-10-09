package cn.mldn.em.dao;

import java.sql.SQLException;
import java.util.Set;

import cn.mldn.em.vo.Role;
import cn.mldn.util.dao.IDAO;

public interface IRoleDAO extends IDAO<Integer, Role> {
	/**
	 * ���Ը����û����ȡ�����еĽ�ɫ��Ϣ
	 * @param mid �û����
	 * @return
	 * @throws SQLException
	 */
	public Set<String> findAllByMember(String mid) throws SQLException ; 
}
