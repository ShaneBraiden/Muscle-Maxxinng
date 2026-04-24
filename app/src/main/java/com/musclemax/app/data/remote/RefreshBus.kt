package com.musclemax.app.data.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

/**
 * Minimal pub/sub so repository writes can nudge observers to re-fetch.
 * Not a true realtime pipe — if you need cross-device live updates, swap
 * in `supabase.realtime.channel(...).postgresChangeFlow<...>()`.
 */
internal class RefreshBus {
    private val signal = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    fun <T> observe(fetch: suspend () -> T): Flow<T> =
        signal
            .onStart { emit(Unit) }
            .map { fetch() }

    fun trigger() {
        signal.tryEmit(Unit)
    }
}
