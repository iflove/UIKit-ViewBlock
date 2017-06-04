# UIKit-ViewBlock

> 简介：快速代替fragment的轻量级解耦类,能对复杂布局划分Block...


----------

项目中用 fragment 来降低 activity 的复杂度，但 fragment 带来的各种奇葩问题让我想死。**ViewBlock** 与 **View** 绑定在一起大大降低 activity 的复杂度。
<br/>


- **支持 layout 文件 的 name/block_class 属性快捷地绑定ViewBlock, 只需Activity 继承 UIKitActivity, or 直接使用 UIKit Layout**
- **ViewBlock 的所有生命周期和 activity 完全保持一致,适配了部分常用 Activity/Context api**
- **支持fragment include 标签，兼容Fragment**
- **能切换ViewBlock,并支持动画效果**
- **支持ViewPager+ Tab 切换**
- **支持动态使用ViewBlock**


-------------------



## NOTE

 1. 目前，本版本仍在开发中...
