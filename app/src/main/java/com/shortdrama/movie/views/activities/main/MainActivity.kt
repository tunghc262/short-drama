package com.shortdrama.movie.views.activities.main

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.module.ads.remote.FirebaseQuery
import com.module.ads.views.UpdateAppDialog
import com.shortdrama.movie.R
import com.shortdrama.movie.app.AppConstants
import com.shortdrama.movie.app.GlobalApp
import com.shortdrama.movie.databinding.ActivityMainBinding
import com.shortdrama.movie.notification.DailyWorkReceiver
import com.shortdrama.movie.utils.AppUtils
import com.shortdrama.movie.utils.PermissionUtils
import com.shortdrama.movie.views.activities.main.fragments.for_you.ForYouFragment
import com.shortdrama.movie.views.activities.main.fragments.home.HomeFragment
import com.shortdrama.movie.views.activities.main.fragments.my_list.MyListFragment
import com.shortdrama.movie.views.activities.main.fragments.profile.ProfileFragment
import com.shortdrama.movie.views.bases.BaseActivity
import com.shortdrama.movie.views.bases.ext.goneView
import com.shortdrama.movie.views.bases.ext.visibleView
import com.shortdrama.movie.views.dialogs.ExitAppDialog
import com.shortdrama.movie.views.dialogs.NotificationFullscreenPermissionDialog
import com.shortdrama.movie.views.dialogs.NotificationPermissionDialog
import java.util.Calendar
import kotlin.system.exitProcess

class MainActivity : BaseActivity<ActivityMainBinding>() {
    private lateinit var alarmManager: AlarmManager

    private var isShowLastMovie: Boolean = false
    private var mainPagerAdapter: MainViewPager? = null
    private var ivNavCurrent: ImageView? = null
    private var imgNavCurrent: Int = R.drawable.ic_home
    private var tvNavCurrent: TextView? = null
    override fun getLayoutActivity(): Int {
        return R.layout.activity_main
    }

    override fun initViews() {
        super.initViews()
        UpdateAppDialog(this).showDialog()
        initPermission()
        ivNavCurrent = mBinding.ivMainHome
        tvNavCurrent = mBinding.tvMainHome
        setUpViewPager()
    }

    override fun onClickViews() {
        super.onClickViews()
    }

    override fun observerData() {
        super.observerData()
        mBinding.llNavHome.setOnClickListener {
            if (mBinding.vpMain.currentItem == 0) return@setOnClickListener
            if (isShowLastMovie) mBinding.clMovieLast.visibleView()
            setOnClickBottomNav(
                mBinding.ivMainHome,
                R.drawable.ic_home,
                R.drawable.ic_home_selected,
                mBinding.tvMainHome
            )
            mBinding.vpMain.setCurrentItem(0, false)
        }
        mBinding.llNavForYou.setOnClickListener {
            if (mBinding.vpMain.currentItem == 1) return@setOnClickListener
            mBinding.clMovieLast.goneView()
            setOnClickBottomNav(
                mBinding.ivMainForYou,
                R.drawable.ic_foryou,
                R.drawable.ic_foryou_selected,
                mBinding.tvMainForYou
            )
            mBinding.vpMain.setCurrentItem(1, false)
        }
        mBinding.llNavMyList.setOnClickListener {
            if (mBinding.vpMain.currentItem == 2) return@setOnClickListener
            if (isShowLastMovie) mBinding.clMovieLast.visibleView()
            setOnClickBottomNav(
                mBinding.ivMainMyList,
                R.drawable.ic_my_list,
                R.drawable.ic_my_list_selected,
                mBinding.tvMainMyList
            )
            mBinding.vpMain.setCurrentItem(2, false)
        }
        mBinding.llNavProfile.setOnClickListener {
            if (mBinding.vpMain.currentItem == 3) return@setOnClickListener
            mBinding.clMovieLast.goneView()
            setOnClickBottomNav(
                mBinding.ivMainProfile,
                R.drawable.ic_profile,
                R.drawable.ic_profile_selected,
                mBinding.tvMainProfile
            )
            mBinding.vpMain.setCurrentItem(3, false)
        }
        mBinding.clMovieLast.setOnClickListener {

        }
    }


    override fun onResume() {
        super.onResume()
    }

    private fun setUpViewPager() {
        mainPagerAdapter = MainViewPager(this@MainActivity)
        mainPagerAdapter?.submitFragments(
            listOf(
                HomeFragment(),
                ForYouFragment(),
                MyListFragment(),
                ProfileFragment()
            )
        )
        mBinding.vpMain.apply {
            adapter = mainPagerAdapter
            isUserInputEnabled = false
            offscreenPageLimit = 3
        }
    }

    private fun setOnClickBottomNav(
        ivNav: ImageView,
        imgNav: Int,
        imgNavSelected: Int,
        tvNav: TextView
    ) {
        ivNav.setImageResource(imgNavSelected)
        tvNav.setTextColor(ContextCompat.getColor(this, R.color.white))
        ivNavCurrent?.setImageResource(imgNavCurrent)
        tvNavCurrent?.setTextColor(ContextCompat.getColor(this, R.color.color_65636B))
        ivNavCurrent = ivNav
        imgNavCurrent = imgNav
        tvNavCurrent = tvNav
    }

    private fun initPermission() {
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        if (PermissionUtils.hasPostNotification(this)) {
            checkNotificationLock()
        } else {
            if (AppUtils.isSession2() && !GlobalApp.isShowDialogNotification) {
                NotificationPermissionDialog(this).show()
                GlobalApp.isShowDialogNotification = true
            }
        }
    }

    private fun checkNotificationLock() {
        if (FirebaseQuery.getEnableNotificationLockScr()) {
            val nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val areEnabled: Boolean
            if (PermissionUtils.hasPostNotification(this@MainActivity)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    areEnabled = nm.canUseFullScreenIntent()
                    if (areEnabled) {
                        scheduleDailyWorkLock()
                    } else {
                        if (!GlobalApp.isShowDialogFullIntent) {
                            NotificationFullscreenPermissionDialog(this@MainActivity).show()
                            GlobalApp.isShowDialogFullIntent = true
                        }
                    }
                } else {
                    scheduleDailyWorkLock()
                }
            }
        } else {
            cancelDailyWork()
        }
    }

    private fun cancelDailyWork() {
        val intent = Intent(this@MainActivity, DailyWorkReceiver::class.java)
        intent.putExtra(
            AppConstants.ALARM_REQUEST_CODE,
            AppConstants.REQUEST_CODE_NOTIFICATION_LOCK
        )
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            AppConstants.REQUEST_CODE_NOTIFICATION_LOCK,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
        }
    }

    private fun scheduleDailyWorkLock() {
        val intent = Intent(this@MainActivity, DailyWorkReceiver::class.java)
        intent.putExtra(
            AppConstants.ALARM_REQUEST_CODE,
            AppConstants.REQUEST_CODE_NOTIFICATION_LOCK
        )
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            AppConstants.REQUEST_CODE_NOTIFICATION_LOCK,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 9)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 0)
            if (before(Calendar.getInstance())) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }
        alarmManager.setAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )

    }

    override fun onBackPressed() {
        val dialogExit = ExitAppDialog(this, onClickExit = {
            finish()
            exitProcess(0)
        })
        dialogExit.show()
    }
}