/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees;

import org.jdom.Element;



/**
 *
 * @author proprietaire
 */
abstract public class ElementMusicalDuree extends ElementMusicalSurPortee {
    private Duree duree = null;
    
    
    ElementMusicalDuree(Moment moment, Partie partie, int divisions, Element element) {
        super(moment, partie, element);
        setDuree(new Duree(divisions, element.getChild("duration")));

        if(element.getChild("vague") != null)
            duree.setVague();

    }
    
    
    ElementMusicalDuree(Moment departMoment, Duree duree, Portee portee)
    {
        super(departMoment, portee);
        this.duree = duree;
    }
    

    public Duree getDuree() {
        return duree;
    }
    
    public void setDuree(Duree duree)
    {
        this.duree = duree;
    }


    public void setDureeVague() {
        this.duree.setVague();
    }

    
    public Moment getFinMoment() {
        return new Moment(getDebutMoment().getRational().plus(duree.getRational()));
    }
    
    public void setFinMoment(Moment finMomentNouveau) {
        duree = new Duree(getDebutMoment(), finMomentNouveau);
        
    }
    

    public void dureeDiviserParDeux() {
        setDuree(getDuree().getDureeDiviserParDeux());
    }


    @Override
    public Element sauvegarder()
    {
        Element elementNote = new Element("note");
        Element elementDuree = getDuree().sauvegarder();
        
        elementNote.addContent(elementDuree);

        /* là on ajoute de la décoration*/

        if(getDuree().musicXMLgetType() != null)
            elementNote.addContent(new Element("type").setText(getDuree().musicXMLgetType()));

        if(getDuree().isPremierPetitPoint())
        {
            elementNote.addContent(new Element("dot"));
        }

        if(getDuree().isDeuxiemePetitPoint())
        {
            elementNote.addContent(new Element("dot"));
        }

        if(getDuree().isVague())
        {
            elementNote.addContent(new Element("vague"));
        }
        
        return elementNote;
        

    }
    
    
}
