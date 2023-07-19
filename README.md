## UniPass Custom Auth SDK for Android

This repo defines the Android version of UniPass Custom Auth SDK's relevant interfaces using Kotlin language. 

>This version is not the final effective version, it is only intended as a reference for interface design.

## How to use 

1. Provide an object that implements the following interface.


```Kotlin

package com.unipass.smartAccount

/**
 * Partial Signer from ethers.js https://github.com/ethers-io/ethers.js/blob/main/src.ts/providers/signer.ts
 */
interface Signer {
    /**
     * get signer ethereum address
     * @return ethereum address
     */
    val address: String?

    /**
     * sign string message using personal sign
     * @param message String message to be signed, it will be converted to UTF-8 bytes before signing
     * @return signature with hex format prefixed with '0x'
     */
    fun signMessage(message: String?): String?

    /**
     * sign bytes message using personal sign
     * @param message Bytes message to be signed
     * @return signature with hex format prefixed with '0x'
     */
    fun signMessage(message: ByteArray?): String?
}

```

2. Generate a SmartAccount instance based on Signer, and then use the SmartAccount instance to complete operations such as signing and sending transactions.

```Kotlin
    val ecKeyPair: ECKeyPair = Keys.createEcKeyPair()
    val privateKeyInDec: BigInteger = ecKeyPair.getPrivateKey()
    val sPrivatekeyInHex = privateKeyInDec.toString(16)
    val signer = EOASigner(sPrivatekeyInHex)

    /// init smart account instance
    val smartAccountOption = SmartAccountOptions()
    smartAccountOption.appId = "APPID";
    smartAccountOption.masterKeySigner = signer;
    smartAccountOption.unipassServerUrl = "";
    val smartAccount = SmartAccount(smartAccountOption)

    /// get smart account address
    val address = smartAccount.address;
```


3. Sign message with Smart Account
```Kotlin
    val sig = smartAccount.signMessage("hello world")
```

4. Send Transaction with Smart Account

```Kotlin
    /// simulate transaction to fetch tx fee
    val simulateRet = smartAccount.simulateTransaction(tx)
    val sendTransactionOptions = SendTransactionOptions()
    if(simulateRet!!.isFeeRequired == true) {
        sendTransactionOptions.fee = simulateRet.feeOptions.get(0)
    }
    
    /// send transaction to get tx hash
    val txHash = smartAccount.sendTransaction(tx, sendTransactionOptions)
    // get transaction receipt from chain
    val receipt = smartAccount.waitTransactionReceiptByHash(txHash, 2, ChainID.ETHEREUM_MAINNET, 60);
```

5. Switch chain
```Kotlin
    smartAccount.switchChain(ChainID.BNBCHAIN_MAINNET)
```