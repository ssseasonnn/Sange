![](https://raw.githubusercontent.com/ssseasonnn/Sange/master/sange_icon.png)

[![](https://jitpack.io/v/ssseasonnn/Sange.svg)](https://jitpack.io/#ssseasonnn/Sange)

# Sange

*Read this in other languages: [中文](README.zh.md), [English](README.md)*

A lightweight library that quickly implements RecyclerView paging loading.


### Prepare

1. Add the JitPack repository to your build file
```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

2. Add the dependency

```gradle
dependencies {
    // Replace xyz with a specific version number, for example 1.0.0
	implementation 'com.github.ssseasonnn:Sange:xyz'
}
```

### Usage

1. The core function of Sange is DataSource, which makes it easy to initialize and page load data in a few simple steps.

Before that, we have to define our data types first, for example:

```kotlin
class NormalItem(val number: Int)
```

Next inherit the DataSource and implement the **loadInitial** and **loadAfter** methods, for example:

```kotlin
class NormalDataSource : DataSource<NormalItem>() {

    override fun loadInitial(loadCallback: LoadCallback<NormalItem>) {

        //loadInitial will be called in the io thread.
        Thread.sleep(2000)

        // loading
        val items = mutableListOf<NormalItem>()
        for (i in 0 until 10) {
            items.add(NormalItem(i))
        }

        //Update RecyclerView by passing the loaded data to LoadCallback
        loadCallback.setResult(items)
    }

    override fun loadAfter(loadCallback: LoadCallback<NormalItem>) {
        //loadAfter will be called in the io thread.
        Thread.sleep(2000)

        val items = mutableListOf<NormalItem>()
        for (i in page * 10 until (page + 1) * 10) {
            items.add(NormalItem(i))
        }

        loadCallback.setResult(items)
    }
}

```

Both the loadInitial and loadAfter methods will be called in the io thread,
so there is no need to worry about any time-consuming operations in both methods.

After the data is loaded, just call LoadCallback's setResult(list) method,
and Sange will do all the other work for you, including thread switching,
notification interface update, etc.
What you need to do is just focus on the data load.

2. The next step is to create your own Adapter.
By inheriting the **SangeAdapter** provided by Sange,
you can easily combine the DataSources.

For example

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

3. Finally, associate the RecyclerView with the Adapter:

```kotlin
recycler_view.layoutManager = LinearLayoutManager(this)
recycler_view.adapter = NormalAdapter(NormalDataSource())
```

That's it, you don't have to care about the logic of paging.

## Show loading state.

```kotlin
class NormalDataSource : DataSource<NormalItem>() {

    override fun loadInitial(loadCallback: LoadCallback<NormalItem>) {
        //...
    }

    override fun loadAfter(loadCallback: LoadCallback<NormalItem>) {
        //...
    }

    override fun onStateChanged(newState: Int) {
        //set extra state item
        setState(NormalStateItem(state = newState, retry = ::retry))
    }
}
```

With the setState() method, we added an additional Item entry for
the state to represent the state, so we need to modify our Adapter slightly
so that the loaded state is correctly displayed.

```kotlin
class NormalAdapter(dataSource: DataSource<NormalItem>) :
        SangeAdapter<NormalItem, NormalViewHolder>(dataSource) {

    //...

    override fun onBindViewHolder(holder: NormalViewHolder, position: Int) {
        val item = getItem(position)

        //Determine the current item type, if it is a state type, render state
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

Here we are simply an example of rendering a single type of data,
so we do not use ViewType to implement multiple types of item display,
just simply determine the type of data and render different types of UI views.

Final result:

![](https://github.com/ssseasonnn/Sange/raw/master/normal.gif)


## Multi-Item Type.


If we need a more complex display, including a variety of view types,
or need to display the head and tail, it does not matter, Sange can help you easily.

1. First define the various data types we need:

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

//Don't forget our status type
class StateItem(val state: Int, val retry: () -> Unit) : SangeItem {
    override fun viewType() = STATE
}
```


Implements **SangeItem** interface for all types,
and implements the viewType() method of the interface,
returning the corresponding type value in the method.

2. Next, let's adjust our DataSource, this time we use the more powerful **MultiDataSource** :

```kotlin
class DemoDataSource : MultiDataSource<SangeItem>() {

    override fun loadInitial(loadCallback: LoadCallback<SangeItem>) {
        //loading Header data
        val headers = mutableListOf<SangeItem>()
        for (i in 0 until 2) {
            headers.add(HeaderItem(i))
        }
        //add header data into DataSource
        addHeaders(headers)

        //loading Footer data
        val footers = mutableListOf<SangeItem>()
        for (i in 0 until 2) {
            footers.add(FooterItem(i))
        }

        //add footer data into DataSource
        addFooters(footers)


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


3. Finally adjust the Adapter, here also use the more powerful **SangeMultiAdapter**:

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

At this point, the rest will be handed over to Sange!

Result:

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
