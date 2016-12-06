package com.wongsir.zhihu.DAO;

import com.wongsir.zhihu.entity.ZhihuUser;

/** 
* @Description: 知乎 数据持久化 接口
* @author Wongsir
* @date 2016年12月6日 下午3:54:07 
*  
*/
public interface ZhihuDao {
	 /**
     * 保存用户信息
     * @param user
     * @return
     */
    public int saveUser(ZhihuUser user);
}
