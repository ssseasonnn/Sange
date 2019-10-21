![](https://raw.githubusercontent.com/ssseasonnn/Sange/master/sange_icon.png)

[![](https://jitpack.io/v/ssseasonnn/Sange.svg)](https://jitpack.io/#ssseasonnn/Sange)

# Sange



*Read this in other languages: [中文](README.zh.md), [English](README.md)*

> Item introduction：
> Sange is a versatile Adapter for RecyclerView.
> 
> Features:
> - Support for initial data loading
> - Support data paging loading
> - Support for MultiViewType
> - Support for Header and Footer
> - Support DiffUtil
> - Support Loading State
> - Support CleanUp, free resources to avoid memory leaks

## Prepare

1. Add jitpack
```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

2. Add dependency

```gradle
dependencies {
    // Replace xyz with a specific version number, for example 1.0.0
	implementation 'com.github.ssseasonnn:Sange:xyz'
}
```

## Start

### First Blood

Basic usage:

- Customize data types and implement **SangeItem** interface

    ```kotlin
    class NormalItem(val i: Int) : SangeItem
    ```

- Create a DataSource, override the **loadInitial** and **loadAfter** methods,
They are automatically triggered when the page is initialized and the next page of data needs to be loaded.

    ```kotlin
    class DemoDataSource : SangeDataSource<SangeItem>() {
    
        override fun loadInitial(loadCallback: LoadCallback<SangeItem>) {
            //Load data
            val items = mutableListOf<SangeItem>()
            for (i in 0 until 10) {
                items.add(NormalItem(i))
            }
          
            //Set the initial loading result
            loadCallback.setResult(items)
          
            //Show blank page
            //loadCallback.setResult(emptyList())
          
            //Display loading failure page
            //loadCallback.setResult(null)
        }
    
        override fun loadAfter(loadCallback: LoadCallback<SangeItem>) {
            //...
          
            //Set paging load results
            loadCallback.setResult(items)
          
            //Stop paging loading
            //loadCallback.setResult(emptyList())
          
            //Display paging load failed
            //loadCallback.setResult(null)
        }
    }
    ```

    > Both the loadInitial and loadAfter methods will be called in the child thread, 
    so there is no need to worry about any time-consuming operations in both methods.

- Create an Adapter and associate the DataSource.

    ```kotlin
    class DemoAdapter(dataSource: DataSource<SangeItem>) :
            SangeMultiAdapter<SangeItem, SangeViewHolder<SangeItem>>(dataSource) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SangeViewHolder<SangeItem> {
            return NormalViewHolder(inflate(parent, R.layout.view_holder_normal))
        }
    }
    ```

### Double Kill

Custom paging load status

- Create a data type for the state:

    ```kotlin
    class StateItem(val state: Int, val retry: () -> Unit) : SangeItem {
        override fun viewType() = STATE
    }
    ```
    > Also implement the **SangeItem** interface, and override the viewType method, which returns a new Type type in the method.

- Override the **onStateChanged(newState)** method in the DataSource, which will be called based on the state in the load.

    ```kotlin
    class DemoDataSource : SangeDataSource<SangeItem>() {

        override fun loadInitial(loadCallback: LoadCallback<SangeItem>) {
            //...
        }

        override fun loadAfter(loadCallback: LoadCallback<SangeItem>) {
            //...
        }

        override fun onStateChanged(newState: Int) {
            //Add an extra status item
            setState(StateItem(state = newState, retry = ::retry))
        }
    }
    ```

- Provide a ViewHolder that renders the State:

    ```kotlin
    class StateViewHolder(containerView: View) :
            SangeViewHolder<SangeItem>(containerView) {

        override fun onBind(t: SangeItem) {
            super.onBind(t)
            t as StateItem
            
           //Set status view
            when {
                t.state == FetchingState.FETCHING -> {}
                t.state == FetchingState.FETCHING_ERROR -> {}
                t.state == FetchingState.DONE_FETCHING -> {}
                else -> {}
            }
        }
    }
    ```

- Finally, the loading status is displayed.

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

- Refresh and retry

    The DataSource provides the **invalidate()** and **retry()** methods. 
    When you need to refresh the data, you can call the **invalidate()** method.
    When the load fails and needs to be retried, call the **retry()** method.

- Customize DiffCallback

    Sange uses DiffUtil to update RecyclerView efficiently. 
    You can change the comparison logic according to your actual situation:

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

Resource cleanup
 
- SangeItem provides the **cleanUp** method, which is called automatically when the page is destroyed and the data is cleaned up.
So you can release resources here to avoid the risk of memory leaks.

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
