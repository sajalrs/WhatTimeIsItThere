package com.makeshift.whattimeisitthere

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.makeshift.whattimeisitthere.databinding.FragmentWhenaboutListBinding
import com.makeshift.whattimeisitthere.databinding.ListItemTimeBinding
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.list_item_time.view.*
import java.util.*

private const val TAG = "WHenaboutListFragment"
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
        binding.executePendingBindings()
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_whenabout_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.new_contact ->
                binding.whenaboutListViewModel?.addWhenabout(Whenabout())
        }
        return super.onOptionsItemSelected(item)
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

    private interface Toggleable {
        fun enableEdit()
        fun disableEdit()
    }

    private inner class WhenaboutAdapter(var whenabouts: List<Whenabout>) :
        RecyclerView.Adapter<WhenaboutHolder>() {

        private var current: Toggleable? = null

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
            val whenabout = whenabouts[position]
            val editTextName = holder.itemView.edit_text_name
            val spinnerTimeZone = holder.itemView.spinner_time_zone
            val bacKButton = holder.itemView.back_button
            val timeClock = holder.itemView.time_clock


            fun disableEdit() {
                holder.disableEdit()
                whenabout.name = editTextName.text.toString()
                whenabout.timeZone = TimeZone.getTimeZone(spinnerTimeZone.selectedItem.toString())
                binding.whenaboutListViewModel?.saveWhenabout(whenabout)

            }

            fun enableEdit(){

                holder.enableEdit()

                val spinnerAdapter = ArrayAdapter<String>(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    TimeZone.getAvailableIDs())

                spinnerTimeZone.adapter = spinnerAdapter
                spinnerTimeZone.setSelection(spinnerAdapter.getPosition(whenabout.timeZone.id))
            }



            holder.bind(whenabout)
            

            holder.itemView.setOnClickListener {

                if(current == null){
                    current = holder
                    enableEdit()
                    spinnerTimeZone.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            timeClock.timeZone = spinnerTimeZone.selectedItem.toString()
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {

                        }
                    }

                    bacKButton.setOnClickListener{
                        disableEdit()
                    }

//                    holder.itemView.onFocusChangeListener =
//                        View.OnFocusChangeListener { v, hasFocus ->
//                            if (!hasFocus && !editTextName.hasFocus()) {
//                                disableEdit()
//                            }
//
//                        }


                } else if(current != holder){
                    current!!.disableEdit()
                    current = null
                }


                true

            }




        }

    }



    private inner class WhenaboutHolder(private val listItemTimeBinding: ListItemTimeBinding) :
        RecyclerView.ViewHolder(listItemTimeBinding.root),Toggleable {

        fun bind(whenabout: Whenabout) {
            listItemTimeBinding.whenabout = whenabout
            listItemTimeBinding.isEditable = false
            listItemTimeBinding.executePendingBindings()
        }

        override fun enableEdit(){
            listItemTimeBinding.isEditable = true
            listItemTimeBinding.executePendingBindings()
        }

        override fun disableEdit(){
            listItemTimeBinding.isEditable = false
            listItemTimeBinding.executePendingBindings()
        }

    }

}
