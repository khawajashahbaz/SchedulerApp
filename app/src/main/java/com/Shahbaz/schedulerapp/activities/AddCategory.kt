package com.Shahbaz.schedulerapp.activities

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.Shahbaz.schedulerapp.R
import com.Shahbaz.schedulerapp.communication.SelectedCategory
import com.Shahbaz.schedulerapp.databaseUitlity.entities.Category
import com.Shahbaz.schedulerapp.databinding.ActivityAddCategoryBinding
import com.Shahbaz.schedulerapp.dialogues.CategoryIconDialogue
import com.Shahbaz.schedulerapp.helpUtiity.Modes
import com.Shahbaz.schedulerapp.viewModel.SchedulerViewModel
import com.turkialkhateeb.materialcolorpicker.ColorChooserDialog

class AddCategory : AppCompatActivity(), SelectedCategory {
    var viewModel: SchedulerViewModel? = null
    var binding: ActivityAddCategoryBinding? = null
    var dialogue: CategoryIconDialogue? = null
    private var color = -4
    private var currentMode = Modes.ADD_MMODE
    private var category: Category? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBinding(savedInstanceState)
        supportActionBar!!.hide()
        initializeColor()
        handleEditing()
        handleAddCat()
        handleDelete()
    }

    private fun handleDelete() {
        binding!!.categoryDelete.setOnClickListener { v: View? -> showConfirmDialog() }
    }

    fun showConfirmDialog() {
        val dialog = AlertDialog.Builder(this)
                .setTitle("Delete Item")
                .setMessage("Are you sure you want to delete this Category" +
                        "All respective tasks will be deleted also")
                .setPositiveButton("DELETE") { dialogInterface: DialogInterface?, i: Int ->
                    viewModel!!.deleteCat(category)
                    finish()
                }.setNegativeButton("CANCEL") { dialogInterface: DialogInterface, i: Int -> dialogInterface.dismiss() }.create()
        dialog.show()
    }

    private fun handleEditing() {
        category = Category()
        if (intent.getParcelableExtra<Parcelable?>("CategoryClass") != null) {
            binding!!.categoryDelete.isEnabled = true
            currentMode = Modes.EDIT_MODE
            category = intent.getParcelableExtra("CategoryClass")
            color = category?.color!!
            binding!!.categoryIcon.text = category?.icon
            binding!!.categoryTitle.setText(category?.name)
            changeLayoutColors(color)
        } else {
            binding!!.categoryDelete.isEnabled = false
        }
    }

    private fun handleAddCat() {
        binding!!.addCategorySaveButton.setOnClickListener { v: View? ->
            if (finalCheck()) {
                category?.icon = binding!!.categoryIcon.text.toString()
                category?.name = binding!!.categoryTitle.text.toString()
                category?.color = color
                if (currentMode == Modes.ADD_MMODE) {
                    viewModel!!.addCat(category)
                } else {
                    viewModel!!.editCat(category)
                }
                finish()
            } else {
                binding!!.categoryTitle.error = "Please fill title"
                Toast.makeText(this, "Please fill title", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun finalCheck(): Boolean {
        if (color == -4) color = -16738680
        return if (binding!!.categoryTitle.text.toString().trim { it <= ' ' }.isEmpty()) false else true
    }

    private fun initializeColor() {
        binding!!.colorPallette.setOnClickListener { v: View? -> handleColor() }
        binding!!.categoryIconChoose.setOnClickListener { v: View? -> openIconDialogue() }
    }

    private fun openIconDialogue() {
        dialogue = CategoryIconDialogue()
        dialogue!!.show(supportFragmentManager, "cat_Icon")
    }

    private fun handleColor() {
        val dialog = ColorChooserDialog(this)
        dialog.setTitle("Choose color")
        dialog.setColorListener { v: View?, color: Int -> changeLayoutColors(color) }
        dialog.show()
    }

    private fun changeLayoutColors(color: Int) {
        this.color = color
        binding!!.addCategorySaveButton.setBackgroundColor(color)
        binding!!.l4.setBackgroundColor(color)
        binding!!.l3.setBackgroundColor(color)
        binding!!.doneCircular.setColorFilter(color)
    }

    private fun setBinding(savedInstanceState: Bundle?) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_category)
        viewModel = ViewModelProviders.of(this).get(SchedulerViewModel::class.java)
    }

    override fun selected(s: Category?) {}
    override fun selected(s: String?) {
        binding!!.categoryIcon.text = s
        dialogue!!.dismiss()
    }

    companion object {
        private const val TAG = "AddCategory"
    }
}