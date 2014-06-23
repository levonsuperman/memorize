package ua.levon.memorize;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MemoryItemAdapter extends BaseAdapter {
	
	Context ctx;
	LayoutInflater lInflater;
	ArrayList<MemoryItem> objects;

	MemoryItemAdapter (Context context, ArrayList<MemoryItem> memories) {
		
		ctx=context;
		objects=memories;
		lInflater=(LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
	}

	@Override
	public int getCount() {
		
		return objects.size();
		
	}

	@Override
	public Object getItem (int position) {
		
		return objects.get(position);
		
	}

	@Override
	public long getItemId (int position) {
		
		return position;
		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view=convertView;
		
		if (view == null) {
			
			view = lInflater.inflate(R.layout.memory_item, parent, false);
			
		}

		MemoryItem m=(MemoryItem)getItem(position);

		((TextView)view.findViewById(R.id.title)).setText(m.getTitle());
		((TextView)view.findViewById(R.id.description)).setText(m.getDescription());
		((ImageView)view.findViewById(R.id.photo)).setImageDrawable(m.getPhoto().getDrawable());
		
		return view;
		
	}

}
