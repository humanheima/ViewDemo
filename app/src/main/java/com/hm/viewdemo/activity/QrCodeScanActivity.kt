package com.hm.viewdemo.activity

import com.hm.viewdemo.base.BaseActivity
import com.hm.viewdemo.databinding.ActivityQrCodeScanBinding

class QrCodeScanActivity : BaseActivity<ActivityQrCodeScanBinding>() {

    override fun createViewBinding(): ActivityQrCodeScanBinding {
        return ActivityQrCodeScanBinding.inflate(layoutInflater)
    }

    override fun initData() {}
}
