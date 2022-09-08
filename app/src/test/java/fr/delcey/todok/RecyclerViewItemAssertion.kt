package fr.delcey.todok

import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.IntRange
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matcher

class RecyclerViewItemAssertion(
    @IntRange(from = 0) private val position: Int,
    @IdRes private val viewId: Int,
    private val matcher: Matcher<View>,
) : ViewAssertion {
    override fun check(view: View, noViewFoundException: NoMatchingViewException?) {
        noViewFoundException?.let { throw it }

        check(view is RecyclerView) { "The asserted view is not RecyclerView" }

        val viewHolder = view.findViewHolderForLayoutPosition(position)
            ?: throw IllegalStateException("No ViewHolder found for layout position : $position")

        val childView = viewHolder.itemView.findViewById<View>(viewId)
            ?: throw IllegalStateException("No view found with id : " + view.resources.getResourceEntryName(viewId))

        ViewMatchers.assertThat(childView, matcher)
    }
}