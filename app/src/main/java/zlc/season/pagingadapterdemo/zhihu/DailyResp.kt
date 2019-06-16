package zlc.season.pagingadapterdemo.zhihu

import zlc.season.paging.ViewType


interface DailyItem : ViewType {
    fun getTopStorys(): List<DailyResp.TopStory>

    fun getStory(): DailyResp.Story
}

class DailyHeaderItem(val top_stories: List<DailyResp.TopStory>) : DailyItem {
    override fun viewType(): Int {
        return 1
    }

    override fun getTopStorys(): List<DailyResp.TopStory> {
        return top_stories
    }

    override fun getStory(): DailyResp.Story {
        return DailyResp.Story()
    }
}

class DailyNormalItem(private val story: DailyResp.Story) : DailyItem {
    override fun getTopStorys(): List<DailyResp.TopStory> {
        return emptyList()
    }

    override fun getStory(): DailyResp.Story {
        return story
    }
}

data class DailyResp(
        val date: String = "",
        val stories: List<Story> = listOf(),
        val top_stories: List<TopStory> = listOf()
) {
    data class TopStory(
            val ga_prefix: String = "",
            val id: Int = 0,
            val image: String = "",
            val title: String = "",
            val type: Int = 0
    )

    data class Story(
            val ga_prefix: String = "",
            val id: Int = 0,
            val images: List<String> = listOf(),
            val title: String = "",
            val type: Int = 0
    )
}