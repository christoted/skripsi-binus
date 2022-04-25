package com.example.project_skripsi.utils.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.View

import androidx.viewpager.widget.ViewPager

class WrapContentViewPager : ViewPager {
    constructor(context: Context?) : super(context!!)
    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightMeasureSpecNew = heightMeasureSpec
        val mode = MeasureSpec.getMode(heightMeasureSpecNew)
        // Unspecified means that the ViewPager is in a ScrollView WRAP_CONTENT.
        // At Most means that the ViewPager is not in a ScrollView WRAP_CONTENT.
        if (mode == MeasureSpec.UNSPECIFIED || mode == MeasureSpec.AT_MOST) {
            // super has to be called in the beginning so the child views can be initialized.
            super.onMeasure(widthMeasureSpec, heightMeasureSpecNew)
            var height = 0
            for (i in 0 until childCount) {
                val child: View = getChildAt(i)
                child.measure(
                    widthMeasureSpec,
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
                )
                val childMeasuredHeight: Int = child.getMeasuredHeight()
                if (childMeasuredHeight > height) {
                    height = childMeasuredHeight
                }
            }
            heightMeasureSpecNew = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        }
        // super has to be called again so the new specs are treated as exact measurements
        super.onMeasure(widthMeasureSpec, heightMeasureSpecNew)
    }

    fun hasNext() = (currentItem + 1) < (adapter?.count ?: 0)

    fun hasPrev() = currentItem > 0

    fun nextPage() {
        if (!hasNext()) return
        setCurrentItem(currentItem+1, true)
    }

    fun prevPage() {
        if (!hasPrev()) return
        setCurrentItem(currentItem-1, true)
    }


}