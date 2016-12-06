package com.wongsir.zhihu.controller;

import java.util.Date;

import com.wongsir.zhihu.service.ZhiHuUserPageProcessor;

import us.codecraft.webmagic.Spider;

/**
 * @Description: TODO
 * @author Wongsir
 * @date 2016年12月6日 下午3:55:59
 * 
 */
public class ZhihuSpider {

	// 用户数量
	private static int num = 0;
	// 搜索关键词
	private static String keyword = "JAVA";

	public static void main(String[] args) {
		long startTime, endTime;
		System.out.println("========知乎用户信息小爬虫【启动】喽！=========");
		startTime = new Date().getTime();
		// 入口为：【https://www.zhihu.com/search?type=people&q=xxx 】，其中xxx 是搜索关键词
		Spider.create(new ZhiHuUserPageProcessor()).addUrl("https://www.zhihu.com/search?type=people&q=" + keyword)
				.thread(5).run();
		endTime = new Date().getTime();
		System.out.println("========知乎用户信息小爬虫【结束】喽！=========");
		System.out.println("一共爬到" + num + "个用户信息！用时为：" + (endTime - startTime) / 1000 + "s");
	}
}
