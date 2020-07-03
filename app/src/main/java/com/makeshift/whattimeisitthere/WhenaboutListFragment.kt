package com.makeshift.whattimeisitthere

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.makeshift.whattimeisitthere.databinding.FragmentWhenaboutListBinding

class WhenaboutListFragment: Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentWhenaboutListBinding =
            DataBindingUtil.inflate(inflater,R.layout.fragment_whenabout_list, container,false)

        binding.recyclerView.apply{
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        return binding.root
    }

}
