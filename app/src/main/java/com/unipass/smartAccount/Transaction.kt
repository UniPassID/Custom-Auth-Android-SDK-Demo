package com.unipass.smartAccount

import java.math.BigInteger

class Transaction {
    var to: String? = null
    var value: BigInteger? = null
    lateinit var data: ByteArray
}