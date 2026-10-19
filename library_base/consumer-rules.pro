-keep class com.ls.libbase.bean.**{*;}
# 保留 ARouterPath 类及其所有子类和成员（防止类名或字段被混淆）
-keep class com.ls.libbase.config.ARouterPath { *; }
-keep class com.ls.libbase.config.ARouterPath$* { *; }  # 保留所有内部类