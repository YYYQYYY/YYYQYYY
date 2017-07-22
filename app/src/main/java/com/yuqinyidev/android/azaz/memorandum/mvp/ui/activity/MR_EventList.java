package com.yuqinyidev.android.azaz.memorandum.mvp.ui.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.yuqinyidev.android.azaz.R;
import com.yuqinyidev.android.azaz.memorandum.mvp.model.MR_DBAdapter;
import com.yuqinyidev.android.azaz.memorandum.mvp.model.entity.MR_memory;
import com.yuqinyidev.android.azaz.memorandum.mvp.model.service.MR_MemorandumReceiver;

public class MR_EventList extends Activity {

	public static final String EVENT_DATE_TIME = "event_date_time";

	private static final int DIALOG_CREATE_EVENT = 1;
	private static final int DIALOG_UPDATE_EVENT = 2;
	private static final String BUNDLE_KEY_MEMORY = "memorandum_memory";
	private static final String BUNDLE_KEY_DATE_TIME = "memorandum_date_time";

	private MR_DBAdapter db;

	private ListView listEvent;
	private TextView eventListTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mr_eventlist);

		db = new MR_DBAdapter();
		db.open(getResources().openRawResource(R.raw.memorandum));

		String eventDateTime = getIntent().getStringExtra(EVENT_DATE_TIME);
		setContentView(eventDateTime);
	}

	private void setContentView(final String _eventDateTime) {

		Button eventListCreate = (Button) findViewById(R.id.btnEListCreate);
		eventListTitle = (TextView) findViewById(R.id.txvEListTitle);
		Button eventListPreviou = (Button) findViewById(R.id.btnEListPreviou);
		Button eventListNext = (Button) findViewById(R.id.btnEListNext);

		String sysDt = new SimpleDateFormat("yyyyMMdd").format(Calendar
				.getInstance().getTime());

		// システム日付より前の場合、イベント新規できない。
		if (0 >= sysDt.compareTo(_eventDateTime)) {
			eventListCreate.setEnabled(true);
		} else {
			eventListCreate.setEnabled(false);
		}

		try {
			eventListTitle.setText(new SimpleDateFormat("EEE MMM dd yyyy")
					.format(new SimpleDateFormat("yyyyMMdd").parse(
							_eventDateTime).getTime()));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		listEvent = (ListView) findViewById(R.id.lsvEListEvent);
		listEvent.setAdapter(new EventListViewAdapter(_eventDateTime));

		eventListPreviou.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				MR_memory event = new MR_memory();
				event.memo_date = _eventDateTime;
				event.event_query_flg = 2;
				String PreviouDateTime = db.getEventDate(event);
				if (PreviouDateTime != null) {
					setContentView(PreviouDateTime);
				}
			}
		});

		eventListCreate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putString(BUNDLE_KEY_DATE_TIME, _eventDateTime);
				showDialog(DIALOG_CREATE_EVENT, bundle);
			}
		});

		eventListNext.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				MR_memory event = new MR_memory();
				event.memo_date = _eventDateTime;
				event.event_query_flg = 1;
				String nextDateTime = db.getEventDate(event);
				if (nextDateTime != null) {
					setContentView(nextDateTime);
				}
			}
		});
	}

	@Override
	protected Dialog onCreateDialog(int id, Bundle bundle) {
		switch (id) {
		case DIALOG_CREATE_EVENT:
			return showCreateEventDialog((String) bundle
					.getString(BUNDLE_KEY_DATE_TIME));
		case DIALOG_UPDATE_EVENT:
			return showUpdateEventDialog((MR_memory) bundle
					.getSerializable(BUNDLE_KEY_MEMORY));
		}
		return null;
	}

	private AlertDialog showCreateEventDialog(final String _dateTime) {

		LayoutInflater inflater = getLayoutInflater();
		final View layout = inflater.inflate(R.layout.mr_newevent, null);

		final AlertDialog createDialog = new AlertDialog(MR_EventList.this) {
			protected void onCreate(Bundle savedInstanceState) {
				setView(layout, 0, 0, 0, 0);
				super.onCreate(savedInstanceState);
			}
		};

		final Calendar cal;
		cal = Calendar.getInstance();

		final MR_memory event = new MR_memory();

		final TextView newEventTitle = (TextView) layout
				.findViewById(R.id.txvEventTitle);
		final EditText eventTitle = (EditText) layout
				.findViewById(R.id.edtEventTitle);
		TextView txvEventDate = (TextView) layout
				.findViewById(R.id.txvEventDate);
		try {
			txvEventDate.setText(new SimpleDateFormat("yyyy-MM-dd(EEE)")
					.format(new SimpleDateFormat("yyyyMMdd").parse(_dateTime)
							.getTime()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		final Button eventTime = (Button) layout
				.findViewById(R.id.btnEventTime);
		final EditText eventWhere = (EditText) layout
				.findViewById(R.id.edtEventWhere);
		final CheckBox eventImportant = (CheckBox) layout
				.findViewById(R.id.chbEventImportant);
		final CheckBox eventCompleted = (CheckBox) layout
				.findViewById(R.id.chbEventCompleted);
		eventCompleted.setEnabled(false);
		final EditText eventMemoContent = (EditText) layout
				.findViewById(R.id.edtEventMemoContent);
		Button eventCreate = (Button) layout.findViewById(R.id.btnEventCreate);

		try {
			newEventTitle.setText(new SimpleDateFormat("EEE MMM dd yyyy")
					.format(new SimpleDateFormat("yyyyMMdd").parse(_dateTime)
							.getTime()));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		event.memo_date = _dateTime;

		eventTime.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				new TimePickerDialog(MR_EventList.this,
						new TimePickerDialog.OnTimeSetListener() {
							public void onTimeSet(TimePicker view,
									int hourOfDay, int minute) {
								String hour = null;
								String mimu = null;
								if (hourOfDay < 10) {
									hour = '0' + String.valueOf(hourOfDay);
								} else {
									hour = String.valueOf(hourOfDay);
								}
								if (minute < 10) {
									mimu = '0' + String.valueOf(minute);
								} else {
									mimu = String.valueOf(minute);
								}

								eventTime.setText(hour + ":" + mimu);
								event.memo_time = hour + mimu + "00";
							}
						}, cal.get(Calendar.HOUR_OF_DAY), cal
								.get(Calendar.MINUTE), true).show();
			}
		});

		eventCreate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				event.memo_type = 1;
				event.memo_title = eventTitle.getText().toString();
				event.event_where = eventWhere.getText().toString();
				if (eventImportant.isChecked()) {
					event.important_flg = 1;
				}
				event.memo_content = eventMemoContent.getText().toString();
				long eventInsertResult = db.insert(event);
				if (eventInsertResult != -1) {
					MR_memory[] newEventPcd = db.queryMemoryData(event);
					MR_memory newEventPcdFist = newEventPcd[newEventPcd.length - 1];

					Intent intent = new Intent(MR_EventList.this,
							MR_MemorandumReceiver.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("key_send_event", newEventPcdFist);
					intent.putExtras(bundle);
					MR_EventList.this.sendBroadcast(intent);
				}

				listEvent.setAdapter(new EventListViewAdapter(event.memo_date));
				createDialog.cancel();
			}
		});
		createDialog.show();

		return null;
	}

	private AlertDialog showUpdateEventDialog(final MR_memory _event) {
		LayoutInflater inflater = getLayoutInflater();
		final View layout = inflater.inflate(R.layout.mr_updateevent, null);

		final AlertDialog updateDialog = new AlertDialog(MR_EventList.this) {
			protected void onCreate(Bundle savedInstanceState) {
				setView(layout, 0, 0, 0, 0);
				super.onCreate(savedInstanceState);
			}
		};

		final Calendar cal = Calendar.getInstance();

		final MR_memory event = new MR_memory();

		Button eventUpdate = (Button) layout.findViewById(R.id.btnEventUpdate);
		Button eventCancle = (Button) layout.findViewById(R.id.btnEventCancle);
		final TextView updateEventTitle = (TextView) layout
				.findViewById(R.id.txvEventTitle);
		final EditText eventTitle = (EditText) layout
				.findViewById(R.id.edtEventTitle);
		TextView txvEventDate = (TextView) layout
				.findViewById(R.id.txvEventDate);
		final Button eventTime = (Button) layout
				.findViewById(R.id.btnEventTime);
		final EditText eventWhere = (EditText) layout
				.findViewById(R.id.edtEventWhere);
		final CheckBox eventImportant = (CheckBox) layout
				.findViewById(R.id.chbEventImportant);
		final CheckBox eventCompleted = (CheckBox) layout
				.findViewById(R.id.chbEventCompleted);
		final EditText eventMemoContent = (EditText) layout
				.findViewById(R.id.edtEventMemoContent);

		if (_event.memo_title != null) {
			eventTitle.setText(_event.memo_title);
		}
		if (_event.memo_date != null) {
			try {
				updateEventTitle
						.setText(new SimpleDateFormat("EEE MMM dd yyyy")
								.format(new SimpleDateFormat("yyyyMMdd").parse(
										_event.memo_date).getTime()));
				txvEventDate.setText(new SimpleDateFormat("yyyy-MM-dd(EEE)")
						.format(new SimpleDateFormat("yyyyMMdd").parse(
								_event.memo_date).getTime()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			event.memo_date = _event.memo_date;
		}
		if (_event.memo_time != null) {
			eventTime.setText(_event.memo_time.substring(0, 2) + ":"
					+ _event.memo_time.substring(2, 4));
			event.memo_time = _event.memo_time;
		}
		if (_event.event_where != null) {
			eventWhere.setText(_event.event_where);
		}
		if (1 == _event.important_flg) {
			eventImportant.setChecked(true);
		}
		if (1 == _event.complete_flg) {
			eventCompleted.setChecked(true);
		}
		if (_event.memo_content != null) {
			eventMemoContent.setText(_event.memo_content);
		}

		eventTime.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				new TimePickerDialog(MR_EventList.this,
						new TimePickerDialog.OnTimeSetListener() {
							public void onTimeSet(TimePicker view,
									int hourOfDay, int minute) {
								String hour = null;
								String mimu = null;
								if (hourOfDay < 10) {
									hour = '0' + String.valueOf(hourOfDay);
								} else {
									hour = String.valueOf(hourOfDay);
								}
								if (minute < 10) {
									mimu = '0' + String.valueOf(minute);
								} else {
									mimu = String.valueOf(minute);
								}
								eventTime.setText(hour + ":" + mimu);
								event.memo_time = hour + mimu + "00";
							}
						}, cal.get(Calendar.HOUR_OF_DAY), cal
								.get(Calendar.MINUTE), true).show();
			}
		});

		eventUpdate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				event.pcd = _event.pcd;
				event.memo_type = 1;
				event.memo_title = eventTitle.getText().toString();
				event.event_where = eventWhere.getText().toString();
				if (eventImportant.isChecked()) {
					event.important_flg = 1;
				}
				if (eventCompleted.isChecked()) {
					event.complete_flg = 1;
				}
				event.memo_content = eventMemoContent.getText().toString();
				long eventUpdateResult = db.update(event);

				if (eventUpdateResult != -1) {
					Intent intent = new Intent(MR_EventList.this,
							MR_MemorandumReceiver.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("key_send_event", event);
					intent.putExtras(bundle);
					MR_EventList.this.sendBroadcast(intent);
				}

				try {
					eventListTitle.setText(new SimpleDateFormat(
							"EEE MMM dd yyyy").format(new SimpleDateFormat(
							"yyyyMMdd").parse(event.memo_date).getTime()));
				} catch (ParseException e) {
					e.printStackTrace();
				}

				listEvent.setAdapter(new EventListViewAdapter(event.memo_date));
				updateDialog.cancel();
			}
		});

		eventCancle.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				updateDialog.cancel();
			}
		});
		updateDialog.show();

		return null;
	}

	private void showDeleteDialog(final int _deleteAllFlag, final MR_memory _memo) {
		new AlertDialog.Builder(MR_EventList.this)
				.setTitle(R.string.deleteConfirn).setPositiveButton(
						R.string.yes, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

								if (_deleteAllFlag == 1) {
									db.delete(_memo);
								} else {
									db.deleteMemoryOneData(_memo);
								}
							}
						}).setNegativeButton(R.string.no,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {

							}
						}).show();

	}

	private class EventListViewAdapter extends BaseAdapter {
		View[] itemViews;
		boolean isNotNull = false;

		public EventListViewAdapter(String _dateTime) {
			MR_memory event = new MR_memory();
			event.memo_type = 1;
			event.memo_date = _dateTime;
			MR_memory[] events = db.queryMemoryData(event);

			if (events != null) {
				isNotNull = true;
				itemViews = new View[events.length];

				for (int i = 0; i < itemViews.length; i++) {
					itemViews[i] = makeItemView(events[i], i);
				}
			}
		}

		public int getCount() {
			return isNotNull ? itemViews.length : 0;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		private View makeItemView(final MR_memory _event, int _i) {
			LayoutInflater inflater = (LayoutInflater) MR_EventList.this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			// �g�pView�I?��itemView�^R.layout.item??
			final View itemView = inflater.inflate(R.layout.mr_item, null);

			// ��?findViewById()��@?��R.layout.item���e?��
			ImageView image = (ImageView) itemView.findViewById(R.id.itemImage);
			TextView title = (TextView) itemView.findViewById(R.id.itemTitle);

			if (_i % 2 == 1) {
				itemView.setBackgroundColor(getResources().getColor(R.color.lightblue));
			}
			try {
				title.setText(new SimpleDateFormat("HH:mm aa")
						.format(new SimpleDateFormat("hhmmss").parse(
								_event.memo_time).getTime())
						+ '\n' + _event.memo_title);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			title.setTextColor(Color.BLUE);

			if (_event.complete_flg == 1) {
				image.setImageResource(R.drawable.memo_btn_disabled);
				title.setTextColor(Color.GRAY);
			}
			if (_event.important_flg == 1) {
				title.setTextColor(Color.RED);
				itemView.setBackgroundColor(getResources().getColor(R.color.lightpink));
			}
			image.setOnClickListener(new OnClickListener() {
				public void onClick(View view) {
					if (_event.complete_flg == 0) {

						showAlertDialog(_event, itemView);
					}
				}
			});
			title.setOnClickListener(new OnClickListener() {
				public void onClick(View view) {
					if (_event.complete_flg == 0) {
						Bundle bundle = new Bundle();
						bundle.putSerializable(BUNDLE_KEY_MEMORY, _event);
						showDialog(DIALOG_UPDATE_EVENT, bundle);
					}
				}
			});

			title.setOnLongClickListener(new OnLongClickListener() {

				public boolean onLongClick(View v) {
					final String[] items = { getString(R.string.Delete),
							getString(R.string.deleteAll) };
					new AlertDialog.Builder(MR_EventList.this).setTitle(
							R.string.select_dialog).setItems(items,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									showDeleteDialog(which, _event);
								}
							}).create().show();
					return true;
				}
			});
			return itemView;
		}

		private void showAlertDialog(final MR_memory _event, final View _itemView) {
			new AlertDialog.Builder(MR_EventList.this).setTitle(
					R.string.memoAlarttitle).setMessage(
					R.string.memoAlartContent).setPositiveButton(R.string.yes,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {

							ImageView image = (ImageView) _itemView
									.findViewById(R.id.itemImage);
							image
									.setImageResource(R.drawable.memo_btn_disabled);
							TextView title = (TextView) _itemView
									.findViewById(R.id.itemTitle);
							title.setTextColor(Color.GRAY);

							MR_memory event = new MR_memory();
							event.complete_flg = 1;
							event.pcd = _event.pcd;
							db.update(event);

							listEvent.setAdapter(new EventListViewAdapter(
									_event.memo_date));
						}
					}).setNegativeButton(R.string.no,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {

						}
					}).show();
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			if (convertView == null) {
				if (isNotNull) {
					view = itemViews[position];
				} else {
					LayoutInflater inflater = (LayoutInflater) MR_EventList.this
							.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					view = inflater.inflate(R.layout.mr_item, null);
				}
			} else {
				view = convertView;
			}
			return view;
		}
	}
}