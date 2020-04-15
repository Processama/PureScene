package com.example.purescene.view.map;

public interface IMapView {
    public void setNormalMap();
    public void setSalliteMap();
    public void setSituation(boolean situation);
    public void setTemeprature(boolean temperature);
    public void setSearchLayoutGone();
    public void routeSearch(String start, String end, int way);
}
