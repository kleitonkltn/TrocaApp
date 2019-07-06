package com.example.hj.trocaapp;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class SorteioActivity extends AppCompatActivity {
    String[] pergs;
    String[] resp;
    Button btResposta;
    Button btSorteio;
    ImageView imgTopo;
    TextView perguntatv;
    TextView respostatv;
    int idSorteio = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sorteio);
        btSorteio = (Button) findViewById(R.id.brNovoSorteio);
        btResposta = (Button) findViewById(R.id.btExibirResposta);
        perguntatv = (TextView) findViewById(R.id.tvPergunta);
        respostatv = (TextView) findViewById(R.id.tvResposta);
        imgTopo = (ImageView) findViewById(R.id.imgUTC);

    }

    public void sortear() {
        Random r = new Random();
        idSorteio = r.nextInt(pergs.length);
        perguntatv.setText(pergs[idSorteio] + "??");
    }

    @Override
    protected void onStart() {
        super.onStart();
        listar();
    }

    public void listar() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> trocadilhos = new HashMap<>();

        db.collection("trocadilho").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    pergs = new String[task.getResult().size()];
                    resp = new String[task.getResult().size()];
                    int i = 0;
                    for (DocumentSnapshot document : task.getResult()) {
                        pergs[i] = document.getData().get("pergunta").toString();
                        resp[i] = document.getData().get("resposta").toString();
                        i++;
                    }
                    Toast.makeText(SorteioActivity.this, "OK", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SorteioActivity.this, "Erro ao Carregar trocadilhos", Toast.LENGTH_SHORT).show();
                    Log.w("Chegou", "Error getting documents.", task.getException());
                }

            }

        });

    }

    public void exibirResposta(View view) {
        respostatv.setText(resp[idSorteio] + "\n😂😂😂😂");
    }

    public void novoSorteio(View view) throws InterruptedException {
        if (pergs != null) {

            imgTopo.animate().setDuration(1500);
            imgTopo.animate().rotationBy(2160);
            respostatv.setText("");
            perguntatv.setText("");
            btResposta.setVisibility(View.GONE);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    sortear();
                    btResposta.setVisibility(View.VISIBLE);
                }
            }, 1500);

        } else {
            Toast.makeText(this, "Carregando trocadilhos", Toast.LENGTH_LONG).show();
        }
    }
}
