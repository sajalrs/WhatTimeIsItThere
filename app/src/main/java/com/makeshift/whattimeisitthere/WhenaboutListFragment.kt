package com.makeshift.whattimeisitthere

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.makeshift.whattimeisitthere.databinding.FragmentWhenaboutListBinding
import com.makeshift.whattimeisitthere.databinding.ListItemTimeBinding
import java.util.*

class WhenaboutListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentWhenaboutListBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_whenabout_list, container, false)

        val whenabouts: MutableList<Whenabout> = emptyList<Whenabout>().toMutableList()
      //TODO: Remove list and add viewmodel to populate
        whenabouts.add(Whenabout(UUID.randomUUID(),"Sajal\nSatyal", TimeZone.getDefault()))
        whenabouts.add(Whenabout(UUID.randomUUID(),"Sagun\nSatyal", TimeZone.getDefault()))


        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = WhenaboutAdapter(whenabouts)
        }

        return binding.root
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
            holder.bind(whenabout)
        }

    }


    private inner class WhenaboutHolder(private val binding: ListItemTimeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(whenabout: Whenabout){
            binding.whenabout = whenabout
            binding.executePendingBindings()
        }
    }

}
