package com.zia.easybookmodule.site;

import com.zia.easybookmodule.bean.Book;
import com.zia.easybookmodule.bean.Catalog;
import com.zia.easybookmodule.engine.Site;
import com.zia.easybookmodule.net.NetUtil;
import com.zia.easybookmodule.util.BookGriper;
import com.zia.easybookmodule.util.RegexUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zia on 2018/11/30.
 * http://www.50zw.la/
 * 武林中文网 需要vpn
 */
public class Wulin extends Site {

    @Override
    public List<Book> search(String bookName) throws Exception {
        return BookGriper.baidu(bookName, getSiteName(), "13049992925692302651");
    }

    @Override
    public List<Catalog> parseCatalog(String catalogHtml, String rootUrl) {
        String sub = RegexUtil.regexExcept("<ul class=\"chapterlist\">", "</ul>", catalogHtml).get(0);
        String ssub = sub.split("正文</h5>")[1];
        List<String> as = RegexUtil.regexInclude("<a", "</a>", ssub);
        List<Catalog> list = new ArrayList<>();
        for (String s : as) {
            RegexUtil.Tag tag = new RegexUtil.Tag(s);
            String name = tag.getText();
            String href = rootUrl + tag.getValue("href");
            list.add(new Catalog(name, href));
        }
        return list;
    }

    @Override
    public List<String> parseContent(String chapterHtml) {
        String content = RegexUtil.regexExcept("<div id=\"htmlContent\" class=\"contentbox clear\">", "</div>", chapterHtml).get(0);
        List<String> contents = BookGriper.getContentsByBR(content);
        if (contents.size() > 0 && contents.get(0).contains("武林中文网")) {
            contents.remove(0);
        }
        if (contents.size() > 0 && contents.get(0).replaceAll("第.*章.*", "").isEmpty()) {
            contents.remove(0);
        }
        return contents;
    }

    @Override
    public String getSiteName() {
        return "武林中文网";
    }
}
