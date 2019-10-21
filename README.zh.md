![](https://raw.githubusercontent.com/ssseasonnn/Sange/master/sange_icon.png)

[![](https://jitpack.io/v/ssseasonnn/Sange.svg)](https://jitpack.io/#ssseasonnn/Sange)

# Sange(散华)



*Read this in other languages: [中文](README.zh.md), [English](README.md)*

> 物品介绍：
> 散华是RecyclerView的一个多功能的Adapter.
> 
> 功能介绍:
> - 支持初始化数据加载
> - 支持数据分页加载
> - 支持MultiViewType
> - 支持Header和Footer
> - 支持DiffUtil
> - 支持Loading State
> - 支持CleanUp, 释放资源避免内存泄漏

## Prepare

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

## Start

### First Blood

基本使用:

- 自定义数据类型, 并实现**SangeItem**接口

    ```kotlin
    class NormalItem(val i: Int) : SangeItem
    ```

- 创建DataSource, 重写**loadInitial**和**loadAfter**方法,
它们分别会在页面初始化和需要加载下一页数据时自动触发.

    ```kotlin
    class DemoDataSource : SangeDataSource<SangeItem>() {
    
        override fun loadInitial(loadCallback: LoadCallback<SangeItem>) {
            //加载数据
            val items = mutableListOf<SangeItem>()
            for (i in 0 until 10) {
                items.add(NormalItem(i))
            }
          
            //设置初始化加载结果
            loadCallback.setResult(items)
          
            //显示空白页面
            //loadCallback.setResult(emptyList())
          
            //显示加载失败页面
            //loadCallback.setResult(null)
        }
    
        override fun loadAfter(loadCallback: LoadCallback<SangeItem>) {
            //...
          
            //设置分页加载结果
            loadCallback.setResult(items)
          
            //停止分页加载
            //loadCallback.setResult(emptyList())
          
            //显示分页加载失败
            //loadCallback.setResult(null)
        }
    }
    ```

    > loadInitial和loadAfter方法都将在子线程中调用, 因此无需担心在这两个方法中做的任何耗时操作.

- 创建Adapter,并关联DataSource.

    ```kotlin
    class DemoAdapter(dataSource: DataSource<SangeItem>) :
            SangeMultiAdapter<SangeItem, SangeViewHolder<SangeItem>>(dataSource) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SangeViewHolder<SangeItem> {
            return NormalViewHolder(inflate(parent, R.layout.view_holder_normal))
        }
    }
    ```

### Double Kill

自定义分页加载状态

- 创建状态的数据类型:

    ```kotlin
    class StateItem(val state: Int, val retry: () -> Unit) : SangeItem {
        override fun viewType() = STATE
    }
    ```
    > 同样实现**SangeItem**接口, 并且重写viewType方法, 在该方法中返回了一个新的Type类型

- 重写DataSource中的 **onStateChanged(newState)** 方法, 该方法会根据加载中的状态进行调用.

    ```kotlin
    class DemoDataSource : SangeDataSource<SangeItem>() {

        override fun loadInitial(loadCallback: LoadCallback<SangeItem>) {
            //...
        }

        override fun loadAfter(loadCallback: LoadCallback<SangeItem>) {
            //...
        }

        override fun onStateChanged(newState: Int) {
            //利用DataSource的setState方法, 添加一个额外的状态Item
            setState(StateItem(state = newState, retry = ::retry))
        }
    }
    ```

- 提供一个渲染State的ViewHolder:

    ```kotlin
    class StateViewHolder(containerView: View) :
            SangeViewHolder<SangeItem>(containerView) {

        override fun onBind(t: SangeItem) {
            super.onBind(t)
            t as StateItem
            
           //设置状态视图
            when {
                t.state == FetchingState.FETCHING -> {}
                t.state == FetchingState.FETCHING_ERROR -> {}
                t.state == FetchingState.DONE_FETCHING -> {}
                else -> {}
            }
        }
    }
    ```

- 最后,显示加载状态

    ```kotlin
    class DemoAdapter(dataSource: DataSource<SangeItem>) :
            SangeMultiAdapter<SangeItem, SangeViewHolder<SangeItem>>(dataSource) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SangeViewHolder<SangeItem> {
            return when (viewType) {
                STATE -> StateViewHolder(inflate(parent, R.layout.view_holder_state))
                else -> NormalViewHolder(inflate(parent, R.layout.view_holder_normal))
            }
        }
    }

    ```

### Triple Kill

- 刷新与重试

    散华的DataSource提供了 **invalidate()** 和 **retry()** 方法, 当需要刷新数据时, 调用 **invalidate()** 方法即可,
    当加载失败需要重试时, 调用 **retry()** 方法即可

- 定制DiffCallback

    散华使用了DiffUtil来高效的更新RecyclerView,你可以根据你的实际情况来改变比较逻辑:

    ```kotlin
    class NormalItem(val i: Int) : SangeItem {

        override fun areContentsTheSame(other: Differ): Boolean {
            //use your own diff logic
            //...
        }

        override fun areItemsTheSame(other: Differ): Boolean {
            //use your own diff logic
            //...
        }

        override fun getChangePayload(other: Differ): Any? {
            //...
        }
    }
    ```

### Ultra Kill

资源清理, SangeItem提供了**cleanUp** 方法, 该方法会在页面销毁和数据被清理时自动调用,
因此你可以在此进行资源的释放, 以避免内存泄漏的风险.

    ```kotlin
    class NormalItem(val i: Int) : SangeItem {
    
        private val thread: Thread
    
        var stop = false
    
        init {
            //Test auto clean up!!
            thread = thread {
                for (i in 0..100) {
                    if (stop) {
                        break
                    }
                    Log.d("Sange", "$i")
                    Thread.sleep(1000)
                }
            }
    
        }
    
        override fun cleanUp() {
            //释放线程资源
            stop = true
        }
    }
    ```

## END
    

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
