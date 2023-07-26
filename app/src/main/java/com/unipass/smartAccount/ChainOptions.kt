package com.unipass.smartAccount

import com.unipass.smartAccount.ChainID

class ChainOptions(
    var chainId: ChainID,
    var rpcUrl: String,
    var relayerUrl: String?
) {}