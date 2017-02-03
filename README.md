# MRecyclerView
读XRecyclerView的练习之作

如果想要使用有类似功能的三方，可以移步[XRecyclerView](https://github.com/jianghejie/XRecyclerView)。

# 通过这仓库的代码你可以了解哪些东西？
* 简单的事件处理
* 简单的事件传递
* 简单的属性动画的使用

实现思路是通过阅读XRecyclerView的源码来获得灵感的，我在MRecyclerView中通过操作RefreshHeader接口来控制一系列事件发生时的控件形态，用接口么，总是想要解耦的。你完全可以抛弃我实现的MRefreshHeader来尝试自己实现一个~  由于我本意并非是想封装一个完善的第三方下拉刷新库，所以就不抽出更多的方法了，简单的实现下就完事~
