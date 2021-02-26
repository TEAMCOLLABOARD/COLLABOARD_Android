package com.example.collaboard_android.boardlist.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.collaboard_android.board.ui.BoardActivity
import com.example.collaboard_android.boardlist.ui.CreateBoardActivity.Companion.dialog_board_name
import com.example.collaboard_android.boardlist.ui.CreateBoardActivity.Companion.dialog_repo_name
import com.example.collaboard_android.boardlist.ui.CreateBoardActivity.Companion.nContext
import com.example.collaboard_android.databinding.DialogShowParticipationCodeBinding
import com.example.collaboard_android.model.BoardInfoModel
import com.example.collaboard_android.util.SharedPreferenceController
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class ShowPartCodeDialogFragment : DialogFragment() {

    private var _binding: DialogShowParticipationCodeBinding? = null
    private val binding get() = _binding!!

    private lateinit var BOARD_NAME: String
    private lateinit var BOARD_CODE: String

    private lateinit var UID: String

    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference: DatabaseReference = firebaseDatabase.reference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        _binding = DialogShowParticipationCodeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setPrefValue()

        generateParticipationCode()

        initCopyButton()

        initCloseButton()
    }

    private fun setPrefValue() {
        UID = SharedPreferenceController.getUid(context!!).toString()
    }

    private fun generateParticipationCode() {
        val codeLength = 6
        val charTable = charArrayOf( 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
                'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' )
        val numTable = charArrayOf( '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' )

        val random = Random(System.currentTimeMillis())
        val charTableLen = charTable.size
        val numTableLen = numTable.size
        val buf = StringBuffer()

        for (i in 1..codeLength) {
            if (i % 2 == 1)
                buf.append(charTable[random.nextInt(charTableLen)])
            else if (i % 2 == 0)
                buf.append(numTable[random.nextInt(numTableLen)])
        }
        binding.tvPartCode.text = buf.toString()
    }

    private fun initCopyButton() {
        binding.buttonCopy.setOnClickListener {
            val clipboardManager = context?.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("ID", binding.tvPartCode.text.toString())
            clipboardManager.setPrimaryClip(clipData)
            Toast.makeText(context, "Participation code copied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initCloseButton() {
        binding.buttonClose.setOnClickListener {
            createBoardCol()
            goToBoardActivity()
            finishCreateBoardActivity()
        }
    }

    private fun createBoardCol() {
        val board = BoardInfoModel()
        board.boardName = dialog_board_name
        board.memberCount = 0
        board.repo = dialog_repo_name

        BOARD_CODE = binding.tvPartCode.text.toString()
        BOARD_NAME = dialog_board_name

        databaseReference.apply {
            child("board").child(BOARD_CODE).child("info").setValue(board)
            child("users").child(UID).child("boardlist")
                    .child(BOARD_CODE).setValue(BOARD_NAME)
        }
    }

    private fun goToBoardActivity() {
        val intent = Intent(context, BoardActivity::class.java)
        intent.putExtra("boardName", BOARD_NAME)
        intent.putExtra("boardCode", BOARD_CODE)
        intent.putExtra("intentFrom", "ShowPartCodeDialogFragment")
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