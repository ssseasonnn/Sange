package zlc.season.pagingadapterdemo.github

import zlc.season.paging.Differ

data class GithubRepositoryResp(
        val incomplete_results: Boolean = false,
        val items: List<GithubRepository> = listOf(),
        val total_count: Int = 0
) {
    data class GithubRepository(
            val id: Int = 0,
            val description: String = "",
            val forks_count: Int = 0,
            val full_name: String = "",
            val name: String = "",
            val owner: Owner = Owner(),
            val stargazers_count: Int = 0
    ) : Differ {
        override fun areItemsTheSame(other: Differ): Boolean {
            if (other !is GithubRepository) {
                return false
            }
            return this.id == other.id
        }

        override fun areContentsTheSame(other: Differ): Boolean {
            return true
        }
    }


    data class Owner(
            val avatar_url: String = ""
    )
}