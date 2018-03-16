package com.nimbl3.having.exchange.ui.activity

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.Menu
import android.view.MenuItem
import com.nimbl3.having.exchange.R
import com.nimbl3.having.exchange.ui.dialog.DialogEnterName
import com.nimbl3.having.exchange.ui.fragment.FragmentListDemand
import com.nimbl3.having.exchange.ui.fragment.FragmentListSupply
import kotlinx.android.synthetic.main.activity_main2.*

class ActivityHome : ActivityBase() {
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        container.adapter = mSectionsPagerAdapter
        tabs.setupWithViewPager(container)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main2, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_settings) {
            val dialogEnterName = DialogEnterName.newInstance();
            dialogEnterName.show(fragmentManager, "enter name");
            return true
        }

        return super.onOptionsItemSelected(item)
    }


    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            if (position == 0) {
                return FragmentListDemand.newInstance();
            }
            return FragmentListSupply.newInstance()
        }

        override fun getCount(): Int {
            return 2
        }

        override fun getPageTitle(position: Int): CharSequence {
            if (position == 0) {
                return "My Demand"
            } else {
                return "Supply"
            }
        }
    }
}
