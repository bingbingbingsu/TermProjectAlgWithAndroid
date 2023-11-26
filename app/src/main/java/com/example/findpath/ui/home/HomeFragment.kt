package com.example.findpath.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.findpath.R
import com.example.findpath.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var startingLoc:String = "a"
    private var destination:String = "b"

    private var mode:String = "timeM"

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val resultTextView = binding.textView

        val radioGroupS = binding.radioGroupS
        val radioGroupD = binding.radioGroupD


        radioGroupS.setOnCheckedChangeListener{group, checkedId->
            when (checkedId) {
                R.id.radioButtonSeoul -> startingLoc = "seoul"
                R.id.radioButtonBusan -> startingLoc = "busan"
                R.id.radioButtonGwangju -> startingLoc = "gwangju"
                R.id.radioButtonGyeongju -> startingLoc = "gyeongju"
                R.id.radioButtonDaegu -> startingLoc = "daegu"
            }
        }

        radioGroupD.setOnCheckedChangeListener{group, checkedId->
            when (checkedId) {
                R.id.radioButtonSeoulD -> destination = "seoul"
                R.id.radioButtonBusanD -> destination = "busan"
                R.id.radioButtonGwangjuD -> destination = "gwangju"
                R.id.radioButtonGyeongjuD -> destination = "gyeongju"
                R.id.radioButtonDaeguD -> destination = "daegu"
            }
        }

        val radioMode = binding.mode
        radioMode.setOnCheckedChangeListener{group:RadioGroup,checkedId:Int->
            when (checkedId) {
                R.id.timeM -> mode = "timeM"
                R.id.distantM -> mode = "costM"
                R.id.efficiencyM -> mode = "effM"
            }
        }

        val button = binding.button

        button.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                if (startingLoc==destination) {
                    Toast.makeText(activity,"Please select different values",Toast.LENGTH_SHORT).show()
                }else if (startingLoc=="a") {
                    Toast.makeText(activity,"Please select values",Toast.LENGTH_SHORT).show()
                }
                else {
                    algorithm(resultTextView)
                }
            }
        })


        return root
    }
    fun algorithm(resultTextView: TextView) {
        var iii = 4
        val names:Array<String> = arrayOf("seoul","busan","gwangju","gyeongju","daegu")
        val time = arrayOf(
            arrayOf(0.0, 300.0, 240.0, 255.0, 234.0),
            arrayOf(300.0, 0.0, 178.0, 84.0, 112.0),
            arrayOf(240.0, 178.0, 0.0, 172.0, 136.0),
            arrayOf(255.0, 84.0, 172.0, 0.0, 79.0),
            arrayOf(234.0, 112.0, 136.0, 79.0, 0.0)
        )

        val cost = arrayOf(
            arrayOf(0.0, 19.3, 14.7, 17.7, 12.9),
            arrayOf(19.3, 0.0, 12.8, 4.7, 7.0),
            arrayOf(14.7, 12.8, 0.0, 14.1, 9.7),
            arrayOf(17.7, 4.7, 14.1, 0.0, 4.3),
            arrayOf(12.9, 7.0, 9.7, 4.3, 0.0)
        )

        val efficiency = Array(time.size){Array<Double>(time.size) {0.0} }

        for (i in time.indices) {
            for (k in time.indices) {
                if (time[i][k] * cost[i][k]>=5000) // 5000이 일정 효율
                {
                    efficiency[i][k] = 1.0
                }
                else {
                    efficiency[i][k] = time[i][k]
                }
            }
        }

        var startingNode:Int = -1
        var destinationNode:Int = -1

        startingNode = names.indexOf(startingLoc)
        destinationNode = names.indexOf(destination)

        when (mode) {
            "timeM" -> {
                var minTime:Array<Double> = arrayOf(0.0,0.0,0.0,0.0,0.0)
                var minTimeParent:Array<Int> = arrayOf(0,0,0,0,0)
                dijkstra(minTime,minTimeParent,startingNode,time)
                val outCost: Double = minTime[destinationNode] / 60.0 // 분 단위로 변환하여 소수점으로 표현
                val firstOutput: String = "Time: ${"%.2f".format(outCost)}"
                var timeName:Array<Int> = arrayOf(-1,-1,-1,-1,-1)
                var timeTracebackOut:String = ""
                traceback(timeName,minTimeParent,destinationNode,startingNode,0)

                while (iii!=0) {
                    if (timeName[iii] != -1) {
                        timeTracebackOut += "${names[timeName[iii]]} -> "
                    }
                    iii--
                }
                timeTracebackOut += "${names[timeName[iii]]}"
                resultTextView.setText(firstOutput+"\n"+timeTracebackOut)
            }
            "costM" -> {
                var minCost:Array<Double> = arrayOf(0.0,0.0,0.0,0.0,0.0)
                var minCostParent:Array<Int> = arrayOf(0,0,0,0,0)
                dijkstra(minCost,minCostParent,startingNode,cost)
                val outCost: Double = minCost[destinationNode]
                val firstOutput: String = "Cost: ${"%.2f".format(outCost)}"
                var costName:Array<Int> = arrayOf(-1,-1,-1,-1,-1)
                var costTracebackOut:String = ""
                traceback(costName,minCostParent,destinationNode,startingNode,0)

                while (iii!=0) {
                    if (costName[iii] != -1) {
                        costTracebackOut += "${names[costName[iii]]} -> "
                    }
                    iii--
                }
                costTracebackOut += "${names[costName[iii]]}"
                resultTextView.setText(firstOutput+"\n"+costTracebackOut)
            }
            "effM" -> {
                var minEff:Array<Double> = arrayOf(0.0,0.0,0.0,0.0,0.0)
                var minEffParent:Array<Int> = arrayOf(0,0,0,0,0)
                dijkstra(minEff,minEffParent,startingNode,efficiency)
                var effName:Array<Int> = arrayOf(-1,-1,-1,-1,-1)
                var effTracebackOut:String = ""
                traceback(effName,minEffParent,destinationNode,startingNode,0)

                while (iii!=0) {
                    if (effName[iii] != -1) {
                        effTracebackOut += "${names[effName[iii]]} -> "
                        Log.i("test","000")
                    }
                    Log.i("test","!!!")
                    iii--
                }
                effTracebackOut += "${names[effName[iii]]}"
                resultTextView.setText("eff:"+"\n"+effTracebackOut)
            }

        }



    }

    fun dijkstra(distant:Array<Double>, parent:Array<Int>,startingNode:Int,graph:Array<Array<Double>>) {
        var visited:Array<Int> = arrayOf(0,0,0,0,0)

        for (i in distant.indices) {
            distant[i] = Double.POSITIVE_INFINITY
        }

        distant[startingNode] = 0.0
        parent[startingNode] = 0

        for (i in 0 until distant.size-1) {
            var mindistant:Double = Double.POSITIVE_INFINITY
            var index:Int = -1

            for (j in distant.indices) {
                if (visited[j]==0 && distant[j]<mindistant) {
                    mindistant = distant[j]
                    index = j
                }
            }
            visited[index] =1

            for (k in distant.indices) {
                if(visited[k]==0&&graph[index][k] + distant[index] <distant[k] &&graph[index][k]!=0.0) {
                    distant[k] = graph[index][k] + distant[index]
                    parent[k] = index
                }
            }
        }
    }

    fun traceback(names:Array<Int>, parent: Array<Int>, start:Int, dest:Int, flag:Int ) {
        if (start==dest) {
            names[flag]=start
        } else {
            names[flag]=start
            traceback(names,parent,parent[start],dest,flag+1)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}