package firstDemoAboutWM;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

/** 
* @Description: PageProcessor是webmagic-core的一部分，定制一个PageProcessor即可实现自己的爬虫逻辑。
* @author Wongsir
* @date 2016年11月17日16:31:40 
*  
*/

public class GithubRepoPageProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);

    public void process(Page page) {
        page.addTargetRequests(page.getHtml().links().regex("(https://github\\.com/\\w+/\\w+)").all());
        page.putField("author", page.getUrl().regex("https://github\\.com/(\\w+)/.*").toString());
        page.putField("name", page.getHtml().xpath("//h1[@class='entry-title public']/strong/a/text()").toString());
        if (page.getResultItems().get("name")==null){
            //skip this page
            page.setSkip(true);
        }
        page.putField("readme", page.getHtml().xpath("//div[@id='readme']/tidyText()"));
    }

    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new GithubRepoPageProcessor()).addUrl("https://github.com/code4craft").thread(5).run();
    }
}

//public class GithubRepoPageProcessor implements PageProcessor{
//
//	private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);
//	
//	/* (non-Javadoc)
//	 * @see us.codecraft.webmagic.processor.PageProcessor#process(us.codecraft.webmagic.Page)
//	 */
//	public void process(Page page) {
//		page.addTargetRequests(page.getHtml().links().regex("(https://github\\.com/\\w+/\\w+)").all());
//		page.putField("author", page.getUrl().regex("https://github\\.com/(\\w+)/.*").toString());
//		page.putField("name", page.getHtml().xpath("//h1[@class='entry-title public']/strong/a/text()").toString());
//		if(page.getResultItems().get("name")==null){
//			//skip this page 跳过该页面
//			page.setSkip(true);
//		}
//		page.putField("readme", page.getHtml().xpath("//div[@id='readme']/tidyText()"));
//	}
//
//	/* (non-Javadoc)
//	 * @see us.codecraft.webmagic.processor.PageProcessor#getSite()
//	 */
//	public Site getSite() {
//		return site;
//	}
//	
//	public static void main(String[] args){
//		Spider.create(new GithubRepoPageProcessor())
//			.addUrl("https://github.com/code4craft").thread(5).run();
//	}
//	
//}


/**
 * 
 * 旧版本的用法示例
public class OschinaBlogPageProcesser implements PageProcessor {

	private Site site = Site.me().setDomain("my.oschina.net")
						.addStartUrl("http://my.oschina.net/flashsword/blog");
	
	public void process(Page page) {
		List<String> links = page.getHtml().links().regex("http://my\\.oschina\\.net/flashsword/blog\\d+").all();
		page.addTargetRequests(links);
		page.putField("title", page.getHtml().xpath("//div[@class='BlogEntity']/div[@class='BlogTitle']/h1").toString());
		page.putField("content", page.getHtml().$("div.content").toString());
		page.putField("tags", page.getHtml().xpath("//div[@class='BlogTags']/a/text()").all());
	}

	public Site getSite() {
		return site;
	}
	
	public static void main(String[] args){
		Spider.create(new OschinaBlogPageProcesser()).pipeline(new ConsolePipeline()).run();
	}

}
 */