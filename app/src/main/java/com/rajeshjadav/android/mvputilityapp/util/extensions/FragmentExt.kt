package com.rajeshjadav.android.mvputilityapp.util.extensions

import android.os.Bundle
import androidx.fragment.app.Fragment

inline fun <T : Fragment> T.withArgs(
    argsBuilder: Bundle.() -> Unit
): T = apply {
    arguments = Bundle().apply(argsBuilder)
}