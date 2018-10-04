package com.saiyi.ui.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 */

public class QueryLists {

    public String result;
    public List<Query> data = new ArrayList<>();

    public static class Query {
        public String numberId;
        public String numberPhone;
        public String numberType;
        public String deviceId;
        public String numberOrder;
    }
}
