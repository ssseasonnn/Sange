![](https://raw.githubusercontent.com/ssseasonnn/Sange/master/sange_icon.png)

<p align="left">
	<img src="https://img.shields.io/badge/kotlin-1.8.0-green"/>
	<a href="https://jitpack.io/#ssseasonnn/Sange">
		<img src="https://jitpack.io/v/ssseasonnn/Sange.svg"/>
	</a>
</p>

# Sange

一个RecyclerView的多功能Adapter.

> *Read this in other languages: [中文](README.zh.md), [English](README.md)*

## 功能介绍：

✅ 支持MultiType <br/>
✅ 支持Header和Footer <br/>
✅ 支持自动分页加载 <br/>
✅ 支持DiffUtils刷新 <br/>
✅ 支持加载状态显示 <br/>
✅ 支持CleanUp自动清理资源 <br/>

## Prepare

1. 添加jitpack到build.gradle

```gradle  
allprojects {  
    repositories {
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

## 基本用法

自定义数据类型, 并实现**SangeItem**接口:

```kotlin
class NormalItem(val i: Int) : SangeItem
```

创建DataSource, 重写**loadInitial**和**loadAfter**方法,它们分别会在页面初始化和需要加载下一页数据时自动触发:

```kotlin
class DemoDataSource : SangeDataSource<SangeItem>() {

    override suspend fun loadInitial(): List<SangeItem>? { {
        //加载数据
        val items = mutableListOf<SangeItem>()
        for (i in 0 until 10) {
            items.add(NormalItem(i))
        }
      
        //返回初始化加载数据
        return items
      
        //返回空数组即可显示空白页面
        //return emptyList()
      
        //返回null即可显示加载失败页面
        //return null
    }

    override suspend fun loadAfter(): List<SangeItem>? {
        //...
        
		//返回分页加载数据
        return items
      
        //返回空数组停止分页加载
        //return emptyList()
      
        //返回null显示分页加载失败
        //return null
    }
}
```


创建Adapter,并关联DataSource:

```kotlin
class DemoAdapter(dataSource: DataSource<SangeItem>) :
        SangeMultiAdapter<SangeItem, SangeViewHolder<SangeItem>>(dataSource) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SangeViewHolder<SangeItem> {
        return NormalViewHolder(inflate(parent, R.layout.view_holder_normal))
    }
}
```

以上这几步即可完成对RecyclerView的渲染。

## 其余配置

### 1.自定义分页加载状态

创建加载状态的数据类型:

```kotlin
class StateItem(val state: Int, val retry: () -> Unit) : SangeItem {
    override fun viewType() = STATE
}
```
> 同样实现**SangeItem**接口, 并且重写viewType方法, 在该方法中返回了一个新的Type类型

重写DataSource中的 **onStateChanged(newState)** 方法, 该方法会根据加载中的状态进行调用.

```kotlin
class DemoDataSource : SangeDataSource<SangeItem>() {

    override fun onStateChanged(newState: Int) {
        //利用DataSource的setState方法, 添加一个额外的状态Item
        setState(StateItem(state = newState, retry = ::retry))
    }
}
```

提供渲染状态的ViewHolder:

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

最后,显示加载状态：

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

### 2. 刷新与重试

```kotlin
//刷新dataSource, clear表示是否清除之前的数据
dataSource.invalidate(clear=true)

//重试，遇到加载错误时可通过该方法进行重试
dataSource.retry()
```

### 3. 局部刷新

Sange使用了DiffUtil来高效的更新RecyclerView，可通过重写数据类中的方法告知Sange如何刷新：

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

### 4. 资源清理

SangeItem接口提供了**cleanUp()** 方法, 该方法可在页面销毁和数据项被移除时自动调用，因此你可以在此进行资源的释放, 以避免内存泄漏的风险.

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
