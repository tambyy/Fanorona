LOCAL_PATH := $(call my-dir)

F_SRC_FILES := \
	bitboard.cpp evaluate.cpp fanorona.cpp movegen.cpp movepicker.cpp searchs.cpp tt.cpp

MY_ARCH_DEF :=
ifeq ($(TARGET_ARCH_ABI),arm64-v8a)
  MY_ARCH_DEF += -DIS_64BIT -DUSE_POPCNT
endif
ifeq ($(TARGET_ARCH_ABI),x86_64)
  MY_ARCH_DEF += -DIS_64BIT
endif

include $(CLEAR_VARS)
LOCAL_MODULE    := f95
LOCAL_SRC_FILES := $(F_SRC_FILES)
LOCAL_CFLAGS    := -std=c++11 -O2 -fPIE $(MY_ARCH_DEF) -s
LOCAL_LDFLAGS	+= -fPIE -pie -s
include $(BUILD_SHARED_LIBRARY)
