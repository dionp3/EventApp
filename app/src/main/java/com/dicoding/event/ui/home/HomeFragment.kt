package com.dicoding.event.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.event.databinding.FragmentHomeBinding
import com.dicoding.event.data.response.ListEventsItem
import com.dicoding.event.ui.DetailActivity
import com.dicoding.event.ui.Finished.FinishedAdapter
import com.dicoding.event.ui.Finished.FinishedViewModel
import com.dicoding.event.ui.upcoming.UpcomingAdapter
import com.dicoding.event.ui.upcoming.UpcomingViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val finishedViewModel by viewModels<FinishedViewModel>()
    private val upcomingViewModel by viewModels<UpcomingViewModel>()

    private lateinit var upcomingAdapter: UpcomingAdapter
    private lateinit var finishedAdapter: FinishedAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        upcomingAdapter = UpcomingAdapter(UpcomingAdapter.DIFF_CALLBACK)
        binding.rvUpcoming.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvUpcoming.adapter = upcomingAdapter

        finishedAdapter = FinishedAdapter(FinishedAdapter.DIFF_CALLBACK)
        binding.rvFinished.layoutManager = LinearLayoutManager(context)
        binding.rvFinished.adapter = finishedAdapter

        upcomingViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showUpcomingLoading(isLoading)
        }

        finishedViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showFinishedLoading(isLoading)
        }


        upcomingViewModel.upcoming.observe(viewLifecycleOwner) { listEvents ->
            listEvents?.let {
                setUpcomingList(it.take(5))
            }
        }

        finishedViewModel.finished.observe(viewLifecycleOwner) { listEvents ->
            listEvents?.let {
                setFinishedList(it.take(5))
            }
        }

        upcomingViewModel.detailUpcoming.observe(viewLifecycleOwner) { event ->
            event?.let { detailEvent ->
                val intent = Intent(requireContext(), DetailActivity::class.java)
                intent.putExtra("EXTRA_EVENT", detailEvent)
                startActivity(intent)
            }
        }

        finishedViewModel.detailFinished.observe(viewLifecycleOwner) { event ->
            event?.let { detailEvent ->
                val intent = Intent(requireContext(), DetailActivity::class.java)
                intent.putExtra("EXTRA_EVENT", detailEvent)
                startActivity(intent)
            }
        }
    }

    private fun setUpcomingList(listEvents: List<ListEventsItem>) {
        upcomingAdapter.submitList(listEvents)

        // On item click for upcoming events
        upcomingAdapter.onItemClickListener = { event ->
            event.id?.let {
                // Log to ensure it's for upcoming events
                upcomingViewModel.detailUpcomingEvent(it)
                println("Upcoming event clicked: $it")
            }
        }
    }

    private fun setFinishedList(listEvents: List<ListEventsItem>) {
        finishedAdapter.submitList(listEvents)

        // On item click for finished events
        finishedAdapter.onItemClickListener = { event ->
            event.id?.let {
                // Log to ensure it's for finished events
                finishedViewModel.detailFinishedEvents(it)
                println("Finished event clicked: $it")
            }
        }
    }



    private fun showUpcomingLoading(isLoading: Boolean) {
        binding.progressBarUpcoming.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showFinishedLoading(isLoading: Boolean) {
        binding.progressBarFinished.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}