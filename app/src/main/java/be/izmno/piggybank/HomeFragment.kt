package be.izmno.piggybank

import android.os.Bundle
import android.text.InputType
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.DecimalFormat

class HomeFragment : Fragment() {
    private lateinit var piggyBankView: PiggyBankView
    private lateinit var clickButton: MaterialButton
    private lateinit var customAmountButton: TextView

    override fun onCreateView(
        inflater: android.view.LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val context = requireContext()
        
        // Convert dp to pixels
        val marginTop32dp = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            32f,
            resources.displayMetrics
        ).toInt()
        
        val marginTop16dp = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            16f,
            resources.displayMetrics
        ).toInt()
        
        // Create root ConstraintLayout
        val rootLayout = ConstraintLayout(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
        
        // Create PiggyBankView for total amount
        piggyBankView = PiggyBankView(context).apply {
            id = R.id.counterText
            layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomToTop = R.id.clickButton
                startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                verticalChainStyle = ConstraintLayout.LayoutParams.CHAIN_PACKED
            }
        }
        
        // Create MaterialButton
        clickButton = MaterialButton(context).apply {
            id = R.id.clickButton
            text = getString(R.string.click_button)
            textSize = 18f
            layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = marginTop32dp
                bottomToTop = R.id.customAmountButton
                startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                topToBottom = R.id.counterText
            }
        }
        
        // Create custom amount button (clickable text)
        customAmountButton = TextView(context).apply {
            id = R.id.customAmountButton
            text = getString(R.string.add_custom_amount)
            textSize = 14f
            setTextColor(ContextCompat.getColor(context, android.R.color.holo_blue_dark))
            setPadding(0, marginTop16dp, 0, 0)
            layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
                startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                topToBottom = R.id.clickButton
            }
        }
        
        // Add views to root layout
        rootLayout.addView(piggyBankView)
        rootLayout.addView(clickButton)
        rootLayout.addView(customAmountButton)
        
        return rootLayout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize repository
        LogEntryRepository.initialize(requireContext())
        
        // Update total amount display
        updateTotalAmount()

        clickButton.setOnClickListener {
            val entry = LogEntry(
                timestamp = System.currentTimeMillis(),
                amount = 15.0
            )
            LogEntryRepository.addEntry(entry)
            updateTotalAmount()
        }
        
        customAmountButton.setOnClickListener {
            showCustomAmountDialog()
        }
    }
    
    override fun onResume() {
        super.onResume()
        // Update total when returning to this fragment (in case entries were deleted elsewhere)
        updateTotalAmount()
    }
    
    private fun updateTotalAmount() {
        val total = LogEntryRepository.getTotalAmount()
        val formatter = DecimalFormat("#0.00")
        piggyBankView.text = "€${formatter.format(total)}"
    }
    
    private fun showCustomAmountDialog() {
        val context = requireContext()
        
        // Create input layout and edit text
        val inputLayout = TextInputLayout(context).apply {
            hint = getString(R.string.dialog_amount_label)
            setPadding(
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20f, resources.displayMetrics).toInt(),
                0,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20f, resources.displayMetrics).toInt(),
                0
            )
        }
        
        val inputEditText = TextInputEditText(context).apply {
            inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
            hint = "0.00"
        }
        
        inputLayout.addView(inputEditText)
        
        MaterialAlertDialogBuilder(context)
            .setTitle(getString(R.string.dialog_add_custom_amount_title))
            .setView(inputLayout)
            .setPositiveButton(getString(R.string.dialog_confirm)) { _, _ ->
                val inputText = inputEditText.text?.toString()?.trim()
                if (!inputText.isNullOrEmpty()) {
                    try {
                        val amount = inputText.toDouble()
                        if (amount > 0) {
                            val entry = LogEntry(
                                timestamp = System.currentTimeMillis(),
                                amount = amount
                            )
                            LogEntryRepository.addEntry(entry)
                            updateTotalAmount()
                        } else {
                            showErrorDialog(getString(R.string.dialog_invalid_amount))
                        }
                    } catch (e: NumberFormatException) {
                        showErrorDialog(getString(R.string.dialog_invalid_amount))
                    }
                }
            }
            .setNegativeButton(getString(R.string.dialog_cancel), null)
            .show()
    }
    
    private fun showErrorDialog(message: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }
}


