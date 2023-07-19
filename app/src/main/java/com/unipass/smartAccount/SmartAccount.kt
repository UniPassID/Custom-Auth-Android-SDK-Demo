package com.unipass.smartAccount

import com.unipass.smartAccount.SmartAccountOptions
import com.unipass.smartAccount.SmartAccountInitOptions
import com.unipass.smartAccount.ChainID
import kotlin.jvm.JvmOverloads
import com.unipass.smartAccount.SimulateTransactionOptions
import com.unipass.smartAccount.SimulateResult
import com.unipass.smartAccount.SendTransactionOptions
import org.web3j.protocol.core.methods.response.TransactionReceipt

class SmartAccount(options: SmartAccountOptions?) {
    fun init(options: SmartAccountInitOptions?) {}//        throw new Exception("not implemented");

    /*********************** Account Status Functions  */
    val address: String?
        get() = null

    //        throw new Exception("not implemented");
    val isDeployed: Boolean
        get() = false
    val chainId: ChainID
        get() = ChainID.ETHEREUM_MAINNET
    val nonce: Int
        get() = 1

    fun switchChain(chainID: ChainID?) {}

    /*********************** Message Sign Functions  */
    fun signMessage(message: String?): String? {
        return null
    }

    fun signMessage(messageBytes: ByteArray?): String? {
        return null
    }

    // TODO:
    fun signTypedData(): String? {
        return null
    }

    /*********************** Transaction Functions  */
    @JvmOverloads
    fun simulateTransaction(
        tx: Transaction?,
        options: SimulateTransactionOptions? = null
    ): SimulateResult? {
        return null
    }

    @JvmOverloads
    fun simulateTransactionBatch(
        txs: Array<Transaction?>?,
        options: SimulateTransactionOptions? = null
    ): SimulateResult? {
        return null
    }

    @JvmOverloads
    fun sendTransaction(tx: Transaction?, options: SendTransactionOptions? = null): String? {
        return null
    }

    @JvmOverloads
    fun sendTransactionBatch(
        txs: Array<Transaction?>?,
        options: SendTransactionOptions? = null
    ): String? {
        return null
    }

    @JvmOverloads
    fun signTransaction(
        txs: Array<Transaction?>?,
        options: SendTransactionOptions? = null
    ): Transaction? {
        return null
    }

    // TODO:
    fun sendSignedTransaction(): String? {
        return null
    }

    fun waitTransactionReceiptByHash(
        transactionHash: String?,
        confirmations: Int,
        chainId: ChainID?,
        timeOut: Int
    ): TransactionReceipt? {
        return null
    }
}