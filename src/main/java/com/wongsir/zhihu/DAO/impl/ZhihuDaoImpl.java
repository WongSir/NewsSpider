package com.wongsir.zhihu.DAO.impl;

import java.util.ArrayList;
import java.util.List;

import com.wongsir.zhihu.DAO.ZhihuDao;
import com.wongsir.zhihu.entity.ZhihuUser;
import com.wongsir.zhihu.utils.DBHelper;

/**
 * @Description: 知乎 数据库持久化接口 实现
 * @author Wongsir
 * @date 2016年12月6日 下午3:54:50
 * 
 */
public class ZhihuDaoImpl implements ZhihuDao {
	public int saveUser(ZhihuUser user) {
		DBHelper dbhelper = new DBHelper();
		StringBuffer sql = new StringBuffer();
		sql.append(
				"INSERT INTO spider_zhihu_user ( `key`,`name`,identity,location,profession,sex,school,major,recommend,picUrl,agree,thanks,ask,answer,article,collection)")
				// `key`,`name`,identity,location,profession,sex,school,major,recommend,picUrl,agree,thanks,ask,answer,article,collection
				.append("VALUES (? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? ) ");
		// 设置 sql values 的值
		List<String> sqlValues = new ArrayList<String>();
		sqlValues.add(user.getKey());
		sqlValues.add(user.getName());
		sqlValues.add(user.getIdentity());
		sqlValues.add(user.getLocation());
		sqlValues.add(user.getProfession());
		sqlValues.add("" + user.getSex());
		sqlValues.add(user.getSchool());
		sqlValues.add(user.getMajor());
		sqlValues.add(user.getRecommend());
		sqlValues.add(user.getPicUrl());
		sqlValues.add("" + user.getAgree());
		sqlValues.add("" + user.getThanks());
		sqlValues.add("" + user.getAsk());
		sqlValues.add("" + user.getAnswer());
		sqlValues.add("" + user.getArticle());
		sqlValues.add("" + user.getCollection());
		int result = dbhelper.executeUpdate(sql.toString(), sqlValues);
		return result;
	}
}
