package com.example.tp1.controleur;

import android.content.Context;

import com.example.tp1.modele.AccesLocal;
import com.example.tp1.modele.Profil;
import com.example.tp1.outils.Serializer;

import java.util.Date;

public final class Controle {
    private  static Controle instance = null;
    private  static Profil profil;
    private static String nomFic= "saveProfil";
    private static AccesLocal accesLocal;

    private Controle(){
        super();
    }

    public static final Controle getInstance(Context contexte){
        if(Controle.instance == null){
            Controle.instance = new Controle();
            accesLocal=new AccesLocal(contexte);
            profil=accesLocal.recupDernier();
           // recupSerialize(contexte);
        }
        return  Controle.instance ;
    }

    public void creerProfil(Integer poids , Integer taille , Integer age , Integer sexe, Context contexte){
        profil = new Profil(new Date(),poids , taille , age , sexe ) ;
        accesLocal.ajout(profil);
       // Serializer.serialize(nomFic,profil,contexte);
    }

    public float getImg(){
        return profil.getImg() ;
    }

    public String  getMessage(){
        return profil.getMessage() ;
    }


    private static void recupSerialize(Context contexte){
        profil =(Profil) Serializer.deSerialize(nomFic,contexte);
    }

    public Integer getPoids(){
        if(profil==null){
            return null;
        }else {
            return profil.getPoids();
        }
    }

    public Integer getTaille(){
        if(profil==null){
            return null;
        }else {
            return profil.getTaille();
        }
    }

    public Integer getAge(){
        if(profil==null){
            return null;
        }else {
            return profil.getAge();
        }
    }

    public Integer getSexe(){
        if(profil==null){
            return null;
        }else {
            return profil.getSexe();
        }
    }
}
