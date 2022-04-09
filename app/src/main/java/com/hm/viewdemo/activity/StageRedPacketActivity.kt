package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.hm.viewdemo.R
import com.hm.viewdemo.bean.SendOptionsBean
import com.hm.viewdemo.widget.RoundStageAwardView

/**
 * Created by dumingwei on 2022/4/9
 *
 * Desc: 绘制阶段红包
 */
class StageRedPacketActivity : AppCompatActivity() {


    private lateinit var roundStageAwardViewOne: RoundStageAwardView
    private lateinit var roundStageAwardViewTwo: RoundStageAwardView

    private lateinit var btnUpdateOne: Button
    private lateinit var btnUpdateTwo: Button

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, StageRedPacketActivity::class.java)
            context.startActivity(intent)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stage_red_packet)

        roundStageAwardViewOne = findViewById(R.id.stageAwardView0)
        roundStageAwardViewTwo = findViewById(R.id.stageAwardView1)

        btnUpdateOne = findViewById(R.id.btnUpdateStage3)
        btnUpdateTwo = findViewById(R.id.btnUpdateStage6)


        val stageList1 = arrayListOf<SendOptionsBean>()

        val stageList2 = arrayListOf<SendOptionsBean>()

        for (i in 0 until 9) {
            val bean = SendOptionsBean()
            bean.seg = i + 1
            bean.segKey = 0.5 * i
            bean.segValue = 30 * i
            bean.status = 2
            stageList1.add(bean)
        }

        for (i in 0 until 9) {
            val bean = SendOptionsBean()
            bean.seg = i + 1

            bean.segKey = 0.5 * i

            bean.segValue = 30 * i
            bean.status = 2
            stageList2.add(bean)
        }

        roundStageAwardViewTwo.setData(stageList2, 7, 0.5f)


        btnUpdateOne.setOnClickListener {
            roundStageAwardViewOne.setData(stageList1, 5, 0.5f)
        }
        btnUpdateTwo.setOnClickListener {
            roundStageAwardViewTwo.setData(stageList2, 9, 0.5f)

        }

    }


}