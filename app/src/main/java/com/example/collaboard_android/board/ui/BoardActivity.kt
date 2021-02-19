package com.example.collaboard_android.board.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.collaboard_android.board.adapter.ViewPagerAdapter
import com.example.collaboard_android.databinding.ActivityBoardBinding

class BoardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBoardBinding

    private lateinit var viewPagerAdapter: ViewPagerAdapter

    companion object {
        lateinit var mContext: BoardActivity
        private set
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoardBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        mContext = this

        initViewPager()

        setViewPagerPaging()
    }

    private fun initViewPager() {
        viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        viewPagerAdapter.fragments = listOf(
            TodoFragment(),
            InProgressFragment(),
            DoneFragment()
        )
        binding.viewpagerBoard.adapter = viewPagerAdapter
        binding.viewpagerBoard.clipToPadding = false
    }

    private fun setViewPagerPaging() {
        val dpValue = 14
        val d: Float = resources.displayMetrics.density
        val margin = (dpValue * d).toInt()
        
        binding.viewpagerBoard.setPadding(margin, 0, margin, 0)
        binding.viewpagerBoard.pageMargin = (margin / 1.7).toInt()
    }

    fun getCurrentFrag() : Int {
        return when (binding.viewpagerBoard.currentItem) {
            0 -> 0
            1 -> 1
            else -> 2
        }
    }

    fun movePage(index: Int) {
        val handler = Handler()
        handler.postDelayed({
            binding.viewpagerBoard.post {
                binding.viewpagerBoard.setCurrentItem(index, true)
            }
        }, 500)
    }
}