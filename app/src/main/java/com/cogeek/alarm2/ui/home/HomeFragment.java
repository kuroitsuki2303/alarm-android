package com.cogeek.alarm2.ui.home;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.cogeek.alarm2.AlarmBroadcastReceiver;
import com.cogeek.alarm2.MusicViewModel;
import com.cogeek.alarm2.R;

import java.io.File;
import java.text.DateFormat;
import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;
import static com.cogeek.alarm2.AlarmBroadcastReceiver.MUSIC_URI;

public class HomeFragment extends Fragment  {

    private final int WEEKLY = R.id.btn_weekly;
    private final int DAILY = R.id.btn_daily;
    private final int MONTHLY = R.id.btn_monthly;

    private final Calendar myCalendar = Calendar.getInstance();
    private final int REQUEST_CODE = 2121;
    private TextView txtDate;
    private TimePicker timePicker;
    private MusicViewModel viewModel;
    private TextView txtMusicSelected;
    private RadioGroup radioRepeatGroup;
    private Uri selected;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(MusicViewModel.class);
        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        timePicker = view.findViewById(R.id.timePicker);
        txtDate = view.findViewById(R.id.txtDate);
        radioRepeatGroup = view.findViewById(R.id.repeat_group);
        txtMusicSelected = view.findViewById(R.id.txt_music_selected);

        view.findViewById(R.id.layoutPickDate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        txtDate.setText(DateFormat.getDateInstance(DateFormat.MEDIUM).format(System.currentTimeMillis()));

        view.findViewById(R.id.btnGo).setOnClickListener(v -> {
            schedule();
        });

        view.findViewById(R.id.btn_cancel).setOnClickListener(v -> {
            cancel();
        });

        view.findViewById(R.id.btnChooseMusic).setOnClickListener(v -> {
            NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.action_FirstFragment_to_SecondFragment);
        });

        viewModel.getSelectedFile().observe(getActivity(), file -> {
            txtMusicSelected.setText(file.getName());
            selected = Uri.parse(file.toString());
        });
    }



    private void updateLabel() {
        txtDate.setText(DateFormat.getDateInstance(DateFormat.MEDIUM).format(myCalendar.getTime()));
    }

    private void schedule() {
        int hour = getTimePickerHour(timePicker);
        int minute = getTimePickerMinute(timePicker);

        myCalendar.set(Calendar.HOUR_OF_DAY, hour);
        myCalendar.set(Calendar.MINUTE,minute);
        myCalendar.set(Calendar.SECOND,0);
        myCalendar.set(Calendar.MILLISECOND,0);

        Intent intent = new Intent(getActivity(), AlarmBroadcastReceiver.class);
        if (selected != null) {
            intent.putExtra(MUSIC_URI, selected.toString());
        }
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), REQUEST_CODE, intent, 0);
        AlarmManager am = (AlarmManager)getActivity().getSystemService(ALARM_SERVICE);

        switch (radioRepeatGroup.getCheckedRadioButtonId()) {
            case DAILY:
                am.setRepeating(am.RTC_WAKEUP, myCalendar.getTimeInMillis(), 24*60*60*1000, pendingIntent);
                break;
            case WEEKLY:
                am.setRepeating(am.RTC_WAKEUP, myCalendar.getTimeInMillis(), 24*60*60*1000*7, pendingIntent);
                break;
            case MONTHLY:
                am.setRepeating(am.RTC_WAKEUP, myCalendar.getTimeInMillis(), 24*60*60*1000*30, pendingIntent);
                break;
            default:
                am.setExact(am.RTC_WAKEUP,myCalendar.getTimeInMillis(),pendingIntent);
                break;
        }

//        am.setRepeating(am.RTC_WAKEUP, myCalendar.getTimeInMillis(), 24*60*60*1000, pendingIntent);
//        am.setInexactRepeating(am.RTC_WAKEUP, myCalendar.getTimeInMillis(), 24*60*60*1000, pendingIntent);
//        am.setExact(am.RTC_WAKEUP,myCalendar.getTimeInMillis(),pendingIntent);
        String toastText =  "Tạo báo thức thành công";
        Toast.makeText(getActivity(), toastText, Toast.LENGTH_LONG).show();
    }

    private void cancel() {
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), AlarmBroadcastReceiver.class);
        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(getActivity(), REQUEST_CODE, intent, 0);
        alarmManager.cancel(alarmPendingIntent);
        Toast.makeText(getActivity(), "Đã hủy", Toast.LENGTH_SHORT).show();
    }


    public int getTimePickerHour(TimePicker tp) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return tp.getHour();
        } else {
            return tp.getCurrentHour();
        }
    }

    public int getTimePickerMinute(TimePicker tp) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return tp.getMinute();
        } else {
            return tp.getCurrentMinute();
        }
    }
}