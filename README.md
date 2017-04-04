# XRefreshLayout
A refresh layout, can refresh RecyclerView for all LayoutManager, NestedScrollView and Any View that implements NestedScrollChild !



# Feature

- support RecyclerView for all LayoutManager in vertical orientation !
- support NestedScrollView !
- support any view that implements NestedScrollChild !
- support custom refresh header and footer , which means you can make you wanderful animation !
- not support ListView, GridView, ScrollView !



# Screenshot

- refresh RecyclerView for StaggeredGridLayoutManager ：

  ![staggered](/Screenshot/staggered.gif)

- refresh RecyclerView for GridLayoutManager ：

  ![grid](/Screenshot/grid.gif)

- refresh RecyclerView for LinearLayoutManager ：

  ![linear](/Screenshot/linear.gif)

- refresh NestedScrollView ：

  ![nestedscroll](/Screenshot/nestedscroll.gif)






# Depedency

**Step 1. Add it in your root build.gradle at the end of repositories:**

```groovy
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

**Step 2.** **Add the dependency**

 [![](https://jitpack.io/v/li-xiaojun/XRefreshLayout.svg)](https://jitpack.io/#li-xiaojun/XRefreshLayout)

```groovy
dependencies {
	compile 'com.github.li-xiaojun:XRefreshLayout:lastest release here'
}
```





# Get Start

- set refresh listener

  ```java
  xrefreshLayout.setOnRefreshListener(new XRefreshLayout.OnRefreshListener() {
              @Override
              public void onRefresh() {
              }

              @Override
              public void onLoadMore() {
              }
          });
  ```

- finish refresh

  ```java
  xrefreshLayout.completeRefresh();
  ```

- set custom loadinglayout

  ```java
  //you can see the DefaultLoadingLayout for some help.
  xrefreshLayout.setLoadingLayout(...);
  ```

  ​