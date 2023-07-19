package com.unipass.custom_auth_sdk_android_demo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.unipass.custom_auth_sdk_android_demo.databinding.FragmentFirstBinding
import com.unipass.smartAccount.*
import org.web3j.crypto.ECKeyPair
import org.web3j.crypto.Keys
import org.web3j.utils.Numeric
import java.math.BigInteger

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

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
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)

            this.testEOASigner()
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
            val address = signer.address
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

    fun testSmartAccount() {
        try{
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

            /// sign message with smart account
            val sig = smartAccount.signMessage("hello world")

            /// send transaction with smart account
            val tx = Transaction();
            // USDC Contract address
            tx.to = "0xA0b86991c6218b36c1d19D4a2e9Eb0cE3606eB48";
            tx.value = BigInteger.valueOf(0);
            // ERC20 transfer data: transfer 216.29 USDC to 0x7D5FE7F6CFF4Badd6939db27CADA6f0Bd902D3C1
            tx.data = Numeric.hexStringToByteArray("0xa9059cbb0000000000000000000000007d5fe7f6cff4badd6939db27cada6f0bd902d3c1000000000000000000000000000000000000000000000000000000000ce452d0")


            /// simulate transaction to fetch tx fee
            val simulateRet = smartAccount.simulateTransaction(tx)
            val sendTransactionOptions = SendTransactionOptions()
            if(simulateRet!!.isFeeRequired == true) {
                sendTransactionOptions.fee = simulateRet.feeOptions.get(0)
            }

            // send transaction to get tx hash
            val txHash = smartAccount.sendTransaction(tx, sendTransactionOptions)
            // get transaction receipt from chain
            val receipt = smartAccount.waitTransactionReceiptByHash(txHash, 2, ChainID.ETHEREUM_MAINNET, 60);
        }catch(err: Exception) {
            println(err)
        }
    }

}