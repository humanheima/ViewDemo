package com.brotherd.pullrefresh;

/**
 * Created by shucc on 17/3/29.
 * cc@cchao.org
 */
public interface OnCheckCanDoRefreshListener {

    boolean checkCanPullStart();
    boolean checkCanPullEnd();
}
