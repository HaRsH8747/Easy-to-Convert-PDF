package com.easytoconvertpdf

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import com.easytoconvertpdf.Utils.ItemTouchHelperClass
import com.easytoconvertpdf.adapter.AdapterGridBasic
import com.easytoconvertpdf.adapter.ImageDocument
import com.easytoconvertpdf.adapter.SpacingItemDecoration
import com.easytoconvertpdf.databinding.ActivityImgToPdfBinding
import com.google.android.material.internal.ViewUtils.dpToPx
import com.theartofdev.edmodo.cropper.CropImage

class ImgToPdfActivity : AppCompatActivity() {

    private lateinit var adapterGridBasic: AdapterGridBasic
    private lateinit var binding: ActivityImgToPdfBinding
    var currenSelected = -1
    var itemTouchHelper: ItemTouchHelper? = null
    private val actionModeCallback: ActionModeCallback? = null
    private var actionMode: ActionMode? = null

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImgToPdfBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvImgSelectItem.layoutManager = GridLayoutManager(this, 3)
        binding.rvImgSelectItem.itemAnimator = null
        binding.rvImgSelectItem.addItemDecoration(SpacingItemDecoration(3,
            dpToPx(this, 2).toInt(),
            true))
        binding.rvImgSelectItem.setHasFixedSize(true)
        adapterGridBasic = AdapterGridBasic(this, utils.documents)
        binding.rvImgSelectItem.adapter = adapterGridBasic


        // on item list clicked
        adapterGridBasic.setOnItemClickListener(object : AdapterGridBasic.OnItemClickListener {
            override fun onItemClick(view: View?, obj: ImageDocument?, position: Int) {
                if (adapterGridBasic.selectedItemCount > 0) {
                    enableActionMode(position)
                } else {
                    currenSelected = position
                    CropImage.activity(adapterGridBasic.getItem(position).imageDocument)
                        .start(this@ImgToPdfActivity)
                }
            }

            override fun onItemLongClick(view: View?, obj: ImageDocument?, pos: Int) {
                enableActionMode(pos)
            }
        })

        adapterGridBasic.setDragListener { viewHolder ->
            if (actionMode == null) itemTouchHelper!!.startDrag(viewHolder)
        }
        val callback: ItemTouchHelper.Callback = ItemTouchHelperClass(adapterGridBasic)
        itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper!!.attachToRecyclerView(binding.rvImgSelectItem)
    }

    private fun deleteItems() {
        val selectedItemPositions: List<Int> = adapterGridBasic.getSelectedItems()
        for (i in selectedItemPositions.indices.reversed()) {
            adapterGridBasic.removeData(selectedItemPositions[i])
        }
        adapterGridBasic.notifyDataSetChanged()
    }

    private fun enableActionMode(position: Int) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback!!)
        }
        toggleSelection(position)
    }

    private fun toggleSelection(position: Int) {
        adapterGridBasic.toggleSelection(position)
        // ItemTouchHelperClass.isItemSwipe = false;
        val count: Int = adapterGridBasic.getSelectedItemCount()
        if (count == 0) {
            actionMode?.finish()
        } else {
            actionMode?.title = count.toString()
            actionMode?.invalidate()
        }
    }

    private fun selectAll() {
        adapterGridBasic.selectAll()
        val count: Int = adapterGridBasic.getSelectedItemCount()
        if (count == 0) {
            actionMode?.finish()
        } else {
            actionMode?.title = count.toString()
            actionMode?.invalidate()
        }
    }

    inner class ActionModeCallback : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            mode.menuInflater.inflate(R.menu.menu_delete, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
            return false
        }

        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            val id = item.itemId
            if (id == R.id.action_delete) {
                deleteItems()
                mode.finish()
                return true
            }
            if (id == R.id.select_all) {
                selectAll()
                return true
            }
            return false
        }

        override fun onDestroyActionMode(mode: ActionMode) {
            adapterGridBasic.clearSelections()
            actionMode = null
        }
    }

//    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//    private var mainMenuItem: MenuItem? = null
//    private var isChecked = false
//
//    //>>>>>>>>>>>>MENU
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        val inflater = menuInflater
//        inflater.inflate(R.menu.sortmenu, menu)
//        mainMenuItem = menu.findItem(R.id.fileSort)
//        return true
//    }
//
//    var comparator: Comparator<ImageDocument>? = null
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle item selection
//        return when (item.itemId) {
//            R.id.nameSort -> {
//                mainMenuItem!!.title = "Name"
//                comparator = FileComparator.getNameFileComparator()
//                FileComparator.isDescending = isChecked
//                sortFiles(comparator)
//                true
//            }
//            R.id.modifiedSort -> {
//                mainMenuItem!!.title = "Modified"
//                comparator = FileComparator.getLastModifiedFileComparator()
//                FileComparator.isDescending = isChecked
//                sortFiles(comparator)
//                true
//            }
//            R.id.sizeSort -> {
//                mainMenuItem!!.title = "Size"
//                comparator = FileComparator.getSizeFileComparator()
//                FileComparator.isDescending = isChecked
//                sortFiles(comparator)
//                true
//            }
//            R.id.ordering -> {
//                isChecked = !isChecked
//                if (isChecked) {
//                    item.setIcon(R.drawable.ic_keyboard_arrow_up_black_24dp)
//                } else {
//                    item.setIcon(R.drawable.ic_keyboard_arrow_down_black_24dp)
//                }
//                if (comparator != null) {
//                    FileComparator.isDescending = isChecked
//                    sortFiles(comparator)
//                } else {
//                    comparator = FileComparator.getLastModifiedFileComparator()
//                    FileComparator.isDescending = isChecked
//                    sortFiles(comparator)
//                }
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

}