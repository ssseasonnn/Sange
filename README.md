![](https://raw.githubusercontent.com/ssseasonnn/Sange/master/sange_icon.png)

# Sange

[![](https://jitpack.io/v/ssseasonnn/Sange.svg)](https://jitpack.io/#ssseasonnn/Sange)

### How to use

Step 1. Add the JitPack repository to your build file
```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Step 2. Add the dependency

```gradle
dependencies {
    // replace xyz to latest version number
	implementation 'com.github.ssseasonnn:Sange:xyz'
}
```

Step 3. Usage

First, create a DataSource:

```kotlin
class DemoDataSource : MultiDataSource<SangeItem>() {

    override fun loadInitial(loadCallback: LoadCallback<SangeItem>) {

        val items = mutableListOf<SangeItem>()
        for (i in 0 until 10) {
            items.add(NormalItem(i))
        }

        loadCallback.setResult(items)
    }

    override fun loadAfter(loadCallback: LoadCallback<SangeItem>) {

        val items = mutableListOf<SangeItem>()
        for (i in page * 10 until (page + 1) * 10) {
            items.add(NormalItem(i))
        }

        loadCallback.setResult(items)
    }
}

```

Second, create an Adapter, like that:

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

    private fun inflate(parent: ViewGroup, res: Int): View {
        return LayoutInflater.from(parent.context).inflate(res, parent, false)
    }
}
```

Then, set the data source into adapter:

```kotlin
recycler_view.layoutManager = LinearLayoutManager(this)
recycler_view.adapter = DemoAdapter(demoViewModel.dataSource)
```

Last, enjoy! Sange will  automatic paging.

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
