package com.beckonus.beckonus.customobjects;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Shal on 09/04/2017.
 */

public class LivingDataStore {
    // all the promotion objects in store;
    private static HashMap<String, PromotionObject> promotionMap = new HashMap<String, PromotionObject>();
    public static HashMap<String, StoreObject> favStores = new HashMap<String, StoreObject>();

    public static HashMap<String, PromotionObject> getPromotionMap() {
        return promotionMap;
    }

    public static void addLivingPromotion(PromotionObject promoObj) {
        promotionMap.put(promoObj.getPromotion_id(), promoObj);
    }

    public static PromotionObject getPromotionObjectByUUID(String uuid) {
        if (!promotionMap.containsKey(uuid)) return null;
        return promotionMap.get(uuid);
    }


}
