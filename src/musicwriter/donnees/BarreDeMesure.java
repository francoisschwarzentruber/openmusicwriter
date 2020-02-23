/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees;

import java.util.Collection;
import org.jdom.Element;

/**
 * Cette classe représente une barre de mesure dans la partition
 * @author proprietaire
 */
final public class BarreDeMesure extends ElementMusical {

    /**
     * type = reprise, reprise double, simple etc.
     */
    private BarreDeMesureType barreDeMesureType;


    /**
     * crée un élément "barre de mesure" à partir des données MusicXML element
     *
     * element ressemble à :
     * <barline location="right">
        <bar-style>light-heavy</bar-style>
        </barline>
     *
     *
     * @param moment
     * @param element
     */
    public BarreDeMesure(Moment moment, Element element) {
        this(moment);
        setBarreDeMesureType(element);
    }


    /**
     *
     * @return le type de barre (reprise, fin, double etc.)
     */
    public BarreDeMesureType getBarreDeMesureType() {
        return barreDeMesureType;
    }

    

    /**
     * <barline location="right">
        <bar-style>light-heavy</bar-style>
        </barline>
     * @param element
     */
    public void setBarreDeMesureType(Element element) {
        if(element.getChild("bar-style") != null)
        {
            if(element.getChild("bar-style").getText().equals("light-light"))
                 barreDeMesureType = BarreDeMesureType.DOUBLE;
        }

        if(element.getChild("repeat") != null)
        {
            String direction = element.getChild("repeat").getAttributeValue("direction");

            if(direction.equals("forward"))
                mettreRepriseVersLaDroite();
            else
                mettreRepriseVersLaGauche();

        }
    }




    private void mettreRepriseVersLaDroite() {
        if(isRepeatBackward())
            barreDeMesureType = BarreDeMesureType.REPRISEDOUBLE;
        else
            barreDeMesureType = BarreDeMesureType.REPRISEDROITE;
    }


    private void mettreRepriseVersLaGauche() {
        if(isRepeatForward())
            barreDeMesureType = BarreDeMesureType.REPRISEDOUBLE;
        else
            barreDeMesureType = BarreDeMesureType.REPRISEGAUCHE;
    }





    /**
     *
     * @param parties
     * @return true tout le temps (une barre de mesure est commune à toutes les parties)
     */
    @Override
    public boolean isSurParties(Collection<Partie> parties) {
        return true;
    }

    
    /**
     * 
     * @return true iff the bar is a forward repeat bar i.e. ||: or :||:
     */
    public boolean isRepeatForward() {
        return (getBarreDeMesureType() == BarreDeMesureType.REPRISEDROITE) || 
                (getBarreDeMesureType() == BarreDeMesureType.REPRISEDOUBLE);
    }

    /**
     * 
     * @return true iff the bar is a backward repeat bar i.e. :|| or :||:
     */
    public boolean isRepeatBackward() {
        return (getBarreDeMesureType() == BarreDeMesureType.REPRISEGAUCHE) || 
                (getBarreDeMesureType() == BarreDeMesureType.REPRISEDOUBLE);
    }

    public enum BarreDeMesureType{
        NORMALE(0), DOUBLE(1), REPRISEGAUCHE(2), REPRISEDROITE(3), REPRISEDOUBLE(4), FIN(5);

        private int numero = 0;

        private BarreDeMesureType(int numero)
        {
            this.numero = numero;
        }
    }


    /**
     * construit une barre normale
     * @param moment
     */
    public BarreDeMesure(Moment moment) {
        this(moment, BarreDeMesureType.NORMALE);
    }



    
    public BarreDeMesure(Moment moment, BarreDeMesureType barreDeMesureType) {
        super(moment);
        getDebutMoment().setPrecede();
        this.barreDeMesureType = barreDeMesureType;
    }

    @Override
    public void deplacer(Moment moment) {
        super.deplacer(moment);
        getDebutMoment().setPrecede();
    }





}
