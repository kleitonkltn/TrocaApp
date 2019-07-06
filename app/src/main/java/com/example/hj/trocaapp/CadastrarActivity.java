package com.example.hj.trocaapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CadastrarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);


    }

    public void Notificar(String pergunta, String resposta) throws IOException, JSONException {
        JSONObject json = new JSONObject();
        json.put("to", "/topics/3214");
        JSONObject data = new JSONObject();
        data.put("titulo", pergunta);
        data.put("texto", resposta);
        JSONObject notification = new JSONObject();
        notification.put("title", "Nova Piada");
        notification.put("body", "Click Aqui para Conferir");
        json.put("notification", notification);
        json.put("data", data);
        WebService ws = new WebService();
        ws.enviar(json);

        Toast.makeText(this, "Nova Piada", Toast.LENGTH_SHORT).show();
    }

    public Boolean verificar() {
        EditText pergunta = (EditText) findViewById(R.id.pergunta);
        EditText resposta = (EditText) findViewById(R.id.resposta);
        String resp = resposta.getText().toString();
        String perg = pergunta.getText().toString();
        if (resp.trim().equals("") || perg.trim().equals("")) {
            Toast.makeText(this, "Preencha os Campos Corretamente", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void adicionar(View view) throws IOException, JSONException {
        if (verificar()) {


            final EditText pergunta = (EditText) findViewById(R.id.pergunta);
            final EditText resposta = (EditText) findViewById(R.id.resposta);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Map<String, Object> trocadilhos = new HashMap<>();
            Calendar c = Calendar.getInstance();

            trocadilhos.put("pergunta", pergunta.getEditableText().toString());
            trocadilhos.put("resposta", resposta.getEditableText().toString());
            trocadilhos.put("createdAt", c.getTime());

            db.collection("trocadilho")
                    .add(trocadilhos)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            try {
                                Log.i("ok", documentReference.getId());
//                            Notificar(pergunta.getText().toString(), resposta.getText().toString());
                                finish();
                            } catch (Exception e) {
                                e.printStackTrace();
                                finish();
                            }

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i("Erro", "Não salvou");
                        }
                    });

        }
    }

    public void listar(View view) {
        finish();
    }

    public void Sobre(View view) {
        Intent i = new Intent(CadastrarActivity.this, FullscreenActivity.class);
        startActivity(i);
    }
}

