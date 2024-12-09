package com.example.myapplication.vue;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.controleur.Controle;
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }
    private EditText txtPoids;private EditText txtTaille;private EditText txtAge;
    private RadioButton rdHomme;private RadioButton rdFemme;
    private TextView lblIMG;private ImageView imgSmile;
    private Controle controle;
    private void init(){
        txtPoids =(EditText)findViewById(R.id.txtPoids);
        txtTaille =(EditText)findViewById(R.id.txtTaille);
        txtAge =(EditText)findViewById(R.id.txtAge);
        rdHomme= (RadioButton) findViewById (R.id.rdHomme);
        rdFemme= (RadioButton) findViewById (R.id.rdFemme);
        lblIMG=(TextView)findViewById(R.id.lblIMG);
        imgSmile=(ImageView)findViewById(R.id.imgSmile);
        this.controle=Controle.getInstance(this);
        ecouteCalcul();
        recupProfil();
    }
    private void ecouteCalcul(){
        ((Button)findViewById(R.id.btnCalc)).setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                Integer poids = 0;
                Integer taille = 0;
                Integer age = 0;
                Integer sexe = 0;
                //recuperation des données saisies
                try {
                    poids = Integer.parseInt(txtPoids.getText().toString());
                    taille = Integer.parseInt(txtTaille.getText().toString());
                    age = Integer.parseInt(txtAge.getText().toString());
                }catch(Exception e){};
                if(rdHomme.isChecked()){
                    sexe=1;
                }
                //contrôle des données saisies
                if(poids==0 || taille==0 || age==0) {
                    Toast.makeText(MainActivity.this, "Saisie incorrecte", Toast.LENGTH_SHORT).show();
                }else{
                    afficheResult(poids,taille,age,sexe);
                }
            }
        });
    }
    private void afficheResult(Integer poids,Integer taille,Integer age,Integer sexe){
        //creation du profile et recuperation des informations
        this.controle.creerProfil(poids,taille,age,sexe,this);
        float img=this.controle.getImg();
        String message=this.controle.getMessage();
        //affichage
        if (message.equals("normal")) {
            imgSmile.setImageResource(R.drawable.normal);
            lblIMG.setTextColor(Color.GREEN);
        } else if (message.equals("trop faible")) {
            imgSmile.setImageResource(R.drawable.maigre);
            lblIMG.setTextColor(Color.RED);
        } else {
            imgSmile.setImageResource(R.drawable.graisse);
            lblIMG.setTextColor(Color.YELLOW);
        }
        lblIMG.setText(img+" : IMG "+message);
    }
    /**
     * récupération du profil s'il a été sérialisé
     */
    private void recupProfil(){
        if(controle.getPoids() != null){
            txtPoids.setText(controle.getPoids().toString());
            txtTaille.setText(controle.getTaille().toString());
            txtAge.setText(controle.getAge().toString());
            rdFemme.setChecked(true);
            if(controle.getSexe()==1){
                rdHomme.setChecked(true);
            }
            //simule le clic sur le bouton calcul
            ((Button)findViewById(R.id.btnCalc)).performClick();
        }
    }




}