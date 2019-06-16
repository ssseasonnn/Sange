package zlc.season.pagingadapterdemo

import zlc.season.paging.Differ

data class GithubRepositoryResp(
        val incomplete_results: Boolean = false,
        val items: List<GithubRepository> = listOf(),
        val total_count: Int = 0
) {
    data class GithubRepository(
            var name: String = "",
            var id: Int = 0
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
}