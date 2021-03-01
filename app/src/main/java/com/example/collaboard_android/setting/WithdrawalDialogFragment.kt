package com.example.collaboard_android.setting

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.example.collaboard_android.databinding.DialogWithdrawalBinding
import com.example.collaboard_android.login.ui.SignInOutActivity
import com.example.collaboard_android.util.SharedPreferenceController
import com.google.firebase.database.*


class WithdrawalDialogFragment : DialogFragment() {

    private var _binding: DialogWithdrawalBinding? = null
    private val binding get() = _binding!!

    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference: DatabaseReference = firebaseDatabase.reference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        _binding = DialogWithdrawalBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initOkButton()

        initCancelButton()
    }

    private fun initOkButton() {
        binding.buttonOk.setOnClickListener {
            // DB에서 해당 user 정보 지우기
            removeUserInfo()
            SharedPreferenceController.clearAll(requireContext())
            goToLoginActivity()
            this.dismiss()
        }
    }

    private fun removeUserInfo() {
        databaseReference.child("users").child(SharedPreferenceController.getUid(context!!)!!)
            .addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (child in snapshot.children) {
                    child.ref.removeValue()
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun goToLoginActivity() {
        val intent = Intent(requireContext(), SignInOutActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun initCancelButton() {
        binding.buttonCancel.setOnClickListener {
            this.dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        setDialogSize()
    }

    private fun setDialogSize() {
        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        val windowManager = activity?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val deviceWidth = size.x
        params?.width = (deviceWidth * 0.7).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }
}