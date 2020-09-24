package com.makeshift.whattimeisitthere

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.makeshift.whattimeisitthere.databinding.FragmentWhenaboutListBinding
import com.makeshift.whattimeisitthere.databinding.ListItemTimeBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.list_item_time.view.*
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "WhenaboutListFragment"
private const val DIALOG_DATE = "DialogDate"
private const val REQUEST_DATE = 0;
private const val REQUEST_PHOTO = 2;
class WhenaboutListFragment : Fragment(),  DatePickerFragment.Callbacks {

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
            whenaboutListViewModel = ViewModelProvider(
                this@WhenaboutListFragment).get(WhenaboutListViewModel::class.java)

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

    interface Toggleable {
        fun enableEdit()
        fun disableEdit()
    }


    private inner class WhenaboutAdapter(var whenabouts: List<Whenabout>) :
        RecyclerView.Adapter<WhenaboutHolder>() {

        private var current: Toggleable? = null
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
                WhenaboutHolder {
            val listItemTimeBinding = DataBindingUtil.inflate<ListItemTimeBinding>(
                layoutInflater,
                R.layout.list_item_time,
                parent,
                false
            )


            return WhenaboutHolder(listItemTimeBinding)
        }


        override fun getItemCount(): Int = whenabouts.size ?: 0

        override fun onBindViewHolder(holder: WhenaboutHolder, position: Int) {
            val whenabout = whenabouts[position]
            val backButton = holder.itemView.back_button
            val birthdayButton = holder.itemView.birthday_button
            val deleteButton = holder.itemView.delete_button



            if(position%2 == 1){
                holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"))
            } else {
                holder.itemView.setBackgroundColor(Color.parseColor("#FFFAF8FD"))
            }

            fun disableEdit(){
                current!!.disableEdit()
                binding.whenaboutListViewModel?.lastEdited = -1
                current = null
            }

            fun enableEdit(){
                current = holder
                holder.enableEdit()
                binding.whenaboutListViewModel?.lastEdited = position
            }

            holder.bind(whenabout)

            if(binding.whenaboutListViewModel?.lastEdited == position){
                enableEdit()
            }

            holder.itemView.setOnClickListener {
                if(current == null){
                    enableEdit()
                } else if(current != holder){
                    disableEdit()
                }
                true
            }

            backButton.setOnClickListener{
                disableEdit()
            }

            birthdayButton.setOnClickListener{
                DatePickerFragment.newInstance(whenabout).apply{
                    setTargetFragment(this@WhenaboutListFragment, REQUEST_DATE);
                    show(this@WhenaboutListFragment.requireFragmentManager(), DIALOG_DATE)
                }
            }

            deleteButton.setOnClickListener{
                binding.whenaboutListViewModel?.deleteWhenabout(whenabout)
            }

        }

    }


    private inner class WhenaboutHolder(private val listItemTimeBinding: ListItemTimeBinding) :
        RecyclerView.ViewHolder(listItemTimeBinding.root),Toggleable {
        private lateinit var whenabout: Whenabout

        fun getDateThere(timeZone: TimeZone): String{

            val gmtTime = Calendar.getInstance(TimeZone.getTimeZone("GMT")).timeInMillis
            val timeElsewhere = gmtTime + TimeZone.getTimeZone(timeZone.id).rawOffset

            val calendar = Calendar.getInstance(TimeZone.getTimeZone(timeZone.id))
            calendar.timeInMillis = timeElsewhere

            val dateFormat = SimpleDateFormat(" EEE, MMM dd, ''yy ")
            return dateFormat.format(calendar.time)
        }

        fun bind(whenabout: Whenabout) {
            this.whenabout = whenabout
            listItemTimeBinding.whenabout = this.whenabout
            listItemTimeBinding.isEditable = false
            listItemTimeBinding.textDate.text = getDateThere(whenabout.timeZone)
            listItemTimeBinding.photoFile = binding.whenaboutListViewModel?.getPhotoFile(whenabout)
            listItemTimeBinding.photoUri = FileProvider.getUriForFile(requireActivity(),
            "com.makeshift.whattimeisitthere.fileprovider", listItemTimeBinding.photoFile!!)

            listItemTimeBinding.profilePicture.apply {
                val packageManager = requireActivity().packageManager

                val captureImage = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                val resolvedActivity: ResolveInfo? =
                    packageManager.resolveActivity(captureImage,
                    PackageManager.MATCH_DEFAULT_ONLY)
                if(resolvedActivity == null){
                    isEnabled = true
                }

                setOnClickListener{
                    captureImage.putExtra(MediaStore.EXTRA_OUTPUT, listItemTimeBinding.photoUri)

                    val cameraActivities: List<ResolveInfo> =
                        packageManager.queryIntentActivities(captureImage,
                        PackageManager.MATCH_DEFAULT_ONLY)

                    for(cameraActivity in cameraActivities){
                        requireActivity().grantUriPermission(
                            cameraActivity.activityInfo.packageName,
                            listItemTimeBinding.photoUri,
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                        )

                        startActivityForResult(captureImage, REQUEST_PHOTO)
                    }
                }



            }




            listItemTimeBinding.executePendingBindings()
        }

        override fun enableEdit(){
            listItemTimeBinding.isEditable = true
            val spinnerAdapter = ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                TimeZone.getAvailableIDs())

            listItemTimeBinding.spinnerTimeZone.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedTimeZoneID = listItemTimeBinding.spinnerTimeZone.selectedItem.toString()
                    listItemTimeBinding.timeClock.timeZone =
                        selectedTimeZoneID
                    listItemTimeBinding.textDate.text =
                        getDateThere(TimeZone.getTimeZone(selectedTimeZoneID))
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

            listItemTimeBinding.spinnerTimeZone.adapter = spinnerAdapter
            listItemTimeBinding.spinnerTimeZone.setSelection(spinnerAdapter.getPosition(whenabout.timeZone.id))
            listItemTimeBinding.executePendingBindings()
        }

        override fun disableEdit(){
            listItemTimeBinding.isEditable = false
            whenabout.name = listItemTimeBinding.editTextName.text.toString()
            whenabout.timeZone = TimeZone.getTimeZone(listItemTimeBinding.spinnerTimeZone.selectedItem.toString())
            binding.whenaboutListViewModel?.saveWhenabout(whenabout)
            listItemTimeBinding.executePendingBindings()
        }

    }

    override fun onDateSelected(whenabout: Whenabout) {
       binding.whenaboutListViewModel?.saveWhenabout(whenabout)
    }

}
