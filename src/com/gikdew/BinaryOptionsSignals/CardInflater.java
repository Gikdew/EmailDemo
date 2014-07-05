package com.gikdew.BinaryOptionsSignals;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gikdew.BinaryOptionsSignals.BaseInflaterAdapter;
import com.gikdew.BinaryOptionsSignals.IAdapterViewInflater;
import com.gikdew.BinaryOptionsSignals.ItemData;
import com.gikdew.emaildemo.R;

public class CardInflater implements IAdapterViewInflater<ItemData>
{
	@Override
	public View inflate(final BaseInflaterAdapter<ItemData> adapter, final int pos, View convertView, ViewGroup parent)
	{
		ViewHolder holder;

		if (convertView == null)
		{
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			convertView = inflater.inflate(R.layout.list_item, parent, false);
			holder = new ViewHolder(convertView);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}

		final ItemData item = adapter.getTItem(pos);
		holder.updateDisplay(item);

		return convertView;
	}

	private class ViewHolder{
		private View m_rootView;
		private TextView m_text1;
		private TextView m_text2;
		private ImageView m_result;
		private TextView m_date;

		public ViewHolder(View rootView){
			m_rootView = rootView;
			m_text1 = (TextView) m_rootView.findViewById(R.id.text1);
			m_text2 = (TextView) m_rootView.findViewById(R.id.text2);
			m_result = (ImageView) m_rootView.findViewById(R.id.text3);
			m_date = (TextView) m_rootView.findViewById(R.id.text4);
			rootView.setTag(this);
		}

		public void updateDisplay(ItemData item){
			m_text1.setText(item.getPair());
			m_text2.setText(item.getExpiry());
			//m_result.setText(item.getResult());
			m_date.setText(item.getCreatedat());
			//Log.d("Up", item.getResult());
			if(item.getResult().equals("UP")){
				m_result.setImageResource(R.drawable.up);
				//m_result.setBackgroundResource(R.drawable.up);
				//m_result.setTextColor(Color.parseColor("#2ecc71"));
			}else{
				m_result.setImageResource(R.drawable.down);
				//m_result.setBackgroundResource(R.drawable.down);
				//m_result.setTextColor(Color.parseColor("#e74c3c"));
			}
		}
	}
}
