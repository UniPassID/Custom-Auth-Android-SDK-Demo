package com.unipass.custom_auth_sdk_android_demo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.unipass.custom_auth_sdk_android_demo.databinding.FragmentFirstBinding
import com.unipass.smartAccount.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import uniffi.shared.Transaction
import org.web3j.crypto.ECKeyPair
import org.web3j.crypto.Keys
import org.web3j.utils.Numeric
import uniffi.shared.SendingTransactionOptions
import java.math.BigInteger
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val coroutineScope: CoroutineScope = MainScope()


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            coroutineScope.launch(Dispatchers.Main) {
                testSmartAccount()
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    fun testEOASigner() {
        try {
            val ecKeyPair: ECKeyPair = Keys.createEcKeyPair()
            val privateKeyInDec: BigInteger = ecKeyPair.getPrivateKey()
            val sPrivatekeyInHex = privateKeyInDec.toString(16)
            var content = ""
            content += """
            privateKey: $sPrivatekeyInHex
            
            """.trimIndent()
            val signer = EOASigner(sPrivatekeyInHex)
            val address = signer.address()
            content += """
            address: $address
            
            
            """.trimIndent()
            val message = "HAHA"
            val sig = signer.signMessage(message)
            content += "message: $message\n"
            content += """
            sig: $sig
            
            """.trimIndent()
            binding.textviewFirst.text = content
        } catch (err: Exception) {
            println("TEST ERROR")
            println(err)
        }
    }

    suspend fun testSmartAccount() {
        try {
            val ecKeyPair: ECKeyPair = Keys.createEcKeyPair()
            val privateKeyInDec: BigInteger = ecKeyPair.getPrivateKey()
//            val sPrivatekeyInHex = privateKeyInDec.toString(16)
            val sPrivatekeyInHex =
                "0xa73c09635a405d858f0aa373fba9f2980afa76639eb4347a273cf92110d2350a"
            val signer = EOASigner(sPrivatekeyInHex)

            /// init smart account instance
            val smartAccountOption = SmartAccountOptions(
                signer,
                "9e145ea3e5525ee793f39027646c4511",
                "https://t.wallet.unipass.vip/wallet-coustom-auth",
                arrayOf(
                    ChainOptions(
                        ChainID.POLYGON_MUMBAI,
                        "https://node.wallet.unipass.id/polygon-mumbai",
                        "https://t.wallet.unipass.vip/relayer-v2-polygon"
                    )
                )
            )
            val smartAccount = SmartAccount(smartAccountOption)
            smartAccount.init(SmartAccountInitOptions(ChainID.POLYGON_MUMBAI))

            /// get smart account address
            val address = smartAccount.address;
            println(address)

            /// send transaction with smart account
            // USDC Contract address
            // ERC20 transfer data: transfer 216.29 USDC to 0x7D5FE7F6CFF4Badd6939db27CADA6f0Bd902D3C1
            val tx = Transaction(
                "0x87F0E95E11a49f56b329A1c143Fb22430C07332a",
                "0xa9059cbb0000000000000000000000007d5fe7f6cff4badd6939db27cada6f0bd902d3c1000000000000000000000000000000000000000000000000000000000ce452d0",
                "0x0"
            );

            /// simulate transaction to fetch tx fee
            val simulateRet = smartAccount.simulateTransaction(tx)
            println("SimulateRet: $simulateRet")
            val sendTransactionOptions = SendingTransactionOptions(null)
            if (simulateRet!!.isFeeRequired) {
                sendTransactionOptions.fee =
                    simulateRet?.feeOptions?.find { it.token.lowercase() == "0x87F0E95E11a49f56b329A1c143Fb22430C07332a".lowercase() }
            }
//
            // send transaction to get tx hash
            println(sendTransactionOptions)
            val txHash = smartAccount.sendTransaction(tx, sendTransactionOptions)
            println(txHash);

//            // get transaction receipt from chain
            val receipt =
                smartAccount.waitTransactionReceiptByHash(txHash!!, 2, ChainID.ETHEREUM_MAINNET, 60);
            var content = ""
            content += """
            address: $address
            
            
            """.trimIndent()
            val message = "HAHA"
            var sig: String? = null;
            sig = smartAccount.signMessage(message);
            println("Sign Message Success")
            content += "message: $message\n"
            content += """
            sig: $sig
            simulate result: $simulateRet
            receipt: $receipt
            """.trimIndent()
            binding.textviewFirst.text = content
        } catch (err: Exception) {
            println(err)
        }
    }

}