package firstDemoAboutWM;

import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.model.annotation.ExtractBy;

/**
 * @since 0.4.0
 * @author code4crafter@gmail.com
 */
public class BaiduBaike{
    //body > div.body-wrapper > div.content-wrapper > div > div.main-content > dl > dd > h1
    @ExtractBy(value = "/html/body/div[4]/div[2]/div/div[1]/dl/dd/h1")
    private String name;

    @ExtractBy(value = "/html/body/div[4]/div[2]/div/div[1]/div[4]/div")
    private String description;

    @Override
    public String toString() {
        return "BaiduBaike{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public static void main(String[] args) {
        OOSpider ooSpider = OOSpider.create(Site.me().setSleepTime(0), BaiduBaike.class);
        BaiduBaike baike = ooSpider.<BaiduBaike>get("http://baike.baidu.com/item/httpclient");
        System.out.println(baike);
        ooSpider.close();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
