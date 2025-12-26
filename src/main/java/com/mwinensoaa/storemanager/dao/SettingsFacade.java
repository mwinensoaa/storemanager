package com.mwinensoaa.storemanager.dao;


import com.mwinensoaa.storemanager.entities.Settings;

public interface SettingsFacade {

    void createSettings(Settings settings);
    Settings getSettings();
    void deleteSettings(String companyName);
    Settings getDefaultLanguage();

}
