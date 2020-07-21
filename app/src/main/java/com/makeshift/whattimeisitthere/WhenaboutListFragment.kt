package com.makeshift.whattimeisitthere

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.makeshift.whattimeisitthere.databinding.FragmentWhenaboutListBinding
import com.makeshift.whattimeisitthere.databinding.ListItemTimeBinding
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.list_item_time.*
import kotlinx.android.synthetic.main.list_item_time.view.*
import kotlinx.android.synthetic.main.list_item_time.view.edit_text_name
import java.util.*

class WhenaboutListFragment : Fragment() {

    private lateinit var binding: FragmentWhenaboutListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_whenabout_list, container, false)

        binding.apply {
            whenaboutListViewModel = WhenaboutListViewModel()

        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = WhenaboutAdapter(emptyList())
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_whenabout_list, menu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.whenaboutListViewModel?.whenaboutsListLiveData?.observe(
            viewLifecycleOwner,
            Observer { whenabouts ->
                whenabouts?.let {
                    updateUI(whenabouts)
                }

            }
        )

    }

    private fun updateUI(whenabouts: List<Whenabout>) {
        binding.recyclerView.adapter = WhenaboutAdapter(whenabouts)
    }

    private inner class WhenaboutAdapter(var whenabouts: List<Whenabout>) :
        RecyclerView.Adapter<WhenaboutHolder>() {


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
                WhenaboutHolder {
            val binding = DataBindingUtil.inflate<ListItemTimeBinding>(
                layoutInflater,
                R.layout.list_item_time,
                parent,
                false
            )
            return WhenaboutHolder(binding)
        }

        override fun getItemCount(): Int = whenabouts.size ?: 0

        override fun onBindViewHolder(holder: WhenaboutHolder, position: Int) {
            val whenabout = whenabouts.get(position)
            val textName = holder.itemView.text_name
            val editTextName = holder.itemView.edit_text_name
            val spinnerTimeZone = holder.itemView.spinner_time_zone

            holder.bind(whenabout)

            holder.itemView.isFocusableInTouchMode = true

            holder.itemView.setOnClickListener {
                textName.visibility = View.GONE
                editTextName.visibility = View.VISIBLE
                spinnerTimeZone.visibility = View.VISIBLE

                val data = TimeZone.getAvailableIDs()
                val spinnerAdapter = ArrayAdapter<String>(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    data)

                spinnerTimeZone.adapter = spinnerAdapter
                spinnerTimeZone.setSelection(spinnerAdapter.getPosition(whenabout.timeZone.id))



                holder.itemView.onFocusChangeListener =
                    View.OnFocusChangeListener { v, hasFocus ->
                        if (!hasFocus && !editTextName.hasFocus()) {
                            textName.visibility = View.VISIBLE
                            editTextName.visibility = View.GONE
                            spinnerTimeZone.visibility = View.GONE
                            whenabout.name = editTextName.text.toString()
                            binding.whenaboutListViewModel?.saveWhenabout(whenabout)
                        }
                    }

                true
            }


        }

    }


    private inner class WhenaboutHolder(private val binding: ListItemTimeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(whenabout: Whenabout) {
            binding.whenabout = whenabout
            binding.executePendingBindings()
        }
    }

}
