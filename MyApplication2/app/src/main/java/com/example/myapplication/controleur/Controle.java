package com.example.myapplication.controleur;
import android.content.Context;

import com.example.myapplication.modele.AccesLocal;
import com.example.myapplication.modele.Profil;
import com.example.myapplication.outils.Serializer;

import java.util.Date;
public final class Controle {
    private static Controle instance=null;
    private static Profil profil;
    private static String nomFic="saveprofil";
    private static AccesLocal accesLocal;
    private Controle(){
        super();
    }
    public static final Controle getInstance(Context contexte){
        if(Controle.instance == null){
            Controle.instance = new Controle();
            accesLocal = new AccesLocal(contexte);
            profil = accesLocal.recupDernier();
     //       recupSerialize(contexte);
        }
        return Controle.instance;
    }
    public void creerProfil(Integer poids,Integer taille,Integer age,Integer sexe, Context contexte){
        profil=new Profil(new Date(),poids,taille,age,sexe);
        accesLocal.ajout(profil);
      //  Serializer.serialize(nomFic, profil,contexte);
    }
    /**
     * récupération de l'objet sérialisé (le profil)
     * @param contexte
     */
    public static void recupSerialize(Context contexte){
        profil = (Profil) Serializer.deSerialize(nomFic,contexte);
    }
    public Integer getPoids(){if(profil == null){return null;}else{return profil.getPoids();}}
    public Integer getTaille(){if(profil == null){return null;}else{return profil.getTaille();}}
    public Integer getAge(){if(profil == null){return null;}else{return profil.getAge();}}
    public Integer getSexe(){if(profil == null){return null; } else{ return profil.getSexe();}}
    public float getImg(){return profil.getImg();}
    public String getMessage(){return profil.getMessage();}
}
