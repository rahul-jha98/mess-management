package com.rahul.messmanagement.ui.fragments


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Transition
import com.google.android.material.tabs.TabLayout
import com.rahul.messmanagement.MessApplication

import com.rahul.messmanagement.R
import com.rahul.messmanagement.adapters.MenuAdapter
import com.rahul.messmanagement.viewmodels.MenuViewModel
import com.rahul.messmanagement.viewmodels.RoomViewModelFactory
import kotlinx.android.synthetic.main.fragment_menu.*
import java.util.ArrayList


class MenuFragment : Fragment() {

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if(enterTransition == null) {
            mSectionsPagerAdapter = SectionsPagerAdapter(childFragmentManager)

            // Set up the ViewPager with the sections adapter.
            containerTimeTable.adapter = mSectionsPagerAdapter
            containerTimeTable.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabsTimeTable))
            tabsTimeTable.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(containerTimeTable))
        } else {
            (enterTransition as Transition).addListener(object : Transition.TransitionListener {
                override fun onTransitionEnd(transition: Transition) {
                    mSectionsPagerAdapter = SectionsPagerAdapter(childFragmentManager)

                    // Set up the ViewPager with the sections adapter.
                    containerTimeTable.adapter = mSectionsPagerAdapter
                    containerTimeTable.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabsTimeTable))
                    tabsTimeTable.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(containerTimeTable))
                }

                override fun onTransitionResume(transition: Transition) {

                }

                override fun onTransitionPause(transition: Transition) {

                }

                override fun onTransitionCancel(transition: Transition) {

                }

                override fun onTransitionStart(transition: Transition) {

                }

            })
        }
        super.onViewCreated(view, savedInstanceState)

    }

    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return TimeTableFragment.newInstance(position + 1)
        }

        override fun getCount(): Int {
            // Show 3 total pages.
            return 7
        }
    }

    class TimeTableFragment : Fragment() {
        private lateinit var roomViewModelFactory: RoomViewModelFactory
        private lateinit var timeTableAdapter: MenuAdapter
        private lateinit var recyclerView : RecyclerView

        private var pageNo = 0
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val view = inflater.inflate(R.layout.fragment_menu_for_day, container, false)
            recyclerView = view.findViewById(R.id.timetableNormalRV)
            pageNo = (arguments?.getInt(ARG_DAY_NUMBER) ?: 1) - 1
            return view
        }

        override fun onAttach(context: Context) {
            roomViewModelFactory =
                (activity?.application as MessApplication).appComponent.getViewModelFactory()
            super.onAttach(context)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            timeTableAdapter = MenuAdapter()

            recyclerView.apply {
                layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
                adapter = timeTableAdapter
            }
            val viewModel = ViewModelProviders.of(this.parentFragment!!, roomViewModelFactory).get(MenuViewModel::class.java)
            viewModel.menuList[pageNo].observe(this, Observer {
                it?.let { newList ->
                    timeTableAdapter.swapList(newList)
                }
            })
        }

        companion object {
            /**
             * The fragment argument representing the section number for this
             * fragment.
             */
            private val ARG_DAY_NUMBER = "day_number"

            /**
             * Returns a new instance of this fragment for the given section
             * number.
             */
            fun newInstance(sectionNumber: Int): TimeTableFragment {
                val fragment = TimeTableFragment()
                val args = Bundle()
                args.putInt(ARG_DAY_NUMBER, sectionNumber)
                fragment.arguments = args
                return fragment
            }
        }
    }
}
