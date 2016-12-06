package com.wongsir.zhihu.service;

import java.util.Date;

import com.wongsir.zhihu.DAO.ZhihuDao;
import com.wongsir.zhihu.DAO.impl.ZhihuDaoImpl;
import com.wongsir.zhihu.entity.ZhihuUser;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * @Description: 知乎用户小爬虫<br>
 *               输入搜索用户关键词(keyword)，并把搜出来的用户信息爬出来<br>
 * @author Wongsir
 * @date 2016年12月6日 下午3:51:48
 * 
 */
public class ZhiHuUserPageProcessor implements PageProcessor{

	// 抓取网站的相关配置，包括：编码、抓取间隔、重试次数等
	private Site site = Site.me().setRetryTimes(10).setSleepTime(1000);
	// 用户数量
	private static int num = 0;
	// 搜索关键词
	private static String keyword = "JAVA";
	// 数据库持久化对象，用于将用户信息存入数据库
	private ZhihuDao zhihuDao = new ZhihuDaoImpl();

	/**
	 * process 方法是webmagic爬虫的核心<br>
	 * 编写抽取【待爬取目标链接】的逻辑代码在html中。
	 */
	public void process(Page page) {

		// 1. 如果是用户列表页面 【入口页面】，将所有用户的详细页面的url放入target集合中。
		if (page.getUrl().regex("https://www\\.zhihu\\.com/search\\?type=people&q=[\\s\\S]+").match()) {
			page.addTargetRequests(page.getHtml()
					.xpath("//ul[@class='list users']/li[@class='item item-card user-card clearfix']/div[@class='left content']/div[@class='body']/div[@class='line']").links().all());
			
			System.out.println("入口页面");
		}
		// 2. 如果是用户详细页面
		else {
			
			num++;// 用户数++
			/* 实例化ZhihuUser，方便持久化存储。 */
			ZhihuUser user = new ZhihuUser();
			/* 从下载到的用户详细页面中抽取想要的信息，这里使用xpath居	多 */
			/* 为了方便理解，抽取到的信息先用变量存储，下面再赋值给对象 */
			String name = page.getHtml().xpath("//div[@class='ProfileHeader-contentHead']/h1[@class='ProfileHeader-title']/span[@class='ProfileHeader-name']/text()")
					.get();
			String identity = page.getHtml().xpath("//div[@class='ProfileHeader-contentHead']/h1[@class='ProfileHeader-title']/span[@class='RichText ProfileHeader-headline']/text()")
					.get();
			String location = page.getHtml()
					.xpath("//div[@class='item editable-group']/span[@class='info-wrap']/span[@class='location item']/@title")
					.get();
			String profession = page.getHtml()
					.xpath("//div[@class='item editable-group']/span[@class='info-wrap']/span[@class='business item']/@title")
					.get();
			boolean isMale = page.getHtml().xpath("//span[@class='item gender']/i[@class='icon icon-profile-male']")
					.match();
			boolean isFemale = page.getHtml().xpath("//span[@class='item gender']/i[@class='icon icon-profile-female']")
					.match();
			int sex = -1;
			/* 因为知乎有一部分人不设置性别 或者 不显示性别。所以需要判断一下。 */
			if (isMale && !isFemale)
				sex = 1;// 1代表男性
			else if (!isMale && isFemale)
				sex = 0;// 0代表女性
			else
				sex = 2;// 2代表未知
			String school = page.getHtml().xpath("//span[@class='education item']/@title").get();
			String major = page.getHtml().xpath("//span[@class='education-extra item']/@title").get();
			String recommend = page.getHtml().xpath("//span[@class='fold-item']/span[@class='content']/@title").get();
			String picUrl = page.getHtml().xpath("//div[@class='body clearfix']/img[@class='Avatar Avatar--l']/@src")
					.get();
			int agree = Integer.parseInt(
					page.getHtml().xpath("//span[@class='zm-profile-header-user-agree']/strong/text()").get());
			int thanks = Integer.parseInt(
					page.getHtml().xpath("//span[@class='zm-profile-header-user-thanks']/strong/text()").get());
			int ask = Integer.parseInt(page.getHtml()
					.xpath("//div[@class='profile-navbar clearfix']/a[2]/span[@class='num']/text()").get());
			int answer = Integer.parseInt(page.getHtml()
					.xpath("//div[@class='profile-navbar clearfix']/a[3]/span[@class='num']/text()").get());
			int article = Integer.parseInt(page.getHtml()
					.xpath("//div[@class='profile-navbar clearfix']/a[4]/span[@class='num']/text()").get());
			int collection = Integer.parseInt(page.getHtml()
					.xpath("//div[@class='profile-navbar clearfix']/a[5]/span[@class='num']/text()").get());

			// 对象赋值
			user.setKey(keyword);
			user.setName(name);
			user.setIdentity(identity);
			user.setLocation(location);
			user.setProfession(profession);
			user.setSex(sex);
			user.setSchool(school);
			user.setMajor(major);
			user.setRecommend(recommend);
			user.setPicUrl(picUrl);
			user.setAgree(agree);
			user.setThanks(thanks);
			user.setAsk(ask);
			user.setAnswer(answer);
			user.setArticle(article);
			user.setCollection(collection);

			System.out.println("num:" + num + " " + user.toString());// 输出对象
			System.out.println("用户详细页面");
			zhihuDao.saveUser(user);// 保存用户信息到数据库
		}
	}

	public Site getSite() {
		return this.site;
	}

	public static void main(String[] args) {
        long startTime ,endTime;
        System.out.println("========知乎用户信息小爬虫【启动】喽！=========");
        startTime = new Date().getTime();
        //入口为：【https://www.zhihu.com/search?type=people&q=xxx 】，其中xxx 是搜索关键词
        Spider.create(new ZhiHuUserPageProcessor()).addUrl("https://www.zhihu.com/search?type=people&q="+keyword).thread(5).run();
        endTime = new Date().getTime();
        System.out.println("========知乎用户信息小爬虫【结束】喽！=========");
        System.out.println("一共爬到"+num+"个用户信息！用时为："+(endTime-startTime)/1000+"s");
    }
   
}
