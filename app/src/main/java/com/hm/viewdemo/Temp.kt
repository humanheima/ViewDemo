package com.hm.viewdemo

import org.libpag.PAGView

class Temp {


    /**
     * 有日月轮转背景情况下的装扮
     *
     * 1. 取第一个区间的背景 + 过渡背景 播放 && 取 第一个区间的日月轮转其他效果进行播放。
     * 2. 背景 + 过渡背景 播放结束；取下一个区间的背景 + 过渡背景 播放  && 取 下一个区间的日月轮转其他效果进行播放
     *
     * @param sunMoonBgList 日月轮转的背景列表
     */
//    private fun dressWithSunMoonBg(
//        newCharData: CabinHomePage.CharacterInfo,
//        sunMoonBgList: List<DressUpItem>,
//        index: Int
//    ) {
//        Logger.i(TAG, "dressWithSunMoonBg，当前区间的信息 index = $index")
//
//        //日月轮换的背景列表
//        val size = sunMoonBgList.size
//
//        //第一个区间的背景信息，包括背景和过渡背景
//        val item = sunMoonBgList[index]
//
//        //第一个区间的 背景和过渡背景 pagUrl
//        val bgUrlList = listOf(item.goodsImage, item.fadeImage)
//
//        //小精灵
//        val spiritDressUpItem = newCharData.adornInfo?.adornmentByType(AdornmentType.SPIRIT)
//        val spiritUrlList = getPagUrlList(spiritDressUpItem)
//
//        //氛围动效
//        val atmosphereDressUpItem = newCharData.adornInfo?.adornmentByType(AdornmentType.ATMOSPHERE)
//        val atmosphereUrlList = getPagUrlList(atmosphereDressUpItem)
//
//        cabinContainerView?.setBgList(bgUrlList, true, object : PAGViewListenerImpl() {
//
//            override fun onAnimationEnd(view: PAGView?) {
//                val nextIndex = (index + 1) % size
//                Logger.i(TAG, "onAnimationEnd，播放下一个区间的信息 nextIndex = $nextIndex")
//
//                dressWithSunMoonBg(newCharData, sunMoonBgList, nextIndex)
//            }
//        })
//        if (spiritUrlList.isNullOrEmpty() || index >= spiritUrlList.size) {
//            cabinContainerView?.setSpiritFairyUrl(item.goodsImageWebp)
//        } else {
//            cabinContainerView?.setSpiritFairyUrl(spiritUrlList[index])
//        }
//
//        if (atmosphereUrlList.isNullOrEmpty() || index >= atmosphereUrlList.size) {
//            cabinContainerView?.setAtmosphereUrl(item.goodsImage)
//        } else {
//            cabinContainerView?.setAtmosphereUrl(atmosphereUrlList[index])
//        }
//
//        //装饰按钮
//        previewView?.dressUpButtonPreview2(
//            newCharData.adornInfo?.adornmentByType(AdornmentType.ADORN_BUTTON),
//            index
//        )
//
//
//    }
}