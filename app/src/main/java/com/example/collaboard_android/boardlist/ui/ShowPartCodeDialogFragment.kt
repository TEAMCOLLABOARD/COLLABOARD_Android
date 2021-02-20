package com.example.collaboard_android.boardlist.ui

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.example.collaboard_android.board.ui.BoardActivity
import com.example.collaboard_android.boardlist.ui.CreateBoardActivity.Companion.nContext
import com.example.collaboard_android.databinding.DialogShowParticipationCodeBinding

class ShowPartCodeDialogFragment : DialogFragment() {

    private var _binding: DialogShowParticipationCodeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        _binding = DialogShowParticipationCodeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCloseButton()
    }

    private fun initCloseButton() {
        binding.buttonClose.setOnClickListener {
            goToBoardActivity()
            finishCreateBoardActivity()
        }
    }

    private fun goToBoardActivity() {
        val intent = Intent(context, BoardActivity::class.java)
        startActivity(intent)
    }

    private fun finishCreateBoardActivity() {
        val aContext: CreateBoardActivity = nContext
        aContext.finishActivity()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}