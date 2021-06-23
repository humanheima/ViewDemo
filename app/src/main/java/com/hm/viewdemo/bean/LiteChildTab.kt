package com.hm.viewdemo.bean


/**
 * Created by dumingwei on 2021/6/16
 *
 * Desc:
 */
class LiteChildTab(
        var tabId: Long?,
        var title: String?,
        var selectd: Boolean//本地变量，标记是否选中
) {

    override fun toString(): String {
        return "LiteChildTab(tabId=$tabId, title=$title, selectd=$selectd)"
    }
}