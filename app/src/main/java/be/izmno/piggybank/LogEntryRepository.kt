package be.izmno.piggybank

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object LogEntryRepository {
    private const val PREFS_NAME = "log_entries_prefs"
    private const val KEY_ENTRIES = "log_entries"
    private val gson = Gson()
    
    private var entries: MutableList<LogEntry> = mutableListOf()
    private var prefs: SharedPreferences? = null
    
    fun initialize(context: Context) {
        if (prefs == null) {
            prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            loadEntries()
        }
    }
    
    private fun loadEntries() {
        val entriesJson = prefs?.getString(KEY_ENTRIES, null)
        if (entriesJson != null) {
            val type = object : TypeToken<List<LogEntry>>() {}.type
            entries = gson.fromJson(entriesJson, type) ?: mutableListOf()
        } else {
            entries = mutableListOf()
        }
    }
    
    private fun saveEntries() {
        val entriesJson = gson.toJson(entries)
        prefs?.edit()?.putString(KEY_ENTRIES, entriesJson)?.apply()
    }
    
    fun getAllEntries(): List<LogEntry> {
        return entries.sortedByDescending { it.timestamp }
    }
    
    fun addEntry(entry: LogEntry) {
        entries.add(entry)
        saveEntries()
    }
    
    fun deleteEntry(entry: LogEntry) {
        entries.remove(entry)
        saveEntries()
    }
    
    fun getTotalAmount(): Double {
        return entries.sumOf { it.amount }
    }
}

