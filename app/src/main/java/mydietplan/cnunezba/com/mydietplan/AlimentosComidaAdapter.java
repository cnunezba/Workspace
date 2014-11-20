package mydietplan.cnunezba.com.mydietplan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

/**
 * Created by cnune_000 on 15/11/2014.
 */
public class AlimentosComidaAdapter extends BaseAdapter {

    private Context context;
    private List<Comida> comidas;

    public AlimentosComidaAdapter(Context context, List<Comida> comidas){
        this.context = context;
        this.comidas = comidas;
    }

    @Override
    public int getCount() {
        return this.comidas.size();
    }

    @Override
    public Object getItem(int position) {
        return this.comidas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;

        if (convertView == null) {
            // Create a new view into the list.
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.layout_listado_alimentos_comida, parent, false);
        }

        // Set data into the view.
        TextView txtNombreComida = (TextView) rowView.findViewById(R.id.txtNombreComida);
        EditText txtCantidad = (EditText) rowView.findViewById(R.id.txtCantidad);
        TextView txtProteinaComida = (TextView) rowView.findViewById(R.id.txtProteinaComida);
        TextView txtHdcComida = (TextView) rowView.findViewById(R.id.txtHdcComida);
        TextView txtGrasaComida = (TextView) rowView.findViewById(R.id.txtGrasaComida);

        Comida comida = this.comidas.get(position);

        txtNombreComida.setText(comida.getNombre());
        txtCantidad.setText(comida.getCantidad());
        txtProteinaComida.setText(comida.getProt().toString());
        txtHdcComida.setText(comida.getHdc().toString());
        txtGrasaComida.setText(comida.getGrasa().toString());

        return rowView;
    }
}
