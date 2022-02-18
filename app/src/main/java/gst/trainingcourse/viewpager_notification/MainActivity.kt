package gst.trainingcourse.viewpager_notification

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import gst.trainingcourse.viewpager2_bnv.transformer.DepthPageTransformer
import gst.trainingcourse.viewpager2_bnv.transformer.ZoomOutTransformer
import gst.trainingcourse.viewpager_notification.adapter.MyViewPagerAdapter
import gst.trainingcourse.viewpager_notification.fragments.DashBoardFragment
import gst.trainingcourse.viewpager_notification.fragments.HomeFragment
import gst.trainingcourse.viewpager_notification.fragments.NotificationFragment


class MainActivity : AppCompatActivity() {
    private lateinit var vp2 : ViewPager2
    private lateinit var choose : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        vp2=findViewById(R.id.vp2)
        val bnv : BottomNavigationView = findViewById(R.id.bnv)
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener(
            this
        ) { instanceIdResult: InstanceIdResult ->
            val newToken = instanceIdResult.token
            Log.e("newToken", newToken)
            val screenId = intent.getStringExtra("screenId")?.toInt() ?: 0
            Log.d("TAG", "onCreate: $screenId ")
            vp2.currentItem = screenId
        }



        val fragments :ArrayList<Fragment> = arrayListOf(
            HomeFragment(),
            DashBoardFragment(),
            NotificationFragment()
        )

        val adapter = MyViewPagerAdapter(fragments,this)
        vp2.adapter =adapter
        vp2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when(position){
                    0-> bnv.menu.findItem(R.id.home).isChecked = true
                    1-> bnv.menu.findItem(R.id.dashboard).isChecked = true
                    2-> bnv.menu.findItem(R.id.notifications).isChecked = true
                }
            }
        })

        choose =findViewById(R.id.choose)

        choose =findViewById(R.id.choose)
        choose.setOnClickListener{
            showPopup(choose)
        }
        bnv.setOnItemSelectedListener(mOnNaviagation)
    }

    private fun showPopup(view: View) {
        val popupMenu = PopupMenu(this,view)
        popupMenu.inflate(R.menu.menu_main)
        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem?->
            when(item!!.itemId){
                R.id.zoom ->{
                    vp2.setPageTransformer(ZoomOutTransformer())
                }
                R.id.depth-> {
                    vp2.setPageTransformer(DepthPageTransformer())
                }
            }
            true
        })
        popupMenu.show()
    }

    private val mOnNaviagation =BottomNavigationView.OnNavigationItemSelectedListener {item->
        when(item.itemId){
            R.id.home ->{
                vp2.currentItem =0
                return@OnNavigationItemSelectedListener true
            }
            R.id.dashboard ->{
                vp2.currentItem =1
                return@OnNavigationItemSelectedListener true
            }
            R.id.notifications ->{
                vp2.currentItem =2
                return@OnNavigationItemSelectedListener true
            }
            else ->{
                vp2.currentItem =0
                return@OnNavigationItemSelectedListener true
            }
        }
    }



    override fun onBackPressed() {
        if(vp2.currentItem == 0){
            super.onBackPressed()
        }else{
            vp2.currentItem = vp2.currentItem -1
        }

    }
}