package cn.mldn.em.service.back.impl;

import java.util.HashMap;
import java.util.Map;

import cn.mldn.em.dao.IActionDAO;
import cn.mldn.em.dao.IMemberDAO;
import cn.mldn.em.dao.IRoleDAO;
import cn.mldn.em.dao.impl.ActionDAOImpl;
import cn.mldn.em.dao.impl.MemberDAOImpl;
import cn.mldn.em.dao.impl.RoleDAOImpl;
import cn.mldn.em.service.abs.AbstractService;
import cn.mldn.em.service.back.IMemberServiceBack;
import cn.mldn.em.vo.Member;
import cn.mldn.util.factory.DAOFactory;

public class MemberServiceBackImpl extends AbstractService implements IMemberServiceBack {

	@Override
	public Map<String, Object> login(Member vo) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		IMemberDAO memberDAO = DAOFactory.getInstance(MemberDAOImpl.class);
		Member ckVo = memberDAO.findById(vo.getMid()); // �����û���IDȡ���û���������
		if (ckVo != null) {
			if (ckVo.getPassword().equals(vo.getPassword())) { // ����������֤ͨ��
				IRoleDAO roleDAO = DAOFactory.getInstance(RoleDAOImpl.class) ;
				IActionDAO actionDAO = DAOFactory.getInstance(ActionDAOImpl.class) ;
				map.put("flag", true); // �洢��¼�ɹ��ı�־λ
				map.put("name", ckVo.getName()); // ����ʵ����ȡ��
				map.put("sflag", ckVo.getSflag()); // �������Ա��־
				map.put("allRoles", roleDAO.findAllByMember(vo.getMid())) ;
				map.put("allActions", actionDAO.findAllByMember(vo.getMid())) ;
			} else {
				map.put("flag", false); // �洢��¼�ɹ��ı�־λ
			}
		} else {
			map.put("flag", false); // �洢��¼�ɹ��ı�־λ 
		}
		return map;
	}
}
