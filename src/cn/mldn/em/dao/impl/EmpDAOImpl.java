package cn.mldn.em.dao.impl;

import java.sql.SQLException;

import java.util.List;
import java.util.Set;

import cn.mldn.em.dao.IEmpDAO;
import cn.mldn.em.vo.Emp;
import cn.mldn.util.dao.AbstractDAO;

public class EmpDAOImpl extends AbstractDAO implements IEmpDAO {

	@Override
	public boolean doCreate(Emp vo) throws SQLException {
		String sql = "INSERT INTO emp(deptno,mid,lid,ename,job,sal,conn,hiredate,phone,flag) VALUES (?,?,?,?,?,?,?,?,?,?)" ;
		super.pstmt = super.conn.prepareStatement(sql) ;
		super.pstmt.setInt(1, vo.getDeptno());
		super.pstmt.setString(2, vo.getMid());
		super.pstmt.setInt(3, vo.getLid());
		super.pstmt.setString(4, vo.getEname());
		super.pstmt.setString(5, vo.getJob());
		super.pstmt.setDouble(6, vo.getSal());
		super.pstmt.setDouble(7, vo.getConn());
		super.pstmt.setDate(8, new java.sql.Date(vo.getHiredate().getTime()));
		super.pstmt.setString(9, vo.getPhone());
		super.pstmt.setInt(10, vo.getFlag());
		
		return super.pstmt.executeUpdate() > 0;
	}

	@Override
	public boolean doUpdate(Emp vo) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean doRemoveBatch(Set<Integer> ids) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Emp findById(Integer id) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Emp> findAll() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Emp> findAllSplit(Integer currentPage, Integer lineSize) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Emp> findAllSplit(String column, String keyWord, Integer currentPage, Integer lineSize)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getAllCount() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getAllCount(String column, String keyWord) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
