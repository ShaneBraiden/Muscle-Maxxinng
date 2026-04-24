package com.musclemax.app.data.remote

// Supabase publishable (anon) key — designed to be shipped client-side.
// Row-Level Security policies protect data; the key alone cannot bypass them.
// If you need to rotate, replace these values or move them to BuildConfig
// driven from `local.properties`.
internal object SupabaseConfig {
    const val URL = "https://vgjlyuzxidfrgrwguwbi.supabase.co"
    const val ANON_KEY = "sb_publishable_fo3f2WRcJW-FaC-h7aGkKg_b4InCC8v"
}
