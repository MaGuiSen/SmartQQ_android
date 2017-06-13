
@rem 直接安装已经生成的release.apk
@echo call adb devices
call adb devices

@echo push package to mobile
call adb push .\app\build\outputs\apk\app-release.apk /data/local/tmp/com.ma.qqmsg

@echo install app
call adb shell pm install -r "/data/local/tmp/com.ma.qqmsg"

@echo build debug
call adb shell am start -n "com.ma.qqmsg/com.ma.qqmsg.MainActivity" -a android.intent.action.MAIN -c android.intent.category.LAUNCHER
