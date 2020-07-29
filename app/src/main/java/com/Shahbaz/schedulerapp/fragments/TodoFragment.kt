package com.Shahbaz.schedulerapp.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.getbase.floatingactionbutton.FloatingActionButton
import com.Shahbaz.schedulerapp.R
import com.Shahbaz.schedulerapp.activities.AddCategory
import com.Shahbaz.schedulerapp.activities.AddTask
import com.Shahbaz.schedulerapp.adapter.ExpandableListAdapter
import com.Shahbaz.schedulerapp.databaseUitlity.entities.Category
import com.Shahbaz.schedulerapp.databaseUitlity.entities.Task
import com.Shahbaz.schedulerapp.viewModel.SchedulerViewModel
import java.util.*

class TodoFragment : Fragment() {
    private var mParam1: String? = null
    private var mParam2: String? = null
    private var mListener: OnFragmentInteractionListener? = null
    private var listView: ExpandableListView? = null
    private var adapter: ExpandableListAdapter? = null
    private var addCategory: FloatingActionButton? = null
    private var addTask: FloatingActionButton? = null
    private var viewModel: SchedulerViewModel? = null
    private var noDone: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments!!.getString(ARG_PARAM1)
            mParam2 = arguments!!.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_todo, container, false)
    }

    fun onButtonPressed(uri: Uri?) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noDone = view.findViewById(R.id.no_done_cat)
        viewModel = ViewModelProviders.of(this).get(SchedulerViewModel::class.java)
        listView = view.findViewById(R.id.expandable_list_liew)
        listView?.isSmoothScrollbarEnabled = true
        initializeAdapter()
        initializeExpandableView()
        initializeFloatButton(view)
    }

    private fun initializeFloatButton(view: View) {
        addCategory = view.findViewById(R.id.action_category)
        addTask = view.findViewById(R.id.action_task)
        addCategory?.setOnClickListener { context!!.startActivity(Intent(context, AddCategory::class.java)) }
        addTask?.setOnClickListener{ context!!.startActivity(Intent(context, AddTask::class.java)) }
    }

    private fun initializeExpandableView() {
        listView!!.setAdapter(adapter)
        listView!!.setGroupIndicator(null)
        listView!!.setChildIndicator(null)
        listView!!.divider = resources.getDrawable(android.R.color.transparent)
        listView!!.setChildDivider(resources.getDrawable(android.R.color.transparent))
        listView!!.dividerHeight = resources.getDimension(R.dimen.list_seperator_height).toInt()
    }

    private fun initializeAdapter() {
        adapter = ExpandableListAdapter(ArrayList()
                , HashMap(), context)
        viewModel?.categoryLiveData?.removeObservers(this)
        viewModel?.categoryLiveData?.observe(this, Observer { categories: List<Category?>? -> adapter!!.setGroup(categories?.requireNoNulls()) })
        viewModel?.populateCat()
        viewModel?.taskLiveData?.removeObservers(this)
        viewModel?.taskLiveData?.observe(this, Observer { integerListHashMap: HashMap<Int, List<Task?>?>? ->
            if (integerListHashMap != null) {
                noDone!!.visibility = View.GONE
                adapter!!.setChild(integerListHashMap)
            } else {
                noDone!!.visibility = View.VISIBLE
            }
        })
        viewModel!!.populateTask()
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

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri?)
    }

    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"
        fun newInstance(param1: String?, param2: String?): TodoFragment {
            val fragment = TodoFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}