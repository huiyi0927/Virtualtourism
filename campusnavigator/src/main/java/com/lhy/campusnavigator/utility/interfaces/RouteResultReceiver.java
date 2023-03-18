package com.lhy.campusnavigator.utility.interfaces;

import com.lhy.campusnavigator.model.Route;
import com.lhy.campusnavigator.utility.structures.List;

/**
 * @Description
 * @Author John
 * @email
 * @Date 2022/9/5 10:02
 * @Version 1
 */
public interface RouteResultReceiver {
    void onSingleRouteReceive(List<Route> results);
    void onMultiRouteReceive(List<Route> results);
}
