package com.dicoding.courseschedule.ui.setting

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.notification.DailyReminder
import com.dicoding.courseschedule.util.NightMode
import java.util.Locale

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        //TODO 10 : Update theme based on value in ListPreference

        val themePref = findPreference<ListPreference>(getString(R.string.pref_key_dark))
        themePref?.setOnPreferenceChangeListener { _, newValue ->
            val darkMode = NightMode.valueOf(newValue.toString().uppercase(Locale.US))
            updateTheme(darkMode.value)
            Toast.makeText(requireContext(), getString(R.string.theme_changed), Toast.LENGTH_SHORT).show()
            true
        }

        //TODO 11 : Schedule and cancel notification in DailyReminder based on SwitchPreference

        val notifPref = findPreference<SwitchPreference>(getString(R.string.pref_key_notify))
        val dailyReminder = DailyReminder()
        notifPref?.setOnPreferenceChangeListener { _, newValue ->
            val value = newValue as Boolean

            if (value) {
                dailyReminder.setDailyReminder(requireContext())
            } else {
                dailyReminder.cancelAlarm(requireContext())
            }
            true
        }
    }

    private fun updateTheme(nightMode: Int): Boolean {
        AppCompatDelegate.setDefaultNightMode(nightMode)
        requireActivity().recreate()
        return true
    }
}