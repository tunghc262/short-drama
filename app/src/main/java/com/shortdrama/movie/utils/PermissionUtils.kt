package com.shortdrama.movie.utils

import android.Manifest
import android.app.Activity
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import com.vmadalin.easypermissions.EasyPermissions


internal const val REQUEST_READ_IMAGE = 1
internal const val REQUEST_READ_VIDEO = 2
internal const val REQUEST_READ_AUDIO = 3
internal const val REQUEST_CODE_POST_NOTIFICATION = 4
internal const val REQUEST_CODE_RECORD_AUDIO = 5
internal const val REQUEST_CODE_CAMERA = 6

object PermissionUtils {

    fun hasDrawOverlays(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(context)
        } else {
            true
        }
    }

    fun hasUsageAccess(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                val applicationInfo =
                    context.packageManager.getApplicationInfo(context.packageName, 0)
                val appOpsManager: AppOpsManager =
                    context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
                val mode: Int = appOpsManager.checkOpNoThrow(
                    AppOpsManager.OPSTR_GET_USAGE_STATS,
                    applicationInfo.uid,
                    applicationInfo.packageName
                )
                return (mode == AppOpsManager.MODE_ALLOWED)

            } catch (e: Exception) {
                e.printStackTrace()
                return false
            }
        } else {
            true
        }
    }

    fun requestUsageAccess(context: Context) {
        if (!hasUsageAccess(context)) {
            var intent: Intent
            try {
                intent = Intent(
                    Settings.ACTION_USAGE_ACCESS_SETTINGS,
                    Uri.parse("package:${context.packageName}")
                )
                context.startActivity(intent)
            } catch (e: Exception) {
                intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                context.startActivity(intent)
                e.printStackTrace()
            }
        }
    }

    fun requestDrawOverlays(context: Context) {
        if (!hasDrawOverlays(context)) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val intent = Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:${context.packageName}")
                    )
                    context.startActivity(intent)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun hasReadAudio(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                EasyPermissions.hasPermissions(
                    context,
                    perms = arrayOf(
                        Manifest.permission.READ_MEDIA_AUDIO
                    )
                )
            } else {
                true
            }
        } else {
            true
        }
    }

    fun requestReadAudio(activity: Activity, content: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            EasyPermissions.requestPermissions(
                activity,
                content,
                REQUEST_READ_AUDIO,
                Manifest.permission.READ_MEDIA_AUDIO
            )
        } else {
            EasyPermissions.requestPermissions(
                activity,
                content,
                REQUEST_READ_AUDIO,
                perms = arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            )
        }
    }

    fun hasReadImage(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                EasyPermissions.hasPermissions(
                    context,
                    perms = arrayOf(
                        Manifest.permission.READ_MEDIA_IMAGES
                    )
                )
            } else {
                true
            }
        } else {
            true
        }
    }

    fun requestReadImage(activity: Activity, content: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            EasyPermissions.requestPermissions(
                activity,
                content,
                REQUEST_READ_IMAGE,
                Manifest.permission.READ_MEDIA_IMAGES
            )
        } else {
            EasyPermissions.requestPermissions(
                activity,
                content,
                REQUEST_READ_IMAGE,
                perms = arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            )
        }
    }

    fun hasReadVideo(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                EasyPermissions.hasPermissions(
                    context,
                    perms = arrayOf(
                        Manifest.permission.READ_MEDIA_VIDEO
                    )
                )
            } else {
                true
            }
        } else {
            true
        }
    }

    fun requestReadVideo(activity: Activity, content: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            EasyPermissions.requestPermissions(
                activity,
                content,
                REQUEST_READ_VIDEO,
                Manifest.permission.READ_MEDIA_VIDEO
            )
        } else {
            EasyPermissions.requestPermissions(
                activity,
                content,
                REQUEST_READ_IMAGE,
                perms = arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            )
        }
    }

    fun hasPostNotification(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                EasyPermissions.hasPermissions(
                    context,
                    perms = arrayOf(
                        Manifest.permission.POST_NOTIFICATIONS
                    )
                )
            } else {
                true
            }
        } else {
            true
        }
    }

    fun requestPostNotification(activity: Activity, content: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            EasyPermissions.requestPermissions(
                activity,
                content,
                REQUEST_CODE_POST_NOTIFICATION,
                Manifest.permission.POST_NOTIFICATIONS
            )
        }
    }

    fun hasRecordAudio(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            EasyPermissions.hasPermissions(
                context,
                perms = arrayOf(Manifest.permission.RECORD_AUDIO)
            )
        } else {
            true
        }
    }

    fun requestRecordAudio(activity: Activity, content: String) {
        EasyPermissions.requestPermissions(
            activity,
            content,
            REQUEST_CODE_RECORD_AUDIO,
            Manifest.permission.RECORD_AUDIO
        )
    }

    fun hasRecordCamera(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            EasyPermissions.hasPermissions(
                context,
                perms = arrayOf(Manifest.permission.CAMERA)
            )
        } else {
            true
        }
    }

    fun requestRecordCamera(activity: Activity, content: String) {
        EasyPermissions.requestPermissions(
            activity,
            content,
            REQUEST_CODE_CAMERA,
            Manifest.permission.CAMERA
        )
    }
}