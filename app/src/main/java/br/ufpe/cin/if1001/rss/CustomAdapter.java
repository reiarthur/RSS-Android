package br.ufpe.cin.if1001.rss;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Arthur on 02/04/2018.
 */

public class CustomAdapter extends ArrayAdapter<ItemRSS> {

    // Adapter criado para exibir titulo e data de cada item do RSS

   public CustomAdapter (Context c, int resource, List<ItemRSS> lista) {
      super(c, resource, lista);
   }

   @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      View v = LayoutInflater.
              from(getContext()).
              inflate(R.layout.itemlista, parent, false);


      TextView tvTitulo = (TextView) v.findViewById(R.id.item_titulo);
       TextView tvData = (TextView) v.findViewById(R.id.item_data);

       ItemRSS i = (ItemRSS) getItem(position);

       String titulo = i.getTitle();
       String data = i.getPubDate();

       tvTitulo.setText(titulo);
       tvData.setText(data);

       return v;

   }
}
