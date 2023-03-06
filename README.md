![](https://raw.githubusercontent.com/ssseasonnn/Sange/master/sange_icon.png)

<p align="left">
	<img src="https://img.shields.io/badge/kotlin-1.8.0-green"/>
	<a href="https://jitpack.io/#ssseasonnn/Sange">
		<img src="https://jitpack.io/v/ssseasonnn/Sange.svg"/>
	</a>
</p>

# Sange

A super adapter for RecyclerView.

> *Read this in other languages: [中文](README.zh.md), [English](README.md)*

## Feature introduction：

✅ Support MultiType <br/>
✅ Support Header and Footer <br/>
✅ Support automatic paging loading <br/>
✅ Support DiffUtil <br/>
✅ Support loading status display <br/>
✅ Support automatic clean up resources <br/>

## Prepare

1. Add jitpack to build.gradle

```gradle  
allprojects {  
    repositories {
	    maven { url 'https://jitpack.io' }    
	}
}  
```  

2. Add dependency

```gradle  
dependencies {  
    // Replace xyz with the specific version number, for example 1.0.0   
    implementation 'com.github.ssseasonnn:Sange:xyz'
}  
```  

## Basic Usage

Customize the data type and implement the **SangeItem** interface:

```kotlin
class NormalItem(val i: Int) : SangeItem
```

Create a DataSource and override the **loadInitial** and **loadAfter** methods, which will automatically trigger when the page is initialized and the next page of data needs to be loaded:

```kotlin
class DemoDataSource : SangeDataSource<SangeItem>() {

    override suspend fun loadInitial(): List<SangeItem>? { {
        //Load data
        val items = mutableListOf<SangeItem>()
        for (i in 0 until 10) {
            items.add(NormalItem(i))
        }
      
        //Return initialization load data
        return items
      
        //Return an empty array to display a blank page
        //return emptyList()
      
        //Return null to display the loading failure page
        //return null
    }

    override suspend fun loadAfter(): List<SangeItem>? {
        //...
        
		//Returns paging load data
        return items
      
        //Return an empty array to stop paging
        //return emptyList()
      
        //Return null to show paging loading failure
        //return null
    }
}
```


Create an adapter and associate the DataSource:

```kotlin
class DemoAdapter(dataSource: DataSource<SangeItem>) :
        SangeMultiAdapter<SangeItem, SangeViewHolder<SangeItem>>(dataSource) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SangeViewHolder<SangeItem> {
        return NormalViewHolder(inflate(parent, R.layout.view_holder_normal))
    }
}
```

The above steps can complete the rendering of RecyclerView.

## Other configurations

### 1.Custom paging load status

Create the data type of the loading state:

```kotlin
class StateItem(val state: Int, val retry: () -> Unit) : SangeItem {
    override fun viewType() = STATE
}
```
> Also implement the **SangeItem** interface and override the viewType method, in which a new Type type is returned

Overeride the onStateChanged(newState) method in DataSource, which will be called according to the state in loading

```kotlin
class DemoDataSource : SangeDataSource<SangeItem>() {

    override fun onStateChanged(newState: Int) {
        setState(StateItem(state = newState, retry = ::retry))
    }
}
```

Provide ViewHolder for rendering state:

```kotlin
class StateViewHolder(containerView: View) :
        SangeViewHolder<SangeItem>(containerView) {

    override fun onBind(t: SangeItem) {
        super.onBind(t)
        t as StateItem
        
       //Set Status View
        when {
            t.state == FetchingState.FETCHING -> {}
            t.state == FetchingState.FETCHING_ERROR -> {}
            t.state == FetchingState.DONE_FETCHING -> {}
            else -> {}
        }
    }
}
```

Finally, the loading status is displayed:

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

### 2. Refresh and Retry

```kotlin
//Refresh dataSource, clear indicates whether to clear the previous data
dataSource.invalidate(clear=true)

//Retry. This method can be used to retry when loading errors are encountered
dataSource.retry()
```

### 3. Partial refresh

Sange uses DiffUtil to update the RecyclerView efficiently. You can tell Sange how to refresh by rewriting the methods in the data class:

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

### 4. Resource cleanup

The SangeItem interface provides **cleanUp()** method, which can be automatically called when the page is destroyed and the data item is removed, so you can release resources here to avoid the risk of memory leakage

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
        //Release thread resources
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
