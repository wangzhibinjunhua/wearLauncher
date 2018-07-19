LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE_TAGS := optional

LOCAL_SRC_FILES := $(call all-java-files-under, src) 
LOCAL_STATIC_JAVA_LIBRARIES := launcher-snapshot launcher-gson launcher-v4 launcher-v7 launcher-amap launcher-okhttp launcher-okio                
LOCAL_RESOURCE_DIR += $(LOCAL_PATH)/res
LOCAL_ASSET_FILES += $(call find-subdir-assets)

LOCAL_AAPT_FLAGS += -c ldpi
                    
LOCAL_PACKAGE_NAME := ScznWearLauncher
LOCAL_CERTIFICATE := platform
LOCAL_SDK_VERSION := current

include $(BUILD_PACKAGE)

include $(CLEAR_VARS)
LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES := launcher-snapshot:libs/core-3.2.2-SNAPSHOT.jar
LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES += launcher-gson:libs/gson-2.3.1.jar
LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES += launcher-v4:libs/android-support-v4.jar
LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES += launcher-v7:libs/android-support-v7-recyclerview.jar
LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES += launcher-amap:libs/AMap_Location_V3.3.0_20170118.jar
LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES += launcher-okhttp:libs/okhttp-2.5.0.jar
LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES += launcher-okio:libs/okio-1.6.0.jar
include $(BUILD_MULTI_PREBUILT)

# Use the folloing include to make our test apk.
#include $(call all-makefiles-under,$(LOCAL_PATH))

