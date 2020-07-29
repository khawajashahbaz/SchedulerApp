package com.Shahbaz.schedulerapp.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.Shahbaz.schedulerapp.R
import com.Shahbaz.schedulerapp.adapter.DoneTaskAdapter
import com.Shahbaz.schedulerapp.communication.SelectedTask
import com.Shahbaz.schedulerapp.databaseUitlity.entities.Task
import com.Shahbaz.schedulerapp.viewModel.SchedulerViewModel
import java.util.*

class DoneFragment : Fragment(), SelectedTask {
    private var mParam1: String? = null
    private var mParam2: String? = null
    private var mListener: OnFragmentInteractionListener? = null
    private var doneRec: RecyclerView? = null
    private var clear: Button? = null
    private var adapter: DoneTaskAdapter? = null
    private var viewModel: SchedulerViewModel? = null
    private var noDone: TextView? = null
    var first = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments!!.getString(ARG_PARAM1)
            mParam2 = arguments!!.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_done, container, false)
        viewModel = ViewModelProviders.of(this).get(SchedulerViewModel::class.java)
        doneRec = view.findViewById(R.id.done_rec)
        clear = view.findViewById(R.id.clear_history_button)
        noDone = view.findViewById(R.id.no_done)
        doneRec?.setHasFixedSize(true)
        doneRec?.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        adapter = DoneTaskAdapter(ArrayList(), context, this)
        if (first) viewModel?.donetasks?.observe(this, Observer { tasks: List<Task?>? ->
            Log.d(TAG, "onCreateView: this is Done task")
            if (tasks != null) {
                adapter!!.setList(tasks)
                noDone?.visibility = View.GONE
            } else {
                noDone?.visibility = View.VISIBLE
            }
        })
        first = false
        viewModel!!.populateDoneTasks(true)
        doneRec?.adapter = adapter
        clear?.setOnClickListener { viewModel!!.deleteTasks(true) }
        return view
    }

    fun onButtonPressed(uri: Uri?) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = if (context is OnFragmentInteractionListener) {
            context
        } else {
            throw RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    fun showConfirmDialog(category: Task?) {
        val dialog = AlertDialog.Builder(context)
                .setTitle("Delete Item")
                .setMessage("Are you sure you want to delete this Task")
                .setPositiveButton("DELETE") { dialogInterface: DialogInterface?, i: Int ->
                    viewModel!!.deleteTask(category)
                    viewModel!!.populateDoneTasks(true)
                }.setNegativeButton("CANCEL") { dialogInterface: DialogInterface, i: Int -> dialogInterface.dismiss() }.create()
        dialog.show()
    }

    override fun inform(task: Task?) {
        showConfirmDialog(task)
    }

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri?)
    }

    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"
        private const val TAG = "DoneFragment"
        fun newInstance(param1: String?, param2: String?): DoneFragment {
            val fragment = DoneFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}