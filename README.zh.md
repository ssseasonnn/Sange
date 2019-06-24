![](https://raw.githubusercontent.com/ssseasonnn/Sange/master/sange_icon.png)

[![](https://jitpack.io/v/ssseasonnn/Sange.svg)](https://jitpack.io/#ssseasonnn/Sange)

# Sange(散华)

*Read this in other languages: [中文](README.zh.md), [English](README.md)*

一个快速实现RecyclerView分页加载的轻量级库.


> 物品介绍：
>
> 散华是一件异常精准的武器。它具有不可思议的灵性，就好像它会自己寻找对手的弱点进行攻击。
>
> 增加16点的力量。
>
> 增加10点的攻击力。
>
> 残废（被动）：在攻击中有15%的几率使目标残废。残废效果降低目标20%的移动速度，持续4秒。

### 准备

1. 添加jitpack到build.gradle
```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

2. 添加依赖

```gradle
dependencies {
    // 替换 xyz 为具体的版本号, 例如 1.0.0
	implementation 'com.github.ssseasonnn:Sange:xyz'
}
```

### 开始使用

散华核心的功能是**DataSource**, 利用它,只需几个简单的步骤,即可轻松实现数据的初始化及分页加载.

在此之前, 我们得先定义好我们的数据类型, 例如:

```kotlin
class NormalItem(val number: Int): SangeItem
```

> 如你所见, 数据类型实现了**SangeItem**接口, 不过这一步不是必须的, 除非你需要实现多Item类型


接下来创建你自己的DataSource, 你可以选择继承**DataSource**,或者继承**MultiDataSource**,
区别在于**MultDataSource**有着更多的功能, 例如添加Header或者添加Footer.

这里我们继承了**MultiDataSource**, 并把**SangeItem**当作泛型参数, 然后实现**loadInitial**和**loadAfter**方法:

```kotlin
class CustomDataSource : MultiDataSource<SangeItem>() {

    override fun loadInitial(loadCallback: LoadCallback<SangeItem>) {

        //loadInitial 将会在子线程中调用, 因此无需担心任何耗时操作
        Thread.sleep(2000)

        // 加载数据
        val items = mutableListOf<SangeItem>()
        for (i in 0 until 10) {
            items.add(NormalItem(i))
        }

        //将加载之后的数据传递给 LoadCallback, 即可轻松更新RecyclerView
        loadCallback.setResult(items)
    }

    override fun loadAfter(loadCallback: LoadCallback<SangeItem>) {
        //loadAfter 将会在子线程中调用, 因此无需担心任何耗时操作
        Thread.sleep(2000)

        val items = mutableListOf<SangeItem>()
        for (i in page * 10 until (page + 1) * 10) {
            items.add(NormalItem(i))
        }

        loadCallback.setResult(items)
    }
}

```

loadInitial和loadAfter方法都将在子线程中调用, 因此无需担心在这两个方法中做的任何耗时操作.

数据加载完成后, 只需调用LoadCallback的setResult(list)方法即可, 散华会替你做好其他的一切工作,
包括线程切换,通知界面更新等, 你需要做的, 仅仅只是关注于数据的加载.

2. 下一步就是创建一个你自己的Adapter,通过继承散华提供的**SangeAdapter**, 你可以轻松的将DataSource结合起来.

例如:

```kotlin
class NormalAdapter(dataSource: DataSource<NormalItem>) :
    SangeAdapter<NormalItem, NormalViewHolder>(dataSource) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NormalViewHolder {
        return NormalViewHolder(inflate(parent, R.layout.view_holder_normal))
    }

    override fun onBindViewHolder(holder: NormalViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }
}
```

3. 最后, 将RecyclerView和Adapter关联起来:

```kotlin
recycler_view.layoutManager = LinearLayoutManager(this)
recycler_view.adapter = NormalAdapter(NormalDataSource())
```

就是这样, 你无需关心分页的逻辑, 你只需要专注于你真正应该关注的东西: 数据, 其他的就交给散华吧!

## 显示加载状态

到目前为止我们一切进展很顺利, 可是似乎缺少了分页加载的进度显示, 下面来实现它吧.

在DataSource中, 除了loadInitial和loadAfter我们还有一个额外的方法: **onStateChanged(newState)**.

这个方法会在分页加载的不同阶段来调用,用来告诉我们目前DataSource的状态, 例如加载中, 加载失败, 加载成功等.

通过实现这个方法, 我们便可以自行控制加载状态的显示与否, 以及对显示的样式进行定制, 例如:

```kotlin
class NormalDataSource : DataSource<NormalItem>() {

    override fun loadInitial(loadCallback: LoadCallback<NormalItem>) {
        //...
    }

    override fun loadAfter(loadCallback: LoadCallback<NormalItem>) {
        //...
    }

    override fun onStateChanged(newState: Int) {
        //利用DataSource的setState方法, 添加一个额外的状态Item
        setState(NormalStateItem(state = newState, retry = ::retry))
    }
}
```

通过setState()方法, 我们给这个DataSource添加了一个额外的用于表示状态的Item条目, 因此, 我们需要稍微改造一下我们的Adapter, 以便于能够正确的显示出加载的状态.

```kotlin
class NormalAdapter(dataSource: DataSource<NormalItem>) :
        SangeAdapter<NormalItem, NormalViewHolder>(dataSource) {

    //...

    override fun onBindViewHolder(holder: NormalViewHolder, position: Int) {
        val item = getItem(position)

        //判断当前item的类型, 如果是状态类型, 则渲染状态
        if (item is NormalStateItem) {
            holder.onBindState(item)
        } else {
            holder.onBind(item)
        }
    }

    class NormalViewHolder(containerView: View) :
            RecyclerView.ViewHolder(containerView) {

        //...

        fun onBindState(t: NormalStateItem) {
            itemView.tv_state_content.setOnClickListener {
                t.retry()
            }

            when {
                t.state == FetchingState.FETCHING -> {
                    itemView.state_loading.visibility = View.VISIBLE
                    itemView.tv_state_content.visibility = View.GONE
                }
                t.state == FetchingState.FETCHING_ERROR -> {
                    itemView.state_loading.visibility = View.GONE
                    itemView.tv_state_content.visibility = View.VISIBLE
                }
                t.state == FetchingState.DONE_FETCHING -> {
                    itemView.state_loading.visibility = View.GONE
                    itemView.tv_state_content.visibility = View.GONE
                }
                else -> {
                    itemView.state_loading.visibility = View.GONE
                    itemView.tv_state_content.visibility = View.GONE
                }
            }
        }
    }
}

```

这里我们只是简单的渲染单一类型数据的示例, 因此并没有利用ViewType来实现多种类型的item显示, 只是简单的判断了数据的类型进而渲染不同类型的UI视图.

最终效果图:

![](https://github.com/ssseasonnn/Sange/raw/master/normal.gif)


## 多Item类型显示

截止目前, 我们已经取得了很好的效果, 然后我们来了解一下一些更酷一点的使用方式.

假如我们需要的是一个复杂一点的显示，包含了多种视图类型，或者是需要显示头部和尾部，没关系，散华可以帮你轻松做到．

1. 同样的，我们首先定义好需要的多种数据类型：

```kotlin
const val NORMAL = 0
const val HEADER = 1
const val FOOTER = 2
const val STATE = 3

open class NormalItem(val i: Int) : SangeItem {
    override fun viewType() = NORMAL
}

class HeaderItem(val i: Int) : SangeItem {
    override fun viewType() = HEADER
}

class FooterItem(val i: Int) : SangeItem {
    override fun viewType() = FOOTER
}

//别忘了我们的状态类型
class StateItem(val state: Int, val retry: () -> Unit) : SangeItem {
    override fun viewType() = STATE
}
```

你是否发现我们的类型都实现了**SangeItem**接口, 是的, 为了多ViewType渲染, 我们定义了一个通用的item类型.

将所有的类型都实现这个接口, 并实现该接口的viewType()方法, 在该方法中返回对应的类型值.

2. 接着,我们调整一下我们的DataSource, 这次我们使用功能更强大的**MultiDataSource** :

```kotlin
class DemoDataSource : MultiDataSource<SangeItem>() {

    override fun loadInitial(loadCallback: LoadCallback<SangeItem>) {
        //获取Header头部数据
        val headers = mutableListOf<SangeItem>()
        for (i in 0 until 2) {
            headers.add(HeaderItem(i))
        }
        //添加头部数据到DataSource中
        addHeaders(headers)

        //获取Footer尾部数据
        val footers = mutableListOf<SangeItem>()
        for (i in 0 until 2) {
            footers.add(FooterItem(i))
        }

        //添加尾部数据到DataSource中
        addFooters(footers)

        //获取正文数据
        val items = mutableListOf<SangeItem>()
        for (i in 0 until 10) {
            items.add(NormalItem(i))
        }

        loadCallback.setResult(items)
    }

    override fun loadAfter(loadCallback: LoadCallback<SangeItem>) {
        //...
    }

    override fun onStateChanged(newState: Int) {
        setState(StateItem(newState, ::retry))
    }
}
```

看起来没啥区别, 只不过使用了通用的SangeItem类型.

3. 最后再调整一下Adapter, 这里同样使用更强大的**SangeMultiAdapter** :

```kotlin
class DemoAdapter(dataSource: DataSource<SangeItem>) :
        SangeMultiAdapter<SangeItem, SangeViewHolder<SangeItem>>(dataSource) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SangeViewHolder<SangeItem> {
        return when (viewType) {
            NORMAL -> NormalViewHolder(inflate(parent, R.layout.view_holder_normal))
            HEADER -> HeaderViewHolder(inflate(parent, R.layout.view_holder_header))
            FOOTER -> FooterViewHolder(inflate(parent, R.layout.view_holder_footer))
            STATE -> StateViewHolder(inflate(parent, R.layout.view_holder_state))
            else -> throw  IllegalStateException("not support this view type:[$viewType]")
        }
    }
}
```

到此为止, 剩下的就交给散华吧!

效果图:

![](https://github.com/ssseasonnn/Sange/raw/master/multi.gif)

### License

> ```
> Copyright 2019 Season.Zlc
>
> Licensed under the Apache License, Version 2.0 (the "License");
> you may not use this file except in compliance with the License.
> You may obtain a copy of the License at
>
>    http://www.apache.org/licenses/LICENSE-2.0
>
> Unless required by applicable law or agreed to in writing, software
> distributed under the License is distributed on an "AS IS" BASIS,
> WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
> See the License for the specific language governing permissions and
> limitations under the License.
> ```
