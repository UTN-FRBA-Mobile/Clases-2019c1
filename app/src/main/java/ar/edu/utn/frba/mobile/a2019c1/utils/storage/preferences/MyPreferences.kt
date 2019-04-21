package ar.edu.utn.frba.mobile.a2019c1.utils.storage.preferences

import android.content.Context
import android.content.SharedPreferences

object MyPreferences {
    private const val preference_is_grid_images_list_preferred_view = "preference_is_grid_images_list_preferred_view"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("preferences", Context.MODE_PRIVATE)
    }

    private fun getPreferencesEditor(context: Context): SharedPreferences.Editor {
        return getPreferences(context).edit()
    }

    fun setGridImagesListPreferredView(context: Context, value: Boolean) {
        val editor =
            getPreferencesEditor(context)

        editor.putBoolean(preference_is_grid_images_list_preferred_view, value)

        editor.apply()
    }

    fun isGridImagesListPreferredView(context: Context): Boolean {
        val sharedPref = getPreferences(context)

        return sharedPref.getBoolean(preference_is_grid_images_list_preferred_view, true)
    }
}