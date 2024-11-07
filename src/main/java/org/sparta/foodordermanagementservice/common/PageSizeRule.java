package org.sparta.foodordermanagementservice.common;

public class PageSizeRule {
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static boolean isPageSizeValid(int pageSize) {
        return pageSize == 10 || pageSize == 30 || pageSize == 50;
    }


}
