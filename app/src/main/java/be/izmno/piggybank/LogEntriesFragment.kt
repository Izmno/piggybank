package be.izmno.piggybank

import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Locale

class LogEntriesFragment : Fragment() {
    private lateinit var entriesContainer: LinearLayout
    private lateinit var scrollView: ScrollView

    override fun onCreateView(
        inflater: android.view.LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val context = requireContext()
        
        // Convert dp to pixels
        val padding16dp = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            16f,
            resources.displayMetrics
        ).toInt()
        val margin8dp = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            8f,
            resources.displayMetrics
        ).toInt()
        
        // Create root ConstraintLayout
        val rootLayout = ConstraintLayout(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setPadding(padding16dp, padding16dp, padding16dp, padding16dp)
        }
        
        // Create ScrollView
        scrollView = ScrollView(context).apply {
            id = R.id.logEntriesScrollView
            layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_CONSTRAINT,
                ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
            ).apply {
                startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
            }
        }
        
        // Create LinearLayout container for entries
        entriesContainer = LinearLayout(context).apply {
            id = R.id.logEntriesContainer
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }
        
        scrollView.addView(entriesContainer)
        rootLayout.addView(scrollView)
        
        return rootLayout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Initialize repository
        LogEntryRepository.initialize(requireContext())
        
        // Load and display entries
        updateEntriesList()
    }
    
    override fun onResume() {
        super.onResume()
        // Refresh entries when returning to this fragment
        updateEntriesList()
    }
    
    private fun updateEntriesList() {
        entriesContainer.removeAllViews()
        
        val entries = LogEntryRepository.getAllEntries()
        
        if (entries.isEmpty()) {
            val emptyText = TextView(requireContext()).apply {
                text = "No entries yet"
                textSize = 16f
                gravity = Gravity.CENTER
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    val marginTop = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        32f,
                        resources.displayMetrics
                    ).toInt()
                    topMargin = marginTop
                }
            }
            entriesContainer.addView(emptyText)
            return
        }
        
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val amountFormatter = DecimalFormat("#0.00")
        val margin8dp = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            8f,
            resources.displayMetrics
        ).toInt()
        
        for (entry in entries) {
            val entryRow = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER_VERTICAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    bottomMargin = margin8dp
                }
                setPadding(margin8dp, margin8dp, margin8dp, margin8dp)
                setBackgroundColor(Color.parseColor("#F5F5F5"))
            }
            
            // Timestamp TextView
            val timestampText = TextView(requireContext()).apply {
                text = dateFormat.format(entry.timestamp)
                textSize = 14f
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
                )
            }
            
            // Amount TextView
            val amountText = TextView(requireContext()).apply {
                text = "${amountFormatter.format(entry.amount)} EUR"
                textSize = 14f
                setTypeface(null, android.graphics.Typeface.BOLD)
                gravity = Gravity.END
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    marginEnd = margin8dp
                }
            }
            
            // Delete button
            val deleteButton = MaterialButton(requireContext()).apply {
                text = "X"
                textSize = 14f
                minimumWidth = 0
                minimumHeight = 0
                setPaddingRelative(margin8dp, 0, margin8dp, 0)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                setOnClickListener {
                    LogEntryRepository.deleteEntry(entry)
                    updateEntriesList()
                }
            }
            
            entryRow.addView(timestampText)
            entryRow.addView(amountText)
            entryRow.addView(deleteButton)
            entriesContainer.addView(entryRow)
        }
    }
}

