package com.nimbl3.having.exchange.ui.fragment

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.nimbl3.having.exchange.R
import com.nimbl3.having.exchange.ui.activity.ActivityChat
import com.nimbl3.having.exchange.ui.intents.DemandListIntents
import com.nimbl3.having.exchange.ui.model.Demand
import com.nimbl3.having.exchange.ui.mvibase.MviView
import com.nimbl3.having.exchange.ui.viewmodel.ListDemandViewModel
import com.nimbl3.having.exchange.ui.viewstate.DemandListViewState
import com.nimbl3.having.exchange.ui.viewstate.DemandListViewStateWithData
import com.nimbl3.having.exchange.ui.viewstate.DemandListviewStateEmpty
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

class FragmentListDemand : FragmentBase(), MviView {
    private var mDemandCard: CardView? = null
    private var mTvEmpty: View? = null
    private var mTvTitle: TextView? = null
    private var mTvDetail: TextView? = null
    private var mViewModel: ListDemandViewModel? = null
    private var mDisposables = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_list_demand, container, false)
        mDemandCard = view.findViewById(R.id.demand_card)
        mTvEmpty = view.findViewById(R.id.empty_tv)
        mTvTitle = view.findViewById(R.id.title)
        mTvDetail = view.findViewById(R.id.detail)
        mDemandCard!!.setOnClickListener { v ->
            val intent = Intent(activity, ActivityChat::class.java)
            startActivity(intent)
        }

        mViewModel = ViewModelProviders.of(this).get(ListDemandViewModel::class.java)
        mDisposables = CompositeDisposable()
        bind()
        return view
    }

    private fun bind() {
        // Subscribe to the ViewModel and call render for every emitted state
        mDisposables.add(mViewModel!!.states().subscribe({ this.render(it) }))
        // Pass the UI's intents to the ViewModel
        mViewModel!!.processIntents(intents())
    }

    private fun intents(): Observable<DemandListIntents> {
        return Observable.just(object : DemandListIntents {

        })
    }

    protected fun render(demandListViewState: DemandListViewState) {
        if (demandListViewState is DemandListviewStateEmpty) {
            showEmptyState()
        } else if (demandListViewState is DemandListViewStateWithData) {
            showDemands(demandListViewState.mDemand)
        }
    }

    protected fun setDemandsData(demand: Demand) {
        mTvTitle!!.text = demand.title
        mTvDetail!!.text = demand.description
    }

    protected fun showEmptyState() {
        mDemandCard!!.visibility = View.GONE
        mTvEmpty!!.visibility = View.VISIBLE
    }

    protected fun showDemands(demand: Demand) {
        mDemandCard!!.visibility = View.VISIBLE
        mTvEmpty!!.visibility = View.GONE
        setDemandsData(demand)
    }

    companion object {

        fun newInstance(): FragmentListDemand {
            return FragmentListDemand()
        }
    }
}
