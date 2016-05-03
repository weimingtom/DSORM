package com.meiqi.mushroom.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.meiqi.mushroom.dao.ITMushroomServiceDao;
import com.meiqi.mushroom.entity.TMushroomService;
import com.meiqi.util.BaseDao;
@Repository
public class TMushroomServiceDaoImpl extends BaseDao implements ITMushroomServiceDao{

	private final String STATEMENT = getClass().getCanonicalName();
	
	@Override
	public List<TMushroomService> findAll() {
		return getSqlSessionTemplate().selectList(STATEMENT+".ServiceGroupInfoList");
	}

	@Override
	public int add(TMushroomService tMushroomService) throws Exception {
		int insertCount = getSqlSessionTemplate().insert(STATEMENT+".add", tMushroomService);
		if(insertCount>0){
			return tMushroomService.getSid();
		}
		return 0;
	}

	@Override
	public int add(SqlSession sqlSession, TMushroomService tMushroomService)
			throws Exception {
		int insertCount = sqlSession.insert(STATEMENT+".add", tMushroomService);
		if(insertCount>0){
			return tMushroomService.getSid();
		}
		return 0;
	}

	@Override
	public TMushroomService findByName(String name) {
		return getSqlSessionTemplate().selectOne(STATEMENT+".selectByName_t_mushroom_service",name);
	}
	
}
