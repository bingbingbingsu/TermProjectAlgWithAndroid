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
//import androidx.lifecycle.ViewModelProvider
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
//        val homeViewModel =
//            ViewModelProvider(this).get(HomeViewModel::class.java)

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
                R.id.radioButtonChuncheon -> startingLoc = "chuncheon"
                R.id.radioButtonGangneung -> startingLoc = "gangneung"
                R.id.radioButtonJeonju -> startingLoc = "jeonju"
                R.id.radioButtonPohang -> startingLoc = "pohang"
                R.id.radioButtonUlsan -> startingLoc = "ulsan"
            }
        }

        radioGroupD.setOnCheckedChangeListener{group, checkedId->
            when (checkedId) {
                R.id.radioButtonSeoulD -> destination = "seoul"
                R.id.radioButtonBusanD -> destination = "busan"
                R.id.radioButtonGwangjuD -> destination = "gwangju"
                R.id.radioButtonGyeongjuD -> destination = "gyeongju"
                R.id.radioButtonDaeguD -> destination = "daegu"
                R.id.radioButtonChuncheonD -> destination = "chuncheon"
                R.id.radioButtonGangneungD -> destination = "gangneung"
                R.id.radioButtonJeonjuD -> destination = "jeonju"
                R.id.radioButtonPohangD -> destination = "pohang"
                R.id.radioButtonUlsanD -> destination = "ulsan"
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

        button.setOnClickListener {
            if (startingLoc == destination) {
                Toast.makeText(activity, "Please select different values", Toast.LENGTH_SHORT)
                    .show()
            } else if (startingLoc == "a" || destination == "b") {
                Toast.makeText(activity, "Please select values", Toast.LENGTH_SHORT).show()
            } else {
                algorithm(resultTextView)
            }
        }


        return root
    }
    private fun algorithm(resultTextView: TextView) {
        var iii = 9
        val names:Array<String> = arrayOf("seoul","busan","gwangju","gyeongju","daegu", "chuncheon", "gangneung", "jeonju", "pohang", "ulsan")
        val time = arrayOf(
            arrayOf(0.0, 300.0, 240.0, 255.0, 234.0, 80.0, 140.0, 137.0, 111.0, 232.0), // seoul부터 다른 노드까지
            arrayOf(300.0, 0.0, 178.0, 84.0, 112.0, 256.0, 245.0, 163.0, 177.0, 60.0), // busan부터 다른 노드까지
            arrayOf(240.0, 178.0, 0.0, 172.0, 136.0, 252.0, 276.0, 78.0, 114.0, 195.0), // gwanju부터 다른 노드까지
            arrayOf(255.0, 84.0, 172.0, 0.0, 79.0, 215.0, 187.0, 166.0, 144.0, 44.0), // gyeongju부터 다른 노드까지
            arrayOf(234.0, 112.0, 136.0, 79.0, 0.0, 189.0, 209.0, 121.0, 107.0, 86.0), // daegu부터 다른 노드까지
            arrayOf(62.0, 256.0, 252.0, 215.0, 189.0, 0.0, 65.0, 207.0, 166.0, 248.0), // chuncheon부터 다른 노드까지
            arrayOf(122.0, 245.0, 276.0, 187.0, 209.0, 65.0, 0.0, 237.0, 189.0, 221.0), // gangneung부터 다른 노드까지
            arrayOf(137.0, 163.0, 78.0, 166.0, 121.0, 207.0, 237.0, 0.0, 72.0, 204.0), // jeonju부터 다른 노드까지
            arrayOf(75.0, 177.0, 114.0, 144.0, 107.0, 166.0, 189.0, 72.0, 0.0, 183.0), // pohang부터 다른 노드까지
            arrayOf(232.0, 60.0, 195.0, 44.0, 86.0, 248.0, 221.0, 204.0, 183.0, 0.0) // ulsan부터 다른 노드까지
        )

        val cost = arrayOf( // 0.0000000000000001 은 0 (0은 노드가 연결 안되어있다라고 알고리즘을 만들어서.....)
            arrayOf(0.0, 19.3, 14.7, 17.7, 12.9, 3.9, 10.9, 11.3, 8.5, 19.8), // seoul부터 다른 노드까지
            arrayOf(19.3, 0.0, 12.8, 4.7, 7.0, 19.4, 9.4, 11.7, 13.9, 0.0000000000000001), // busan부터 다른 노드까지
            arrayOf(14.7, 12.8, 0.0, 14.1, 9.7, 20.5, 20.2, 0.0000000000000001, 7.7, 14.1), // gwanju부터 다른 노드까지
            arrayOf(17.7, 4.7, 14.1, 0.0, 4.3, 16.7, 3.5, 12.3, 11.6, 2.0), // gyeongju부터 다른 노드까지
            arrayOf(12.9, 7.0, 9.7, 4.3, 0.0, 13.5, 15.5, 8.2, 8.1, 6.0), // daegu부터 다른 노드까지
            arrayOf(3.9, 19.4, 20.5, 16.7, 13.5, 0.0, 7.4, 17.9, 11.4, 18.8), // chuncheon부터 다른 노드까지
            arrayOf(10.9, 9.4, 20.2, 3.5, 15.5, 7.4, 0.0, 16.8, 13.2, 6.9), // gangneung부터 다른 노드까지
            arrayOf(11.3, 11.7, 0.0000000000000001, 12.3, 8.2, 17.9, 16.8, 0.0, 4.3, 13.8), // jeonju부터 다른 노드까지
            arrayOf(8.5,13.9, 7.7, 11.6, 8.1, 11.4, 13.2, 4.3, 0.0, 13.9), // pohang부터 다른 노드까지
            arrayOf(19.8, 0.0000000000000001, 14.1, 2.0, 6.0, 18.8, 6.9, 13.8, 13.9, 0.0) // ulsan부터 다른 노드까지
        )

        val efficiency = Array(time.size){Array(time.size) {0.0} }

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






        val startingNode = names.indexOf(startingLoc)
        val destinationNode = names.indexOf(destination)

        when (mode) {
            "timeM" -> {
                val minTime:Array<Double> = arrayOf(0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0)
                val minTimeParent:Array<Int> = arrayOf(0,0,0,0,0,0,0,0,0,0)
                dijkstra(minTime,minTimeParent,startingNode,time)
                val outCost: Double = minTime[destinationNode] / 60.0 // 분 단위로 변환하여 소수점으로 표현
                val firstOutput = "Time: ${"%.2f".format(outCost)}"
                val timeName:Array<Int> = arrayOf(-1,-1,-1,-1,-1,-1,-1,-1,-1,-1)
                var timeTracebackOut = ""
                traceback(timeName,minTimeParent,destinationNode,startingNode,0)

                while (iii!=0) {
                    if (timeName[iii] != -1) {
                        timeTracebackOut += "${names[timeName[iii]]} -> "
                    }
                    iii--
                }
                timeTracebackOut += names[timeName[iii]]
                resultTextView.text = firstOutput+"\n"+timeTracebackOut
            }
            "costM" -> {
                val minCost:Array<Double> = arrayOf(0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0)
                val minCostParent:Array<Int> = arrayOf(0,0,0,0,0,0,0,0,0,0)
                dijkstra(minCost,minCostParent,startingNode,cost)
                val outCost: Double = minCost[destinationNode]
                val firstOutput = "Cost: ${"%.2f".format(outCost)}"
                val costName:Array<Int> = arrayOf(-1,-1,-1,-1,-1,-1,-1,-1,-1,-1)
                var costTracebackOut = ""
                traceback(costName,minCostParent,destinationNode,startingNode,0)

                while (iii!=0) {
                    if (costName[iii] != -1) {
                        costTracebackOut += "${names[costName[iii]]} -> "
                    }
                    iii--
                }
                costTracebackOut += names[costName[iii]]
                resultTextView.text = firstOutput+"\n"+costTracebackOut
            }
            "effM" -> {
                val minEff:Array<Double> = arrayOf(0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0)
                val minEffParent:Array<Int> = arrayOf(0,0,0,0,0,0,0,0,0,0)
                dijkstra(minEff,minEffParent,startingNode,efficiency)
                val effName:Array<Int> = arrayOf(-1,-1,-1,-1,-1,-1,-1,-1,-1,-1)
                var effTracebackOut = ""
                traceback(effName,minEffParent,destinationNode,startingNode,0)

                while (iii!=0) {
                    if (effName[iii] != -1) {
                        effTracebackOut += "${names[effName[iii]]} -> "
                        Log.i("test","000")
                    }
                    Log.i("test","!!!")
                    iii--
                }
                effTracebackOut += names[effName[iii]]
                resultTextView.text = "eff:\n$effTracebackOut"
            }

        }



    }

    private fun dijkstra(distant:Array<Double>, parent:Array<Int>, startingNode:Int, graph:Array<Array<Double>>) {
        val visited:Array<Int> = arrayOf(0,0,0,0,0,0,0,0,0,0)

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

    private fun traceback(names:Array<Int>, parent: Array<Int>, start:Int, dest:Int, flag:Int ) {
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