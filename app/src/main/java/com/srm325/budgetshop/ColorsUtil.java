package com.srm325.budgetshop;

import android.graphics.Color;

public class ColorsUtil {
    public static final int AUTO_CATEGORY_COLOR = getColorInt("#6EB650");
    public static final int BEAUTY_CATEGORY_COLOR = getColorInt("#D61E5E");
    public static final int CLOTHING_CATEGORY_COLOR = getColorInt("#00B2D6");
    public static final int ENTERTAINMENT_CATEGORY_COLOR = getColorInt("#E63B43");
    public static final int FINANCIAL_CATEGORY_COLOR = getColorInt("#E63B43");
    public static final int GENERAL_CATEGORY_COLOR = getColorInt("#E63B43");
    public static final int GROCERIES_CATEGORY_COLOR = getColorInt("#932B8E");
    public static final int HOME_CATEGORY_COLOR = getColorInt("#8DC643");
    public static final int INDUSTRY_CATEGORY_COLOR = getColorInt("#29AAE3");
    public static final int LEARNING_CATEGORY_COLOR = getColorInt("#00A652");
    public static final int MEDICAL_CATEGORY_COLOR = getColorInt("#56B9E0");
    public static final int NIGHTLIFE_CATEGORY_COLOR = getColorInt("#603A16");
    public static final int RESTAURANTS_CATEGORY_COLOR = getColorInt("#C0D441");
    public static final int SERVICES_CATEGORY_COLOR = getColorInt("#ED2528");
    public static final int SHOP_CATEGORY_COLOR = getColorInt("#006837");
    public static final int SPIRITUAL_CATEGORY_COLOR = getColorInt("#683093");
    public static final int SPORTS_CATEGORY_COLOR = getColorInt("#AA272D");
    public static final int TRANSPORT_CATEGORY_COLOR = getColorInt("#009144");
    public static final int TRAVEL_CATEGORY_COLOR = getColorInt("#9D1F5F");
    public static final int PAYCHECK_CATEGORY_COLOR = getColorInt("#29AAE3");
    public static final int GIFT_CATEGORY_COLOR = getColorInt("#E63B43");
    public static final int INTEREST_CATEGORY_COLOR = getColorInt("#009144");
    public static final int OTHER_CATEGORY_COLOR = getColorInt("#932B8E");

    public static int getColorInt(String color) {
        return Color.parseColor(color);
    }
}
