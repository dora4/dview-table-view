dview-table-view
![Release](https://jitpack.io/v/dora4/dview-table-view.svg)
--------------------------------

#### 运行效果
![图片_20250926163600_13](https://github.com/user-attachments/assets/f3b53efe-d9d5-4d29-b315-b2461850396c)

#### 卡片
![DORA视图 古老的石板](https://github.com/user-attachments/assets/b5094707-759e-4cd3-9275-f8c9859adf31)

#### Gradle依赖配置

```groovy
// 添加以下代码到项目根目录下的build.gradle
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
// 添加以下代码到app模块的build.gradle
dependencies {
    implementation 'com.github.dora4:dview-table-view:1.12'
}
```

#### 使用方式
activity_table_view.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    tools:context=".ui.TableViewActivity">

    <data>

    </data>

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical">

        <dora.widget.DoraTitleBar
            android:id="@+id/titleBar"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:background="@color/colorPrimary"
            app:dview_title="@string/common_title" />

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical">

            <androidx.appcompat.widget.AppCompatImageView
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:scaleType="centerCrop"
                android:src="@drawable/by_tamagotchi" />
            <dora.widget.DoraTableView
                android:id="@+id/tableView"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_margin="10dp"
                app:dview_tv_orientation="vertical"
                app:dview_tv_dividerColor="@color/colorDivider"
                app:dview_tv_dividerSize="1dp" />
        </LinearLayout>
    </LinearLayout>

</layout>
```
Kotlin代码。
```kt
val data = listOf(
            TableCell("权益类别", isBold = true, textColor = Color.WHITE, backgroundColor = ContextCompat.getColor(this, dora.widget.colors.R.color.light_gray)),
            TableCell("普通用户", isBold = true, textColor = Color.WHITE, backgroundColor = ContextCompat.getColor(this, dora.widget.colors.R.color.light_gray)),
            TableCell("VIP会员", isBold = true, textColor = Color.WHITE, backgroundColor = ContextCompat.getColor(this, dora.widget.colors.R.color.light_gray)),

            TableCell("AES加/解密功能"),
            TableCell("✅ 支持"),
            TableCell("✅ 支持"),

            TableCell("RSA加/解密功能"),
            TableCell("✅ 支持"),
            TableCell("✅ 支持"),

            TableCell("文件加/解密功能"),
            TableCell("❌ 不支持"),
            TableCell("✅ 支持"),

            TableCell("去中心化时间锁功能"),
            TableCell("❌ 不支持"),
            TableCell("✅ 支持"),

            TableCell("授权平台数量"),
            TableCell("2 个"),
            TableCell("10 个"),

            TableCell("授权账号数量"),
            TableCell("10 个"),
            TableCell("100 个"),

            TableCell("直登账号数量"),
            TableCell("10 个"),
            TableCell("100 个"),

            TableCell("账号矩阵超额规则"),
            TableCell("/"),
            TableCell("\uD83D\uDD39 超出授权平台，每 10 个位置 / 19.8 POL\n" +
                    "\uD83D\uDD39 超出授权账号或直登账号，每 100 个位置 / 19.8 POL")
        )
        binding.tableView.setData(data, 3)
```
