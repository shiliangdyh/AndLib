package com.mirstone.module.xrecyclerview

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import com.jcodecraeer.xrecyclerview.XRecyclerView
import com.mirstone.R
import com.mirstone.baselib.base.BaseSwipeBackActivity
import kotlinx.android.synthetic.main.activity_xrecyclerview.*

/**
 * @package: com.mirstone.module.xrecyclerview
 * @fileName: XRecyclerViewActivity
 * @data: 2018/9/3 15:41
 * @author: ShiLiang
 * @describe:
 */
class XRecyclerViewActivity : BaseSwipeBackActivity() {
    val handler = Handler(Handler.Callback {
        recyclerview.refreshComplete()
        false
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_xrecyclerview)

        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.defaultRefreshHeaderView.setRefreshTimeVisible(true)
        recyclerview.setPullRefreshEnabled(true)
        recyclerview.setLoadingMoreEnabled(true)
        recyclerview.adapter = MyAdapter()
        recyclerview.setLoadingListener(object : XRecyclerView.LoadingListener {
            override fun onLoadMore() {

            }

            override fun onRefresh() {
                handler.sendEmptyMessageDelayed(10, 2000)
            }
        })
    }

    override fun getStateBarColor(): Int {
        return Color.parseColor("#88000000")
    }
}
