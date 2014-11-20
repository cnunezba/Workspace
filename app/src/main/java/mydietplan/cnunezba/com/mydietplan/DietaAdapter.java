package mydietplan.cnunezba.com.mydietplan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by cnune_000 on 15/11/2014.
 */
public class DietaAdapter extends BaseAdapter {

    private Context context;
    private List<Dieta> dietas;

    public DietaAdapter(Context context, List<Dieta> dietas){
        this.context = context;
        this.dietas = dietas;
    }

    @Override
    public int getCount() {
        return this.dietas.size();
    }

    @Override
    public Object getItem(int position) {
        return this.dietas.get(position);
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
            rowView = inflater.inflate(R.layout.layout_lista_dieta, parent, false);
        }

        // Set data into the view.
        TextView txtNombreDieta = (TextView) rowView.findViewById(R.id.txtNombreDieta);

        Dieta dieta = this.dietas.get(position);
        txtNombreDieta.setText(dieta.getNombre());

        return rowView;
    }
}
