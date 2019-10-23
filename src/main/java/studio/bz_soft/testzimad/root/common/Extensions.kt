package studio.bz_soft.testzimad.root.common

import android.content.Context
import android.widget.Toast

fun Context.showToast(message: CharSequence, duration: Int) =
    Toast.makeText(this, message, duration).show()

fun showToast(context: Context, message: CharSequence) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}