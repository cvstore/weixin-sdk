package com.riversoft.weixin.qy.menu;

import com.riversoft.weixin.common.WxClient;
import com.riversoft.weixin.common.menu.Menu;
import com.riversoft.weixin.common.menu.MenuWrapper;
import com.riversoft.weixin.common.util.JsonMapper;
import com.riversoft.weixin.qy.QyWxClientFactory;
import com.riversoft.weixin.qy.base.CorpSetting;
import com.riversoft.weixin.qy.base.DefaultSettings;
import com.riversoft.weixin.qy.base.WxEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by exizhai on 9/25/2015.
 */
public class Menus {

    private static Logger logger = LoggerFactory.getLogger(Menus.class);
    private WxClient wxClient;

    public static Menus defaultMenus() {
        return with(DefaultSettings.defaultSettings().getCorpSetting());
    }

    public static Menus with(CorpSetting corpSetting) {
        Menus menus = new Menus();
        menus.setWxClient(QyWxClientFactory.getInstance().with(corpSetting));
        return menus;
    }

    public void setWxClient(WxClient wxClient) {
        this.wxClient = wxClient;
    }

    public void create(int agent, Menu menu) {
        String url = WxEndpoint.get("url.menu.create");

        String json = JsonMapper.nonEmptyMapper().toJson(menu);
        logger.info("update menu: {}", json);
        wxClient.post(String.format(url, agent), json);
    }

    public void create(Menu menu) {
        create(DefaultSettings.defaultSettings().getDefaultAgent(), menu);
    }

    public void delete() {
        delete(DefaultSettings.defaultSettings().getDefaultAgent());
    }

    public void delete(int agent) {
        String url = WxEndpoint.get("url.menu.delete");
        wxClient.get(String.format(url, agent));
    }

    public Menu list(int agent) {
        String url = WxEndpoint.get("url.menu.list");
        String content = wxClient.get(String.format(url, agent));

        logger.debug("list menu: {}", content);

        MenuWrapper menuWrapper = JsonMapper.nonEmptyMapper().fromJson(content, MenuWrapper.class);
        return menuWrapper.getMenu();
    }

    public Menu list() {
        return list(DefaultSettings.defaultSettings().getDefaultAgent());
    }

}
