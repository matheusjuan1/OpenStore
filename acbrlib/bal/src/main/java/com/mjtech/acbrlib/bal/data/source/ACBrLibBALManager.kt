package com.mjtech.acbrlib.bal.data.source

import br.com.acbr.lib.bal.ACBrLibBAL

class ACBrLibBALManager private constructor() {

    private val acbrLibInstance: ACBrLibBAL

    init {
        acbrLibInstance = ACBrLibBAL("", "")
    }

    fun getLib(): ACBrLibBAL {
        return acbrLibInstance
    }

    companion object {
        @Volatile
        private var INSTANCE: ACBrLibBALManager? = null

        fun getInstance(): ACBrLibBALManager =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: ACBrLibBALManager().also { INSTANCE = it }
            }
    }
}