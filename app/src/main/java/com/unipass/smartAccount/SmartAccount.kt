package com.unipass.smartAccount

import com.unipass.smartAccount.SmartAccountOptions
import com.unipass.smartAccount.SmartAccountInitOptions
import com.unipass.smartAccount.ChainID
import kotlin.jvm.JvmOverloads
import com.unipass.smartAccount.SimulateTransactionOptions
import com.unipass.smartAccount.SimulateResult
import com.unipass.smartAccount.SendTransactionOptions
import kotlinx.coroutines.coroutineScope
import org.web3j.protocol.core.methods.response.TransactionReceipt
import org.web3j.utils.Numeric
import uniffi.shared.SmartAccount
import uniffi.shared.SmartAccountBuilder
import java.math.BigInteger
import kotlin.coroutines.suspendCoroutine

class SmartAccount(options: SmartAccountOptions) {
    var builder: SmartAccountBuilder?;
    var inner: SmartAccount? = null;
    var masterKeySigner: WrapSigner;

    init {
        masterKeySigner = WrapSigner(options.masterKeySigner);
        builder = SmartAccountBuilder();
        builder!!.withAppId(options.appId);
        builder!!.withMasterKeySigner(masterKeySigner);
        builder!!.withUnipassServerUrl(options.unipassServerUrl);
        options.chainOptions.iterator().forEach { chainOptions ->
            builder!!.addChainOption(
                chainOptions.chainId.iD.toULong(),
                chainOptions.rpcUrl,
                chainOptions.relayerUrl
            )
        };
    }

    suspend fun init(options: SmartAccountInitOptions) {
        builder!!.withActiveChain(options.chainId.iD.toULong());
        return coroutineScope {
            inner = builder!!.build();
            builder!!.destroy();
            builder = null;
        }
    }

    /*********************** Account Status Functions  */
    val address: String?
        get() = Numeric.toHexString(inner?.address()?.toUByteArray()?.toByteArray())

    //        throw new Exception("not implemented");
    val isDeployed: Boolean
        get() = false
    val chainId: ChainID
        get() = ChainID.ETHEREUM_MAINNET
    val nonce: Int
        get() = 1

    fun switchChain(chainID: ChainID?) {}

    /*********************** Message Sign Functions  */
    suspend fun signMessage(message: String): String? {
        return Numeric.toHexString(
            inner?.signMessage(message.toByteArray().toUByteArray().toList())?.toUByteArray()
                ?.toByteArray()
        );
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