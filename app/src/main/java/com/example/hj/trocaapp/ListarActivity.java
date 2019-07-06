package com.example.hj.trocaapp;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import java.util.HashMap;
import java.util.Map;

public class ListarActivity extends AppCompatActivity {
    String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar);
        FirebaseMessaging.getInstance().subscribeToTopic("3214")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Inscrição Ok";
                        if (!task.isSuccessful()) {
                            msg = "Falha ao inscrever  Ok";
                        }
                        Log.d(TAG, msg);
                        Toast.makeText(ListarActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        preencherListViewComAdapterDePerguntas();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_novo) {
            abrirActivityCadastrar();
        }

        if (id == R.id.menu_sorterio) {
            abrirActivitySorteio();
        }
        return super.onOptionsItemSelected(item);
    }



    public void preencherListViewComAdapterDePerguntas() {
        final ListView lvpiadas = findViewById(R.id.lista_piadas);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> trocadilhos = new HashMap<>();
        db.collection("trocadilho")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            final String[] pergs = new String[task.getResult().size()];
                            final String[] resp = new String[task.getResult().size()];
                            int i = 0;
                            for (DocumentSnapshot document : task.getResult()) {
                                pergs[i] = document.getData().get("pergunta").toString();
                                resp[i] = document.getData().get("resposta").toString();
                                i++;
                            }
                            ArrayAdapter adapter = new ArrayAdapter(ListarActivity.this, android.R.layout.simple_list_item_1, pergs);
                            lvpiadas.setAdapter(adapter);
                            lvpiadas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    lvpiadas.animate().setDuration(200);
                                    lvpiadas.animate().alpha(200);
                                    Toast.makeText(ListarActivity.this, resp[i], Toast.LENGTH_LONG).show();
                                }
                            });
                        } else {
                            Log.w("Chegou", "Error getting documents.", task.getException());
                        }
                    }
                });

    }
    public void abrirActivityCadastrar() {
        Intent i = new Intent(ListarActivity.this, CadastrarActivity.class);
        startActivity(i);
    }

    public void abrirActivitySorteio() {
        Intent i = new Intent(ListarActivity.this, SorteioActivity.class);
        startActivity(i);
    }
}
