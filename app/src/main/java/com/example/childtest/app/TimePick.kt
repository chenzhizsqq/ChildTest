package com.example.childtest.app

//
//class TimePick : DialogFragment(), TimePickerDialog.OnTimeSetListener {
//
//
//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        //インスタンス
//        val c = Calendar.getInstance()
//        //最初に、システムの現在の時間と分を取得
//        val hour = c.get(Calendar.HOUR_OF_DAY)
//        val minute = c.get(Calendar.MINUTE)
//
//        return TimePickerDialog(
//            activity,
//            activity as ClockActivity?,//返したいアクティビティをセット
//            hour,
//            minute,
//            false)
//    }
//
//    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
//        //
//    }
//}