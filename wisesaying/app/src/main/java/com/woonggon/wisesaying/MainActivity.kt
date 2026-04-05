package com.woonggon.wisesaying

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.woonggon.wisesaying.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val sentenceList = mutableListOf<String>()
        sentenceList.add("삶이 있는 한 희망은 있다. - 키케로")
        sentenceList.add("산다는것 그것은 치열한 전투이다. - 로망로랑")
        sentenceList.add("언제나 현재에 집중할수 있다면 행복할것이다. - 파울로 코엘료")
        sentenceList.add("진정으로 웃으려면 고통을 참아야하며 , 나아가 고통과 함께 놀아야 한다. - 찰리 채플린")
        sentenceList.add("직업에서 행복을 찾아라. 아니면 행복이 무엇인지 절대 모를 것이다. - 엘버트 허버드")
        sentenceList.add("신은 용기있는자를 결코 버리지 않는다. - 켄러")
        sentenceList.add("피할수 없으면 즐겨라. - 로버트 엘리엇")
        sentenceList.add("먼저 자신을 비웃어라. 다른 사람에게 비웃음 당하기 전에. - 에프라임 헤르즈")
        sentenceList.add("행복한 삶을 살기위해 필요한 것은 거의 없다. - 마르쿠스 아우렐리우스 안토니우스")
        sentenceList.add("절대 어제를 후회하지 마라. 인생은 오늘의 내 안에 있고 내일은 스스로 만드는 것이다. - L.론허바드")



        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.showAllSentenceBtn.setOnClickListener {
            val intent = Intent(this, SentenceActivity::class.java)
            intent.putStringArrayListExtra("sentenceList", ArrayList(sentenceList))
            startActivity(intent)
        }

        binding.goodWordTextArea.setText(sentenceList.random())

    }


}