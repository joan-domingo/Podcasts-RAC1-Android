package cat.xojan.random1.feature.home

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import cat.xojan.random1.R

class HomePagerAdapter(fm: FragmentManager,
                       private val context: Context) : FragmentPagerAdapter(fm) {

    private val fragmentList = mutableListOf<Fragment>()

    fun addFragment(fragment: Fragment) {
        fragmentList.add(fragment)
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence = when (position) {
        0 -> context.getString(R.string.podcasts_programs)
        1 -> context.getString(R.string.podcasts_downloaded)
        else -> throw IllegalArgumentException("Invalid position: " + position)
    }
}