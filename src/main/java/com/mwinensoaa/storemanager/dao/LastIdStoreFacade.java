package com.mwinensoaa.storemanager.dao;

import com.mwinensoaa.storemanager.entities.LastIdStore;

public interface LastIdStoreFacade {

    void updateLastId(LastIdStore lastIdStore);
    LastIdStore getIds();
    LastIdStore getId(String key);


}
