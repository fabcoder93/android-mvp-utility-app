package com.rajeshjadav.android.mvputilityapp.common

/*
Setup DelayAutoCompleteTextView with adapter
fun setupAutoCompleteView() {
    context?.let {
        val autoCompleteAdapter = AgentFinderAutoCompleteAdapter(
            it,
            R.layout.simple_auto_complete_list_item,
            presenter
        )
        searchAutoCompleteTextView.threshold = 1
        searchAutoCompleteTextView.setAdapter(autoCompleteAdapter)
    }
}*//*




//SPINNER
<array name="spinner_items">
<item>Item 1</item>
<item>Item 2</item>
<item>Item 3</item>
<item>Item 4</item>
<item>Item 5</item>
</array>

override fun showAgentCategorySpinner() {
    val spinnerAdapter = ArrayAdapter(
        context,
        R.layout.spinner,
        resources.getStringArray(R.array.spinner_items)
    )
    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    spinner.adapter = spinnerAdapter
}


=====TEXTWATCHER=====
usernameEditText.addTextChangedListener(this)
passwordEditText.addTextChangedListener(this)
override fun afterTextChanged(editable: Editable?) {
    when {
        editable === usernameEditText.editableText -> {
            presenter.onUsernameTextChanged(usernameEditText.enteredText)
        }
        editable === passwordEditText.editableText -> {
            presenter.onPasswordTextChanged(passwordEditText.enteredText)
        }
    }
}

override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
}

override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
}


=====TEXTINPUTLAYOUT=====
override fun showUsernameError() {
    usernameTextInputLayout.error = getString(R.string.error_enter_user_name)
    usernameEditText.requestFocus()
}


=====CAMERA PERMISSION HANDLING=====
override fun showCamera() {
    openCameraAppWithPermissionCheck()
}

@NeedsPermission(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
fun openCameraApp() {
    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    if (takePictureIntent.resolveActivity(activity?.packageManager) != null) {
        try {
            var photoFile: File?
            context?.let { context ->
                photoFile = createImageFile(context)
                photoFile?.let {
                    capturedCurrentPhotoPath = it.absolutePath
                    val photoURI = FileProvider.getUriForFile(
                        context,
                        getString(R.string.app_authorities),
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                }
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        startActivityForResult(
            takePictureIntent,
            REQUEST_CODE_IMAGE_CAPTURE
        )
    } else {
        showToast(getString(R.string.error_camera_app_not_found))
    }
}


@OnNeverAskAgain(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
fun onCameraPermissionDenied() {
    activity?.let {
        showAlertDialog(
            context = it,
            title = getString(R.string.dialog_title_permission_camera_storage),
            message = getString(R.string.error_message_permission_permanent_denial_camera_storage),
            positiveButtonText = getString(R.string.action_settings),
            negativeButtonText = getString(R.string.button_not_now),
            positiveButtonCallBack = DialogInterface.OnClickListener { _, _ ->
                activity?.openAppSettings()
            },
            negativeButtonCallBack = DialogInterface.OnClickListener { _, _ ->
            }
        )
    }
}

override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_IMAGE_CAPTURE) {
            val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            mediaScanIntent.data = Uri.fromFile(File(capturedCurrentPhotoPath))
            context?.sendBroadcast(mediaScanIntent)
            presenter.setAttachmentsCapturePicturesPath(capturedCurrentPhotoPath)
        }
        presenter.onActivityResult(requestCode, resultCode, data)
    }

requestCode == REQUEST_CODE_IMAGE_CAPTURE && resultCode == RESULT_OK -> {
                view?.copyNoteAttachmentToLocalFolder(
                    captureCurrentPhotoPath,
                    propertyDetails.addressText
                )
                    ?.let { addNoteAttachment(it) }
                loadAttachments()

                context?.copyFile(captureCurrentPhotoPath, newDirectoryName.replace(oldChar = '/', newChar = ' '))
            }
            requestCode == REQUEST_CODE_IMAGE_CAPTURE && resultCode == RESULT_CANCELED -> {
                File(captureCurrentPhotoPath).apply {
                    if (isFile) delete()
                }
            }

*/


