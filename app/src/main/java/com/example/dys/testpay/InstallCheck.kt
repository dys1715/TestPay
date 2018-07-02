package com.example.dys.testpay

import android.content.Context
import android.content.Intent
import android.net.Uri


object InstallCheck {

    /**
     * 检测是否安装支付宝
     * @param context
     * @return
     */
    fun isAliPayInstalled(context: Context): Boolean {
        val uri = Uri.parse("alipays://platformapi/startApp")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        val componentName = intent.resolveActivity(context.packageManager)
        return componentName != null
    }

    /**
     * 判断 用户是否安装微信客户端
     */
    fun isWeChatInstalled(context: Context): Boolean {
        val packageManager = context.packageManager// 获取packagemanager
        val packageInfo = packageManager.getInstalledPackages(0)// 获取所有已安装程序的包信息
        if (packageInfo != null) {
            for (i in packageInfo.indices) {
                val pn = packageInfo[i].packageName
                if (pn == "com.tencent.mm") {
                    return true
                }
            }
        }
        return false
    }

}