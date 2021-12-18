package com.example.childtest.menu

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.DialogFragment
import com.example.childtest.appConfig.Tools
import com.example.childtest.databinding.DialogSettingLogoBinding

class SettingLogoDialog : DialogFragment() {
    private lateinit var binding: DialogSettingLogoBinding
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : Dialog(requireActivity(), theme) {

        }
    }

    var listener: OnDialogListener? = null

    interface OnDialogListener {
        fun onClick(bOpen: Boolean)
    }

    fun setOnDialogListener(dialogListener: OnDialogListener) {
        this.listener = dialogListener
    }

    override fun onCreateView(
        @NonNull inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View {
        binding = DialogSettingLogoBinding.inflate(inflater, container, false)

        //random num setting
        val randomNum1: String = "" + Tools.randomNum(2, 9)
        val randomNum2: String = "" + Tools.randomNum(2, 9)
        binding.SUMNumber1.text = randomNum1
        binding.SUMNumber2.text = randomNum2

        //严重密码如何
        binding.btnOk.setOnClickListener {

            val dengyuS = binding.SUM.text as String

            val SUM_number1 = binding.SUMNumber1.text as String
            val SUM_number2 = binding.SUMNumber2.text as String
            if (dengyuS.toInt() == SUM_number1.toInt() * SUM_number2.toInt()) {
                listener?.onClick(true)
            } else {
                listener?.onClick(false)
            }
            dialog!!.dismiss()
        }


        binding.number1.setOnClickListener {
            binding.SUM.text = addTextFun("1")
        }

        binding.number2.setOnClickListener {
            binding.SUM.text = addTextFun("2")
        }

        binding.number3.setOnClickListener {
            binding.SUM.text = addTextFun("3")
        }

        binding.number4.setOnClickListener {
            binding.SUM.text = addTextFun("4")
        }

        binding.number5.setOnClickListener {
            binding.SUM.text = addTextFun("5")
        }

        binding.number6.setOnClickListener {
            binding.SUM.text = addTextFun("6")
        }

        binding.number7.setOnClickListener {
            binding.SUM.text = addTextFun("7")
        }

        binding.number8.setOnClickListener {
            binding.SUM.text = addTextFun("8")
        }

        binding.number9.setOnClickListener {
            binding.SUM.text = addTextFun("9")
        }

        binding.number0.setOnClickListener {
            binding.SUM.text = addTextFun("0")
        }

        return binding.root
    }

    private fun addTextFun(addText: String): String {
        var sumText = ""
        sumText = binding.SUM.text as String
        if (sumText.toInt() < 10) {
            if (sumText == "0") sumText = ""
            sumText += addText
        }
        return sumText
    }
}