![](https://raw.githubusercontent.com/ssseasonnn/Sange/master/sange_icon.png)

[![](https://jitpack.io/v/ssseasonnn/Sange.svg)](https://jitpack.io/#ssseasonnn/Sange)

# Sange

*Read this in other languages: [中文](README.zh.md), [English](README.md)*

A lightweight library that quickly implements RecyclerView paging loading.


> Item introduction：
>
> Sange is an extremely accurate weapon. It has incredible spirituality, as if it would find its own weaknesses to attack.
>
> Increase the power of 16.
>
> Increase the attack power by 10.
>
> Disabled (passive): There is a 15% chance that the target will be disabled in the attack. The disability effect reduces the target's movement speed by 20% for 4 sec.

## Prepare

1. Add jitpack to build.gradle
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

- The function of the Sange core is **DataSource**, which makes it easy to initialize and page load data in a few simple steps.

    Before that, we have to define our data types first, remember to implement the **SangeItem** interface. For example:

    ```kotlin
    class NormalItem(val number: Int): SangeItem
    ```

- Next create your own DataSource.

    As shown below, we inherit **MultiDataSource** and treat **SangeItem** as a generic parameter, then implement the **loadInitial** and **loadAfter** methods:

    ```kotlin
    class DemoDataSource : MultiDataSource<SangeItem>() {

        override fun loadInitial(loadCallback: LoadCallback<SangeItem>) {

            //loadInitial will be called in the io thread, so there is no need to worry about any time-consuming operations
            Thread.sleep(2000)

            // loading...
            val items = mutableListOf<SangeItem>()
            for (i in 0 until 10) {
                items.add(NormalItem(i))
            }

            //set loading result
            loadCallback.setResult(items)
        }

        override fun loadAfter(loadCallback: LoadCallback<SangeItem>) {
            //loadAfter will be called in the io thread
            Thread.sleep(2000)

            val items = mutableListOf<SangeItem>()
            for (i in page * 10 until (page + 1) * 10) {
                items.add(NormalItem(i))
            }

            loadCallback.setResult(items)
        }
    }

    ```

    Both the loadInitial and loadAfter methods will be called in the child thread, so there is no need to worry about any time-consuming operations in both methods.

    After the data is loaded, just call LoadCallback's setResult(list) method, and Sange will do everything else for you.
    Including thread switching, notification interface updates, etc., you need to do, just focus on the loading of data.

- Next, create a ViewHolder for display. By inheriting the **SangeViewHolder** provided by the Sange, you can omit many other tedious tasks.

    E.g:

    ```kotlin
    class NormalViewHolder(containerView: View) :
            SangeViewHolder<SangeItem>(containerView) {

        override fun onBind(t: SangeItem) {
            t as NormalItem
            tv_normal_content.text = t.toString()
        }
    }
    ```

- The next step is to create your own Adapter. By inheriting the **SangeMultiAdapter** provided by Sange, you can easily combine the DataSources.

    E.g:

    ```kotlin
    class DemoAdapter(dataSource: DataSource<SangeItem>) :
            SangeMultiAdapter<SangeItem, SangeViewHolder<SangeItem>>(dataSource) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SangeViewHolder<SangeItem> {
            return NormalViewHolder(inflate(parent, R.layout.view_holder_normal))
        }
    }
    ```

- Finally, associate the RecyclerView with the Adapter:

    ```kotlin
    recycler_view.layoutManager = LinearLayoutManager(this)
    recycler_view.adapter = DemoAdapter(DemoDataSource())
    ```

    That's it, you don't need to care about the logic of paging, you just need to focus on what you really should pay attention to: Load the data, and leave it to the Sange!

### Double Kill

So far we have all gone well, but it seems that we lack the status display of page load. Let's implement it.

- In order to display the loaded state, we first create a data type that represents the state:

    ```kotlin
    class StateItem(val state: Int, val retry: () -> Unit) : SangeItem {
        override fun viewType() = STATE
    }
    ```
    > As you can see, we also implemented the **SangeItem** interface and implemented the viewType method, which returns a new Type type in the method.

- Then we slightly modify the DataSource, we implement an additional method: **onStateChanged(newState)**.

    ```kotlin
    class DemoDataSource : MultiDataSource<SangeItem>() {

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

    This method will be called at different stages of the page load to tell us the current state of the DataSource, such as loading, loading failure, loading success, etc.
    By implementing this method, we can control the display of the loading state and customize the style of the display.

    As shown above, we have added a Data Item item to represent the status.

- Again, we need a ViewHolder that renders the State:

    ```kotlin
    class StateViewHolder(containerView: View) :
            SangeViewHolder<SangeItem>(containerView) {

        override fun onBind(t: SangeItem) {
            super.onBind(t)
            t as StateItem

            tv_state_content.setOnClickListener {
                t.retry()
            }

            when {
                t.state == FetchingState.FETCHING -> {
                    state_loading.visibility = View.VISIBLE
                    tv_state_content.visibility = View.GONE
                }
                t.state == FetchingState.FETCHING_ERROR -> {
                    state_loading.visibility = View.GONE
                    tv_state_content.visibility = View.VISIBLE
                }
                t.state == FetchingState.DONE_FETCHING -> {
                    state_loading.visibility = View.GONE
                    tv_state_content.visibility = View.GONE
                }
                else -> {
                    state_loading.visibility = View.GONE
                    tv_state_content.visibility = View.GONE
                }
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

    The Sange DataSource provides **invalidate()** and **retry()** methods.
    When the data needs to be refreshed, the **invalidate()** method can be called.
    When the load fails and needs to be retried, it is called. **retry()** method.

- Custom DiffCallback

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

## END

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