/*
=====FILE PICKER HANDLING=====

Matisse.from(this)
.choose(MimeType.of(MimeType.PNG, MimeType.JPEG))
.showSingleMediaType(true)
.capture(false)
.maxSelectable(maxCount)
.countable(true)
.restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
.imageEngine(GlideModule())
.theme(theme)
.forResult(REQUEST_CODE_IMAGE_CHOOSE)

        GlideModule extend ImageEngine and implement below methods to get working.
override fun loadThumbnail(
    context: Context,
    resize: Int,
    placeholder: Drawable,
    imageView: ImageView,
    uri: Uri
) {
    GlideApp.with(context)
        .load(uri)
        .placeholder(placeholder)
        .override(resize, resize)
        .centerCrop()
        .into(imageView)
}

override fun loadImage(
    context: Context,
    resizeX: Int,
    resizeY: Int,
    imageView: ImageView,
    uri: Uri
) {
    GlideApp.with(context)
        .load(uri)
        .override(resizeX, resizeY)
        .priority(Priority.HIGH)
        .into(imageView)
}

override fun loadGifImage(
    context: Context,
    resizeX: Int,
    resizeY: Int,
    imageView: ImageView,
    uri: Uri?
) {
    GlideApp.with(context)
        .load(uri)
        .override(resizeX, resizeY)
        .priority(Priority.HIGH)
        .into(imageView)
}

override fun loadGifThumbnail(
    context: Context,
    resize: Int,
    placeholder: Drawable?,
    imageView: ImageView,
    uri: Uri?
) {
    GlideApp.with(context)
        .load(uri)
        .placeholder(placeholder)
        .override(resize, resize)
        .centerCrop()
        .into(imageView)
}

override fun supportAnimatedGif(): Boolean {
    return true
}


=====FCM=====
Make intentservice for starting activity on notification click
class PushNotificationIntentService : IntentService("PushNotificationIntentService") {

    override fun onHandleIntent(intent: Intent?) {
    Intent redirectIntent = Intent(this, HomeActivity::class.java)
            redirectIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            redirectIntent.putExtra(INT_EXTRA_NOTIFICATION, notification)
            startActivity(redirectIntent)

   }
   }

 private fun sendNotification(notification: Notification?) {
        pushNotificationMessageList.add(notification)
        notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val requestID = System.currentTimeMillis().toInt()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                getString(R.string.app_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = getString(R.string.app_name)
            channel.setShowBadge(true)
            notificationManager?.createNotificationChannel(channel)
        }

        val notificationIntent =
            Intent(this@AppFirebaseMessagingService, PushNotificationIntentService::class.java)
        notificationIntent.putExtra(INT_EXTRA_NOTIFICATION, notification)
        notificationIntent.putExtra(INT_EXTRA_NOTIFICATION_COUNT, pushNotificationCount)

        val pendingIntent = PendingIntent.getService(
            this@AppFirebaseMessagingService,
            requestID,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(notification?.notificationHeader)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSound(defaultSoundUri)
            .setNumber(pushNotificationCount)
            .setGroup(NOTIFICATION_GROUP_KEY)
            .setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.drawable.ic_launcher)
            notificationBuilder.color =
                ContextCompat.getColor(applicationContext, R.color.colorPrimary)
        } else {
            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher)
        }

        if (pushNotificationCount > 1) {
            notificationBuilder.setContentText(getContentText())
            val inboxStyle = NotificationCompat.InboxStyle()
            notificationBuilder.setContentTitle("$pushNotificationCount new notifications")
            inboxStyle.setBigContentTitle("$pushNotificationCount new notifications")
            pushNotificationMessageList.forEach {
                if (it?.notificationBody?.isEmpty() == true)
                    inboxStyle.addLine("${it.notificationHeader}")
                else
                    inboxStyle.addLine("${it?.notificationHeader}: ${it?.notificationBody}")
            }
            notificationBuilder.setStyle(inboxStyle)
        } else if (notification?.publicUserNotificationType == LISTING_ALERT_TYPE_SAVED_PROPERTY && pushNotificationCount == 1) {
            notificationBuilder.setContentText(notification.notificationBody)
        }

        notificationManager?.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

  */
