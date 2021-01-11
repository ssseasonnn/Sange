package zlc.season.sangedemo.demo

import android.view.View
import zlc.season.sange.FetchingState
import zlc.season.sange.SangeItem
import zlc.season.sange.SangeViewHolder
import zlc.season.sangedemo.databinding.ViewHolderFooterBinding
import zlc.season.sangedemo.databinding.ViewHolderHeaderBinding
import zlc.season.sangedemo.databinding.ViewHolderNormalBinding
import zlc.season.sangedemo.databinding.ViewHolderStateBinding

class NormalViewHolder(containerView: View) :
    SangeViewHolder<SangeItem>(containerView) {

    override fun onBind(position: Int, t: SangeItem) {
        super.onBind(position, t)
        t as NormalItem
        val binding = ViewHolderNormalBinding.bind(itemView)
        binding.tvNormalContent.text = t.toString()
    }
}

class HeaderViewHolder(containerView: View) :
    SangeViewHolder<SangeItem>(containerView) {

    override fun onBind(position: Int, t: SangeItem) {
        super.onBind(position, t)
        t as HeaderItem
        val binding = ViewHolderHeaderBinding.bind(itemView)
        binding.tvHeaderContent.text = t.toString()
    }
}

class FooterViewHolder(containerView: View) :
    SangeViewHolder<SangeItem>(containerView) {

    override fun onBind(position: Int, t: SangeItem) {
        super.onBind(position, t)
        t as FooterItem
        val binding = ViewHolderFooterBinding.bind(itemView)
        binding.tvFooterContent.text = t.toString()
    }
}

class StateViewHolder(containerView: View) :
    SangeViewHolder<SangeItem>(containerView) {

    override fun onBind(position: Int, t: SangeItem) {
        super.onBind(position, t)
        t as StateItem
        val binding = ViewHolderStateBinding.bind(itemView)
        binding.tvStateContent.setOnClickListener {
            t.retry()
        }

        when (t.state) {
            FetchingState.FETCHING -> {
                binding.stateLoading.visibility = View.VISIBLE
                binding.tvStateContent.visibility = View.GONE
            }
            FetchingState.FETCHING_ERROR -> {
                binding.stateLoading.visibility = View.GONE
                binding.tvStateContent.visibility = View.VISIBLE
            }
            FetchingState.DONE_FETCHING -> {
                binding.stateLoading.visibility = View.GONE
                binding.tvStateContent.visibility = View.GONE
            }
            else -> {
                binding.stateLoading.visibility = View.GONE
                binding.tvStateContent.visibility = View.GONE
            }
        }
    }
}