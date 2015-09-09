package com.eelaiwind.horus.timeChart;

public enum TimeCategory{
    BUSY(0,"busy",0xffFF0000),
    FREE(1,"free",0xff00FF00),
    OFF(2,"off",0xffAAAAAA),
    UNKNOWN(3,"unknown",0xff660099);

    private int index, color;
    private String name;

    TimeCategory(int index,String name,int color){
        this.color = color;
        this.name = name;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public int getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public static TimeCategory getTimCategory(int index,TimeCategory defaultCategory) {
        for (TimeCategory temp : TimeCategory.values()){
            if (temp.getIndex() == index){
                return temp;
            }
        }
       return defaultCategory;
    }
}
