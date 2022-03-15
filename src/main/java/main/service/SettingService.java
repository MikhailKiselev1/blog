package main.service;

import main.api.response.SettingResponse;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class SettingService {

    private ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("ApplicationContext.xml");

    public SettingResponse getInitGlobalSetting() {
        SettingResponse settingResponse = applicationContext.getBean("settingResponse", SettingResponse.class);
        settingResponse.setMultiuserMode(false);
        settingResponse.setPostPremoderation(true);
        settingResponse.setStatisticsIsPublic(true);
        return settingResponse;
    }

}
