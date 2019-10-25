package studio.bz_soft.testzimad.root.common

import android.content.Context
import android.os.Handler
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

fun Context.showToast(message: CharSequence, duration: Int) =
    Toast.makeText(this, message, duration).show()

fun showToast(context: Context, message: CharSequence) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun RecyclerView.scrollToPosition(recyclerView: RecyclerView, position: Int) =
    Handler().postDelayed({ recyclerView.scrollToPosition(position) }, 200)