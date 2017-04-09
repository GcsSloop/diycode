# RecyclerView 工具包

1. SingleTypeAdapter (单数据类型适配器)
2. MultiTypeAdapter (多数据类型适配器)
3. HeaderFooterAdapter (可以包含头部和尾部的适配器)

其中 MultiTypeAdapter 实现参考了 drakeet 的 [MultiType](https://github.com/drakeet/MultiType) ，其中性能上可能要稍差一点，但使用起来更加方便。

HeaderFooterAdapter 可以随时添加／删除头部和尾部，不影响其中数据。

如果需要处理的单个条目内容复杂繁多时，不推荐使用。